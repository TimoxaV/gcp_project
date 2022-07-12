package gcp.project.cloud.controllers;

import gcp.project.cloud.config.Exchange.CustomMessageGateway;
import gcp.project.cloud.config.TestPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final CustomMessageGateway gateway;

    @Autowired
    public TestController(CustomMessageGateway gateway) {
        this.gateway = gateway;
    }

    @PostMapping
    public String sendToPubSub(@RequestBody TestPayload payload) {
        gateway.sendToPubSub(payload);
        return "Success";
    }
}
