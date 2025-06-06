package vn.edu.hust.nmcnpm_20242_n3.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.repository.UserRepository;

@Component
@Slf4j
public class DataSeeder {
    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder encoder;

    public DataSeeder(UserRepository userRepository, @Lazy PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Bean
    public CommandLineRunner dataAltering() {
        return args -> {
            logger.atInfo().log("Altering initial data...");

            for (User user : userRepository.findAll()) {
                user.setPassword(encoder.encode(user.getPassword()));
                userRepository.save(user);
            }
            logger.atInfo().log("Default admin account created with username 'admin' and password 'password'.");

            logger.atInfo().log("Initial data seeded successfully.");
        };
    }
}
