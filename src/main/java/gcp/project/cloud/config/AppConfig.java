package gcp.project.cloud.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import gcp.project.cloud.exceptions.GoogleCredentialsException;
import java.io.IOException;
import java.io.InputStream;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {
        "gcp.project.cloud.repository",
        "gcp.project.cloud.service"
})
public class AppConfig {
    @Value("${bigquery.project-id}")
    private String projectId;

    @Bean
    public ServiceAccountCredentials getCredentials() {
        ServiceAccountCredentials credentials;
        try {
            InputStream credStream = getClass().getResourceAsStream("/gcred.json");
            credentials = ServiceAccountCredentials.fromStream(credStream);
            credStream.close();
            return credentials;
        } catch (IOException e) {
            throw new GoogleCredentialsException("Can't get credentials", e);
        }
    }

    @Bean
    public Storage getGoogleStorage() {
        return StorageOptions.newBuilder()
                .setCredentials(getCredentials())
                .build()
                .getService();
    }

    @Bean
    public BigQuery getBigQuery() {
        return BigQueryOptions.newBuilder()
                .setCredentials(getCredentials())
                .setProjectId(projectId)
                .build()
                .getService();
    }

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
