package gcp.project.cloud.function;

import gcp.project.cloud.function.CloudFunctionExample.GCSEvent;
import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.io.IOException;

public class CloudFunctionExample implements BackgroundFunction<GCSEvent> {
    private static final String REQUEST_URL = "REQUEST_URL";
    private static final Logger logger = Logger.getLogger(CloudFunctionExample.class.getName());

    @Override
    public void accept(GCSEvent event, Context context) {
        logger.info("Processing file: " + event.name);
        String requestBody = String.format("{\"bucketName\":\"%s\",\"objectName\":\"%s\"}",
                event.bucket, event.name);
        try {
            URL url = new URL(REQUEST_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            try (DataOutputStream writer = new DataOutputStream(con.getOutputStream())) {
                writer.write(requestBody.getBytes(StandardCharsets.UTF_8));
            }
            int responseCode = con.getResponseCode();
            con.disconnect();
            logger.info("File processing ended with status: " + responseCode);
        } catch (IOException e) {
            throw new RuntimeException("can't get data", e);
        }
    }

    public static class GCSEvent {
        String bucket;
        String name;
        String metageneration;
    }
}
