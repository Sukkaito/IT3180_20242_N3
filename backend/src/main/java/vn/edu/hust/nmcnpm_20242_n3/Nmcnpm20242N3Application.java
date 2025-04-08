package vn.edu.hust.nmcnpm_20242_n3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Nmcnpm20242N3Application {

	public static void main(String[] args) {
		SpringApplication.run(Nmcnpm20242N3Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
			// Your initialization code here
			System.out.println("Application started successfully!");
		};
	}

}
