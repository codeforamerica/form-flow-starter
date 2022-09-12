package org.codeforamerica.formflowstarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Our main application class that auto-configures the spring boot app
 */
@SpringBootApplication
public class FormflowstarterApplication {

	/**
	 * @param args  Command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(FormflowstarterApplication.class, args);
	}
}
