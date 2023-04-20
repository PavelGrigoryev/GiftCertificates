package ru.clevertec.ecl.giftcertificates.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.giftcertificates.dao.GiftCertificateDao;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM gift_certificates",
                this::mapRowToGiftCertificate
        );
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        List<GiftCertificate> giftCertificates = jdbcTemplate.query(
                "SELECT * FROM gift_certificates WHERE id = ?",
                this::mapRowToGiftCertificate,
                id
        );
        return giftCertificates.isEmpty()
                ? Optional.empty()
                : Optional.of(giftCertificates.get(0));
    }

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(
                    """
                            INSERT INTO gift_certificates (name, description, price, duration, create_date, last_update_date)
                            VALUES (?, ?, ?, ?, ?, ?)
                            """,
                    Statement.RETURN_GENERATED_KEYS
            );
            setGiftCertificateValuesInPrepareStatement(giftCertificate, preparedStatement);
            return preparedStatement;
        }, keyHolder);
        long id = (long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        giftCertificate.setId(id);
        return giftCertificate;
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(
                    """
                            UPDATE gift_certificates
                            SET name = ?, description = ?, price = ?, duration = ?, create_date = ?, last_update_date = ?
                            WHERE id = ?
                            """,
                    Statement.RETURN_GENERATED_KEYS
            );
            setGiftCertificateValuesInPrepareStatement(giftCertificate, preparedStatement);
            preparedStatement.setLong(7, giftCertificate.getId());
            return preparedStatement;
        }, keyHolder);
        long id = (long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        giftCertificate.setId(id);
        return giftCertificate;
    }

    @Override
    public Integer delete(Long id) {
        return jdbcTemplate.update(
                "DELETE FROM gift_certificates WHERE id = ?",
                id
        );
    }

    private GiftCertificate mapRowToGiftCertificate(ResultSet resultSet, int rowNum) throws SQLException {
        return GiftCertificate.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .price(resultSet.getBigDecimal("price"))
                .duration(resultSet.getInt("duration"))
                .createDate(resultSet.getTimestamp("create_date").toLocalDateTime())
                .lastUpdateDate(resultSet.getTimestamp("last_update_date").toLocalDateTime())
                .build();
    }

    private void setGiftCertificateValuesInPrepareStatement(GiftCertificate giftCertificate,
                                                            PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, giftCertificate.getName());
        preparedStatement.setString(2, giftCertificate.getDescription());
        preparedStatement.setBigDecimal(3, giftCertificate.getPrice());
        preparedStatement.setInt(4, giftCertificate.getDuration());
        preparedStatement.setObject(5, giftCertificate.getCreateDate());
        preparedStatement.setObject(6, giftCertificate.getLastUpdateDate());
    }

}
