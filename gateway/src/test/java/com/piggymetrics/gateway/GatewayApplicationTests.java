package com.piggymetrics.gateway;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(classes = GatewayApplication.class)
@WebAppConfiguration
public class GatewayApplicationTests extends AbstractTestNGSpringContextTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void fire() {

	}

}
