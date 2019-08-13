package sample.netty.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Launcher {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Launcher.class);
        app.run(args);
    }
}
