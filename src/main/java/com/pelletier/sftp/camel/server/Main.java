package com.pelletier.sftp.camel.server;

import java.io.IOException;

import org.apache.camel.CamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Starts Camel Context
 * 
 * @author Ryan Pelletier
 *
 */
public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		CamelContext camelContext = (CamelContext) context.getBean("camelContext");

		try {
			camelContext.start();
			System.out.println("Camel Context Started");
			Thread.sleep(1000000000);
			camelContext.stop();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Camel Context Stopped");
	}
}
