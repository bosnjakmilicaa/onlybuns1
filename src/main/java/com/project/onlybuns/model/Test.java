package com.project.onlybuns.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
@RequestMapping("/test")
public class Test {

    @Autowired
    private DataSource dataSource;

    @GetMapping
    public String testConnection() {
        try (Connection conn = dataSource.getConnection()) {
            return "Connection to database established!";
        } catch (SQLException e) {
            return "Failed to connect to database: " + e.getMessage();
        }
    }
}
