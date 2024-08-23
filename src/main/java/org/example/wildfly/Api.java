package org.example.wildfly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.jboss.logging.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
public class Api {

    private final static Logger LOGGER = Logger.getLogger(Api.class.getName());

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @GetMapping("/rw")
    public String rw() throws SQLException {
        LOGGER.info("Read write query");

        Connection connection = DataSourceUtils.getConnection(dataSource);
        connection.setReadOnly(false);

        return jdbcTemplate.query("SELECT * FROM mvms_camera LIMIT 5",
            (rs, rowNum) ->
                new Example(
                    rs.getString("name"),
                    rs.getInt("camera_id")
                )
        ).toString();
    }

    @GetMapping("/ro")
    // this might not have the desired effect as we're using JDBC Template instead of hibernate
    @Transactional(readOnly = true, propagation=Propagation.REQUIRED)
    public String ro() throws SQLException {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        connection.setReadOnly(true);

        LOGGER.info("Read only query");

        try {
            return jdbcTemplate.query("SELECT * FROM mvms_camera LIMIT 5",
                    (rs, rowNum) ->
                            new Example(
                                    rs.getString("name"),
                                    rs.getInt("camera_id")
                            )
            ).toString();
        } finally {
            connection.setReadOnly(false);
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }
}
