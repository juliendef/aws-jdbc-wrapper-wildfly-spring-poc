package org.example.wildfly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.jboss.logging.Logger;

@RestController
public class Api {

    private final static Logger LOGGER = Logger.getLogger(Api.class.getName());

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/rw")
    public String rw() {
        LOGGER.info("Read write query");

        return jdbcTemplate.query("SELECT * FROM mvms_camera LIMIT 5",
            (rs, rowNum) ->
                new Example(
                    rs.getString("name"),
                    rs.getInt("camera_id")
                )
        ).toString();
    }

    @GetMapping("/ro")
    @Transactional(readOnly = true)
    public String ro() {
        LOGGER.info("Read only query");

        return jdbcTemplate.query("SELECT * FROM mvms_camera LIMIT 5",
                (rs, rowNum) ->
                        new Example(
                                rs.getString("name"),
                                rs.getInt("camera_id")
                        )
        ).toString();
    }
}
