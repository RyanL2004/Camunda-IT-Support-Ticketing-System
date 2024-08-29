package io.camunda.getstarted.whosfifty;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;

import java.util.logging.Logger;

@SpringBootApplication
@EnableZeebeClient
public class CompleteSurveyWorker {

    private static final Logger LOGGER = Logger.getLogger(CompleteSurveyWorker.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(CompleteSurveyWorker.class, args);
    }

    @ZeebeWorker(type = "CompleteSurvey")
    public void execute(final JobClient client, final ActivatedJob job) {

        try {
            // Complete the job
            client.newCompleteCommand(job.getKey())
                    .send()
                    .exceptionally((throwable -> {
                        throw new RuntimeException("Could not complete job", throwable);
                    }));

        } catch (Exception e) {
            int retries = job.getRetries() - 1;

            LOGGER.severe("Error executing CompleteSurveyWorker: " + e.getMessage());

            client.newFailCommand(job.getKey())
                    .retries(retries)
                    .errorMessage("Error executing CompleteSurveyWorker")
                    .send();
        }
    }
}
