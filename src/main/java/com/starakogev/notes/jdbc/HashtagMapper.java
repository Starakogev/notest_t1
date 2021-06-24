package com.starakogev.notes.jdbc;

import com.starakogev.notes.entity.Hashtag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HashtagMapper implements RowMapper<String> {
    @Override
    public String mapRow(ResultSet resultSet, int i) throws SQLException {
        Hashtag hashtag = new Hashtag();
        hashtag.setHashtag(resultSet.getString("hashtag"));
        return hashtag.getHashtag();
    }
}
