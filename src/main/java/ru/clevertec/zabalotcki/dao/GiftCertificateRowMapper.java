package ru.clevertec.zabalotcki.dao;

import ru.clevertec.zabalotcki.model.GiftCertificate;
import ru.clevertec.zabalotcki.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftCertificateRowMapper {

    public static List<GiftCertificate> extractData(ResultSet rs) throws SQLException {
        Map<Long, GiftCertificate> giftCertificateMap = new HashMap<>();

        while (rs.next()) {
            Long id = rs.getLong("id");
            GiftCertificate giftCertificate = giftCertificateMap.computeIfAbsent(id, k -> {
                GiftCertificate gc = new GiftCertificate();
                gc.setId(id);

                try {
                    gc.setName(rs.getString("name"));
                    gc.setDescription(rs.getString("description"));
                    gc.setPrice(rs.getBigDecimal("price"));
                    gc.setDuration(rs.getInt("duration"));
                    gc.setCreateDate(rs.getObject("create_date", LocalDateTime.class));
                    gc.setLastUpdateDate(rs.getObject("last_update_date", LocalDateTime.class));
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                gc.setTags(new ArrayList<>());
                return gc;
            });

            Long tagId = rs.getLong("tag_id");

            if (tagId != null) {
                Tag tag = new Tag();
                tag.setId(tagId);
                tag.setName(rs.getString("tag_name"));
                giftCertificate.getTags().add(tag);
            }
        }

        return new ArrayList<>(giftCertificateMap.values());
    }

    public static GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(rs.getLong("id"));
        giftCertificate.setName(rs.getString("name"));
        giftCertificate.setDescription(rs.getString("description"));
        giftCertificate.setPrice(rs.getBigDecimal("price"));
        giftCertificate.setDuration(rs.getInt("duration"));
        giftCertificate.setCreateDate(rs.getTimestamp("create_date").toLocalDateTime());
        giftCertificate.setLastUpdateDate(rs.getTimestamp("last_update_date").toLocalDateTime());

        Tag tag = new Tag();
        tag.setId(rs.getLong("tag_id"));
        tag.setName(rs.getString("tag_name"));

        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        giftCertificate.setTags(tags);

        while (rs.next()) {
            Long currentId = rs.getLong("id");
            if (!currentId.equals(giftCertificate.getId())) {
                break;
            }

            Tag currentTag = new Tag();
            currentTag.setId(rs.getLong("tag_id"));
            currentTag.setName(rs.getString("tag_name"));

            tags.add(currentTag);
        }

        return giftCertificate;
    }
}
