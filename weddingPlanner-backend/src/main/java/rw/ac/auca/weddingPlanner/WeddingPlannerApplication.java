package rw.ac.auca.weddingPlanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "rw.ac.auca")
@EntityScan(basePackages = "rw.ac.auca")
@EnableJpaRepositories(basePackages = "rw.ac.auca")
public class WeddingPlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeddingPlannerApplication.class, args);
	}

}
