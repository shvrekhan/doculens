package com.dev.doculens;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class DoculensApplicationTests {

	@Test
	void contextLoads() {
	}

}
