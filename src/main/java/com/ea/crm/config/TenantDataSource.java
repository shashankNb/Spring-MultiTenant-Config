package com.ea.crm.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TenantDataSource implements Serializable {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Environment environment;

    private final HashMap<String, DataSource> dataSources = new HashMap<>();

    @Autowired
    public TenantDataSource(NamedParameterJdbcTemplate jdbcTemplate, Environment environment) {
        this.jdbcTemplate = jdbcTemplate;
        this.environment = environment;
    }

    public DataSource getDataSource(Map<String, Object> app) {
        String appId = app.get("app_id").toString();
        if (dataSources.get(appId) != null) {
            return dataSources.get(appId);
        }
        DataSource dataSource = createDataSource(app);
        if (dataSource != null) {
            dataSources.put(appId, dataSource);
        }
        return dataSource;
    }

    @PostConstruct
    public Map<String, DataSource> getAll() {
        List<Map<String, Object>> apps = jdbcTemplate.queryForList("SELECT * FROM EA_USER.dbo.APPS", new HashMap<>());
        apps.stream().forEach(i -> System.out.println(i.get("db_name").toString()));
        Map<String, DataSource> result = new HashMap<>();
        for (Map<String, Object> app : apps) {
            DataSource dataSource = getDataSource(app);
            result.put(app.get("app_id").toString(), dataSource);
        }
        return result;
    }

    private DataSource createDataSource(Map<String, Object> app) {
        if (app != null) {
            DataSourceBuilder<?> factory = DataSourceBuilder
                    .create().driverClassName(environment.getProperty("com.microsoft.sqlserver.jdbc.SQLServerDriver"))
                    .username(environment.getProperty("spring.datasource.username"))
                    .password(environment.getProperty("spring.datasource.password"))
                    .url("jdbc:sqlserver://GHOST\\SQLEXPRESS:52598;database="+ app.get("db_name").toString() +";encrypt=true;trustServerCertificate=true");
            HikariConfig config = new HikariConfig();
            config.setSchema(app.get("db_name").toString());
            config.setDataSource(factory.build());
            config.setMaximumPoolSize(10);
            return new HikariDataSource(config);
        }
        return null;
    }

}
