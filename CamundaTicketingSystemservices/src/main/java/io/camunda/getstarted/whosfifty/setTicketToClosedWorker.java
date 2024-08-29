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
public class setTicketToClosedWorker {

    public static void main(String[] args) {
        SpringApplication.run(setTicketToClosedWorker.class, args);
    }

    @ZeebeWorker(type = "SetTicketToClosed")
    public void execute(final JobClient client, final ActivatedJob job) {

        try {
            // Set the ticket status to "Closed"
            HashMap<String, Object> variables = new HashMap<>();
            variables.put("status", "Closed");

            // Complete the job
            client.newCompleteCommand(job.getKey())
                    .variables(variables)
                    .send()
                    .exceptionally((throwable -> {
                        throw new RuntimeException("Could not complete job", throwable);
                    }));

            // Display a message that the ticket is closed
            System.out.println("Ticket is closed.");

        } catch (Exception e) {
            int retries = job.getRetries() - 1;

            e.printStackTrace();
            client.newFailCommand(job.getKey())
                    .retries(retries)
                    .send();
        }
    }
}
