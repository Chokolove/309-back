package com.company.back;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
  properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
    "spring.flyway.enabled=false"
  }
)
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
