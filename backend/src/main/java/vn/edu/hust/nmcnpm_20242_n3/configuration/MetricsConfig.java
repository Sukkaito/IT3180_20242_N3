package vn.edu.hust.nmcnpm_20242_n3.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.statsd.StatsdMeterRegistry;
import io.micrometer.statsd.StatsdConfig;
import io.micrometer.statsd.StatsdFlavor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class MetricsConfig {

    @Bean
    public StatsdConfig statsdConfig() {
        return new StatsdConfig() {
            @Override
            public String get(String key) {
                return null; // use defaults unless overridden below
            }

            @Override
            public StatsdFlavor flavor() {
                return StatsdFlavor.ETSY; // for Graphite compatibility
            }

            @Override
            public String host() {
                return "host.docker.internal"; // Use host.docker.internal for Docker compatibility
            }

            @Override
            public int port() {
                return 8125;
            }

            @Override
            public Duration pollingFrequency() {
                return Duration.ofSeconds(10);
            }

            @Override
            public boolean enabled() {
                return true;
            }
        };
    }

    @Bean
    public MeterRegistry meterRegistry(StatsdConfig config) {
        return StatsdMeterRegistry.builder(config).build();
    }
}
