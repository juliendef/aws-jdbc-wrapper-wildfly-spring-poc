package org.example.wildfly.routes;

import org.example.wildfly.repositories.CameraRepository;
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

    @Autowired
    CameraRepository cameraRepository;

    @GetMapping("/rw")
    @Transactional()
    public String rw() {
        LOGGER.info("Read write query");
        return cameraRepository.findById(2).orElse(null).toString();
    }

    @GetMapping("/ro")
    @Transactional(readOnly = true)
    public String ro() {
        LOGGER.info("Read only query");
        return cameraRepository.findById(2).orElse(null).toString();
    }
}
