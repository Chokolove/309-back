package com.company.back;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.company.back.repository.UserRepository;

@SpringBootTest(
  properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.flyway.enabled=false"
  }
)
class ApplicationTests {

    @Configuration
    static class TestConfig {
        @Bean
        public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }
    }

    @Test
    void contextLoads() {
    }
}