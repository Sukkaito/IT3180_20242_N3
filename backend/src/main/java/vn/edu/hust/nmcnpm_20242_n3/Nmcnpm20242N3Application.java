package vn.edu.hust.nmcnpm_20242_n3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import vn.edu.hust.nmcnpm_20242_n3.service.ApplicationService;

@SpringBootApplication
@EnableScheduling
public class Nmcnpm20242N3Application {
	private static ConfigurableApplicationContext context;

	@Autowired
	private ApplicationService applicationService;

	public static void main(String[] args) {
		context = SpringApplication.run(Nmcnpm20242N3Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
			// Your initialization code here
			applicationService.onApplicationStartup();
			System.out.println("Application started successfully!");
		};
	}

	public static void restart() {
		ApplicationArguments args = context.getBean(ApplicationArguments.class);

		Thread thread = new Thread(() -> {
			context.close();
			context = SpringApplication.run(Nmcnpm20242N3Application.class, args.getSourceArgs());
		});

		thread.setDaemon(false);
		thread.start();
	}
}
