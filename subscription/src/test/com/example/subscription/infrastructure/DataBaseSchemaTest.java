package com.example.subscription.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class DatabaseSchemaTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void subscriptionsTableShouldExist() {
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'SUBSCRIPTIONS'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void requiredIndexesShouldExist() {
        String sql = """
            SELECT INDEX_NAME 
            FROM INFORMATION_SCHEMA.INDEXES 
            WHERE TABLE_NAME = 'SUBSCRIPTIONS'
            """;

        List<String> indexes = jdbcTemplate.queryForList(sql, String.class);

        assertThat(indexes)
                .contains(
                        "idx_subscriptions_user_id",
                        "idx_subscriptions_plan_id",
                        "idx_subscriptions_status",
                        "idx_subscriptions_end_date",
                        "idx_subscriptions_user_status"
                );
    }
}