package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = ShareItApp.class)
@ComponentScan(basePackages = "ru.practicum.shareit")
class ShareItTests {

	@Test
	void contextLoads() {
	}

}