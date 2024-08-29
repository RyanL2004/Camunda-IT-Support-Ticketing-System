package io.camunda.getstarted.whosfifty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


import java.net.URLEncoder;
import java.util.logging.Logger;

@SpringBootApplication
@EnableZeebeClient
public class MongoDBTaskHandler {

    private static final Logger LOGGER = Logger.getLogger(MongoDBTaskHandler.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(MongoDBTaskHandler.class, args);
    }

    @ZeebeWorker(type = "MongoDBTask")
    public void execute(final JobClient client, final ActivatedJob job) {

        Map<String, Object> variablesAsMap = job.getVariablesAsMap();
        LOGGER.info("Received Variables: " + variablesAsMap.toString());

        String sender = (String) variablesAsMap.get("sender");
        String priority = (String) variablesAsMap.get("priority");
        String receiver = (String) variablesAsMap.get("receiver");
        String subject = (String) variablesAsMap.get("subject");
        String body = (String) variablesAsMap.get("body");
        String systemMessage = (String) variablesAsMap.get("message");  // added to capture system message

        try (var mongoClient = MongoClients.create(getConnectionString())) {
            MongoDatabase database = mongoClient.getDatabase("TicketDB");
            MongoCollection<Document> collection = database.getCollection("tickets");

            Document ticketDocument = new Document()

                    .append("sender", sender)
                    .append("receiver", receiver)
                    .append("subject", subject)
                    .append("body", body)
                    .append("priority", priority)
                    .append("systemMessage", systemMessage)  // added to store system message
                    .append("createdAt", LocalDate.now().toString());


            collection.insertOne(ticketDocument);

            // Prepare variables to send back to the workflow
            HashMap<String, Object> variables = new HashMap<>();
            variables.put("ticketId", ticketDocument.getObjectId("_id").toString());

            // Complete the job
            client.newCompleteCommand(job.getKey())
                    .variables(variables)
                    .send()
                    .exceptionally((throwable -> {
                        throw new RuntimeException("Could not complete job", throwable);
                    }));

        } catch (Exception e) {
            int retries = job.getRetries() - 1;

            LOGGER.severe("Error executing MongoDBTask: " + e.getMessage());

            client.newFailCommand(job.getKey())
                    .retries(retries)
                    .errorMessage("Error executing MongoDBTask")
                    .send();
        }
    }

    private static String getConnectionString() {
        String username = "Ryan";
        String password = "Sonysony@PS4";

        try {
            password = URLEncoder.encode(password, "UTF-8");
        } catch (Exception e) {
            LOGGER.severe("Error encoding password: " + e.getMessage());
        }

        return String.format("mongodb+srv://%s:%s@cluster0r.p7maplx.mongodb.net/TicketDB?retryWrites=true&w=majority", username, password);
    }
}
