package gcp.project.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.autoconfigure.storage.GcpStorageAutoConfiguration;

@SpringBootApplication(exclude = {GcpStorageAutoConfiguration.class})
public class CloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudApplication.class, args);
    }
}
