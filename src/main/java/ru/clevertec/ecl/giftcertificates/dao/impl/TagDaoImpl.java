package ru.clevertec.ecl.giftcertificates.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.giftcertificates.dao.TagDao;
import ru.clevertec.ecl.giftcertificates.model.Tag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagDaoImpl implements TagDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM tag",
                this::mapRowToTag
        );
    }

    @Override
    public Optional<Tag> findById(Long id) {
        List<Tag> tags = jdbcTemplate.query(
                "SELECT * FROM tag WHERE id = ?",
                this::mapRowToTag,
                id
        );
        return tags.isEmpty()
                ? Optional.empty()
                : Optional.of(tags.get(0));
    }

    @Override
    public Tag save(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(
                    """
                            INSERT INTO tag (name)
                            VALUES (?)
                            """,
                    Statement.RETURN_GENERATED_KEYS
            );
            preparedStatement.setString(1, tag.getName());
            return preparedStatement;
        }, keyHolder);
        long id = (long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        tag.setId(id);
        return tag;
    }

    @Override
    public Tag update(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(
                    """
                            UPDATE tag
                            SET name = ?
                            WHERE id = ?
                            """,
                    Statement.RETURN_GENERATED_KEYS
            );
            preparedStatement.setString(1, tag.getName());
            preparedStatement.setLong(2, tag.getId());
            return preparedStatement;
        }, keyHolder);
        long id = (long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        tag.setId(id);
        return tag;
    }

    @Override
    public Integer delete(Long id) {
        return jdbcTemplate.update(
                "DELETE FROM tag WHERE id = ?",
                id
        );
    }

    private Tag mapRowToTag(ResultSet resultSet, int rowNum) throws SQLException {
        return Tag.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }

}
