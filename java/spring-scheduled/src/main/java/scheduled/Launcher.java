package scheduled;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Launcher {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Launcher.class);
        app.addInitializers(new SpringContextHolder());
        app.run(args);
    }
}
