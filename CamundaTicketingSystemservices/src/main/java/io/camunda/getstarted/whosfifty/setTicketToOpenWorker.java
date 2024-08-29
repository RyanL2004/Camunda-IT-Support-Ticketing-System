package io.camunda.getstarted.whosfifty;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;

@SpringBootApplication
@EnableZeebeClient
public class setTicketToOpenWorker {

    public static void main(String[] args) {
        SpringApplication.run(setTicketToOpenWorker.class, args);
    }

    @ZeebeWorker(type = "SetTicketToOpen")
    public void execute(final JobClient client, final ActivatedJob job) {

        try {
            // Set the ticket status to "Open"
            HashMap<String, Object> variables = new HashMap<>();
            variables.put("status", "Open");

            // Complete the job
            client.newCompleteCommand(job.getKey())
                    .variables(variables)
                    .send()
                    .exceptionally((throwable -> {
                        throw new RuntimeException("Could not complete job", throwable);
                    }));

            // Display a message that the ticket is set to Open
            System.out.println("Ticket status is set to Open.");

        } catch (Exception e) {
            int retries = job.getRetries() - 1;

            e.printStackTrace();
            client.newFailCommand(job.getKey())
                    .retries(retries)
                    .send();
        }
    }
}
