package com.cegeka.cnads.simpleproject;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest
class HelloControllerIntegrationTest {

	@Autowired
	private HelloController helloController;

	@Test
	void hello() {
		String result = helloController.hello("test");

		assertThat(result).isEqualTo("Hello test");
		assertThat(helloController.getUsers()).contains("test");
	}

}
