package ru.clevertec.zabalotcki.dao;

import ru.clevertec.zabalotcki.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagRowMapper {

    public static List<Tag> extractData(ResultSet rs) throws SQLException {
        Map<Long, Tag> tags = new HashMap<>();

        while (rs.next()) {
            Long id = rs.getLong("id");
            tags.computeIfAbsent(id, k -> {
                Tag newTag = new Tag();
                newTag.setId(id);
                try {
                    newTag.setName(rs.getString("name"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return newTag;
            });
        }

        return new ArrayList<>(tags.values());
    }

    public static Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getLong("id"));
        tag.setName(rs.getString("name"));

        return tag;
    }
}
