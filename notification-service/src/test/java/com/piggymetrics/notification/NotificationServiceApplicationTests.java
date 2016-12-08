package com.piggymetrics.notification;

import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(classes = NotificationServiceApplication.class, initializers = ConfigFileApplicationContextInitializer.class)
@WebAppConfiguration
public class NotificationServiceApplicationTests extends AbstractTestNGSpringContextTests {

	@Test
	public void contextLoads() {
	}

}
