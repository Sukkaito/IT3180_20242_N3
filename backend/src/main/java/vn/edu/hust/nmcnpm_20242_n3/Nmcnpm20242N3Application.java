package vn.edu.hust.nmcnpm_20242_n3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
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

			applicationService.onApplicationStartup();
			System.out.println("Application started successfully!");
		};
	}

	@Bean
	CommandLineRunner setupTriggers(JdbcTemplate jdbcTemplate) {
		return args -> {
			// Drop existing trigger if exists
			jdbcTemplate.execute("DROP TRIGGER IF EXISTS before_user_delete ON users");

			// Drop existing function if exists
			jdbcTemplate.execute("DROP FUNCTION IF EXISTS before_user_delete_func()");

			// Create function
			jdbcTemplate.execute(
					"CREATE OR REPLACE FUNCTION before_user_delete_func() RETURNS TRIGGER AS '\n" +
							"BEGIN\n" +
							"    DELETE FROM fines WHERE user_id = OLD.id;\n" +
							"    DELETE FROM book_requests WHERE user_id = OLD.id;\n" +
							"    DELETE FROM book_loans WHERE user_id = OLD.id;\n" +
							"    DELETE FROM subscriptions WHERE user_id = OLD.id;\n" +
							"    RETURN OLD;\n" +
							"END;\n" +
							"' LANGUAGE plpgsql"
			);

			// Create trigger
			jdbcTemplate.execute(
					"CREATE TRIGGER before_user_delete " +
							"BEFORE DELETE ON users " +
							"FOR EACH ROW " +
							"EXECUTE FUNCTION before_user_delete_func()"
			);

			System.out.println("Trigger and function created successfully");
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
