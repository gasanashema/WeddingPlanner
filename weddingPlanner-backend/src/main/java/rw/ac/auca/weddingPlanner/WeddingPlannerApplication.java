package rw.ac.auca.weddingPlanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "rw.ac.auca")
@EntityScan(basePackages = "rw.ac.auca")
@EnableJpaRepositories(basePackages = {"rw.ac.auca.user", "rw.ac.auca.wedding", "rw.ac.auca.ceremony", "rw.ac.auca.auth", "rw.ac.auca.task", "rw.ac.auca.homeprep", "rw.ac.auca.template", "rw.ac.auca.budget", "rw.ac.auca.guest", "rw.ac.auca.invitation"})
@EnableMongoRepositories(basePackages = "rw.ac.auca.nosql")
public class WeddingPlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeddingPlannerApplication.class, args);
	}

}
