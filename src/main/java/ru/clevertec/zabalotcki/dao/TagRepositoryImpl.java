package ru.clevertec.zabalotcki.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.clevertec.zabalotcki.excepton.EntityNotFoundException;
import ru.clevertec.zabalotcki.model.Tag;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Tag save(Tag tag) {
        String sql = "INSERT INTO tag (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);

        Long tagId = keyHolder.getKey().longValue();
        tag.setId(tagId);
        return tag;
    }

    @Override
    public Tag update(Tag tag) {
        String sql = "UPDATE tag SET name = ? WHERE id = ?";
        int rowsUpdated = jdbcTemplate.update(sql,
                tag.getName(),
                tag.getId());
        if (rowsUpdated == 0) {
            throw new EntityNotFoundException("Tag with id " + tag.getId() + " not found");
        }
        Long id = tag.getId();
        jdbcTemplate.update("DELETE FROM gift_certificate_tag WHERE tag_id = ?", id);
        return tag;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM tag WHERE id = ?";
        int rowsDeleted = jdbcTemplate.update(sql, id);
        if (rowsDeleted == 0) {
            throw new EntityNotFoundException("Tag with id " + id + " not found");
        }
    }

    @Override
    public Optional<Tag> findById(Long id) {
        String sql = "SELECT id, name FROM tag WHERE id = ?";

        RowMapper<Tag> mapper = TagRowMapper::mapRow;
        List<Tag> result = jdbcTemplate.query(sql, mapper, id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }


    @Override
    public List<Tag> findAll() {
        String sql = "SELECT id, name FROM tag";

        ResultSetExtractor<List<Tag>> rse = TagRowMapper::extractData;
        return jdbcTemplate.query(sql, rse);
    }

}
