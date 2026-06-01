package ru.mephi.statisticschecker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class StatisticsWorkCheckerApplicationTests {

    @Test
    void contextLoads() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(StatisticsWorkCheckerApplication.class)
                .web(WebApplicationType.NONE)
                .run()) {
            assertThat(context.isRunning()).isTrue();
        }
    }
}
