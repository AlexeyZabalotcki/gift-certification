package ru.clevertec.zabalotcki.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.clevertec.zabalotcki.excepton.EntityNotFoundException;
import ru.clevertec.zabalotcki.model.GiftCertificate;
import ru.clevertec.zabalotcki.model.Tag;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TagRepositoryImpl tagRepository;

    @Override
    public List<GiftCertificate> findAll() {
        String sql = "SELECT gc.id, gc.name, description, price, duration, create_date, last_update_date, t.id as tag_id, t.name as tag_name " +
                "FROM gift_certificate as gc " +
                "LEFT JOIN gift_certificate_tag gct ON id = gift_certificate_id LEFT OUTER JOIN tag t on tag_id = t.id";

        ResultSetExtractor<List<GiftCertificate>> rse = GiftCertificateRowMapper::extractData;
        return jdbcTemplate.query(sql, rse);
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        String sql = "SELECT gc.id, gc.name, description, price, duration, create_date, last_update_date, t.id as tag_id, t.name as tag_name " +
                "FROM gift_certificate as gc " +
                "LEFT JOIN gift_certificate_tag gct ON id = gift_certificate_id " +
                "LEFT OUTER JOIN tag t on tag_id = t.id " +
                "WHERE gc.id = ?";

        RowMapper<GiftCertificate> mapper = GiftCertificateRowMapper::mapRow;

        List<GiftCertificate> result = jdbcTemplate.query(sql, mapper, id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public void save(GiftCertificate giftCertificate) {
        String sql = "INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, giftCertificate.getName());
            ps.setString(2, giftCertificate.getDescription());
            ps.setBigDecimal(3, giftCertificate.getPrice());
            ps.setInt(4, giftCertificate.getDuration());
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        giftCertificate.setId(id);

        List<Tag> tags = new ArrayList<>(giftCertificate.getTags());
        workWithTagsId(tags);

        insertIntoIntermediateTable(giftCertificate, id);
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        String sql = "UPDATE gift_certificate SET name = ?, description = ?, price = ?, duration = ?, last_update_date = ? WHERE id = ?";
        int rowsUpdated = jdbcTemplate.update(sql,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration(),
                Timestamp.valueOf(LocalDateTime.now()),
                giftCertificate.getId());
        if (rowsUpdated == 0) {
            throw new EntityNotFoundException("Gift certificate with id " + giftCertificate.getId() + " not found");
        }
        Long id = deleteFromIntermediateTable(giftCertificate);

        updateTags(giftCertificate, id);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM gift_certificate WHERE id = ?";
        int rowsDeleted = jdbcTemplate.update(sql, id);
        if (rowsDeleted == 0) {
            throw new EntityNotFoundException("Gift certificate with id " + id + " not found");
        }
    }

    @Override
    public List<GiftCertificate> searchByNameOrDescription(String query) {
        String pattern = "%" + query + "%";
        String request = "SELECT gc.id, gc.name, description, price, duration, create_date, last_update_date, t.id as tag_id, t.name as tag_name " +
                "FROM gift_certificate as gc " +
                "LEFT JOIN gift_certificate_tag gct ON id = gift_certificate_id " +
                "LEFT OUTER JOIN tag t on tag_id = t.id  " +
                "WHERE gc.name LIKE ? OR t.name LIKE ? OR description LIKE ? " +
                "GROUP BY gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date, t.id " +
                "ORDER BY gc.id ASC";

        ResultSetExtractor<List<GiftCertificate>> rse = GiftCertificateRowMapper::extractData;
        return jdbcTemplate.query(request, rse, pattern, pattern, pattern);
    }

    @Override
    public List<GiftCertificate> getAllSorted(String sortBy) {
        String request = "SELECT gc.id, gc.name, description, price, duration, create_date, last_update_date, t.id as tag_id, t.name as tag_name " +
                "FROM gift_certificate as gc " +
                "LEFT JOIN gift_certificate_tag gct ON id = gift_certificate_id " +
                "LEFT OUTER JOIN tag t on tag_id = t.id " +
                "GROUP BY gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date, t.id " +
                "ORDER BY ?";

        ResultSetExtractor<List<GiftCertificate>> rse = GiftCertificateRowMapper::extractData;
        List<GiftCertificate> list = new ArrayList<>(Objects.requireNonNull(jdbcTemplate.query(request, rse, sortBy)));
        return list;
    }

    private void insertIntoIntermediateTable(GiftCertificate giftCertificate, long id) {
        for (Tag tag : giftCertificate.getTags()) {
            String tagSql = "INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id) VALUES (?, ?)";
            jdbcTemplate.update(tagSql, id, tag.getId());
        }
    }

    private void workWithTagsId(List<Tag> giftCertificate) {
        for (Tag tag : giftCertificate) {
            String selectSql = "SELECT id FROM tag WHERE name = ?";
            List<Long> tagIds = jdbcTemplate.queryForList(selectSql, Long.class, tag.getName());
            if (!tagIds.isEmpty()) {
                tag.setId(tagIds.get(0));
            } else {
                tagRepository.save(tag);
                tagIds.add(tag.getId());
            }
        }
    }

    private Long deleteFromIntermediateTable(GiftCertificate giftCertificate) {
        Long id = giftCertificate.getId();
        jdbcTemplate.update("DELETE FROM gift_certificate_tag WHERE gift_certificate_id = ?", id);
        return id;
    }

    private void updateTags(GiftCertificate giftCertificate, Long id) {
        List<Tag> tags = new ArrayList<>();
        for (Tag tag : giftCertificate.getTags()) {
            tags.add(tag);
            workWithTagsId(tags);
            String tagSql = "INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id) VALUES (?, ?)";
            jdbcTemplate.update(tagSql, id, tag.getId());
        }
    }
}
