package io.camunda.getstarted.whosfifty;

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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@SpringBootApplication
@EnableZeebeClient
public class MongoDBSurveyHandler {

    private static final Logger LOGGER = Logger.getLogger(MongoDBSurveyHandler.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(MongoDBSurveyHandler.class, args);
    }

    @ZeebeWorker(type = "MongoDBSurvey")
    public void execute(final JobClient client, final ActivatedJob job) {

        Map<String, Object> variablesAsMap = job.getVariablesAsMap();
        LOGGER.info("Received Variables: " + variablesAsMap.toString());

        // Extract survey data from job variables
        String name = (String) variablesAsMap.get("name"); // Update to match the input name
        String email = (String) variablesAsMap.get("email"); // Update to match the input name
        String satisfactionLevel = (String) variablesAsMap.get("q1"); // Update to match the input name
        String reportEase =  (String) variablesAsMap.get("q2");
// Update to match the input name
        String aspectsToImprove;
        Object aspectsToImproveObj = variablesAsMap.get("improveAspects");
        if (aspectsToImproveObj != null && aspectsToImproveObj instanceof String[]) {
            aspectsToImprove = String.join(", ", (String[]) aspectsToImproveObj);
        } else {
            aspectsToImprove = "Communication"; // or any default value you want
        }

// Update to match the input name

        try (var mongoClient = MongoClients.create(getConnectionString())) {
            MongoDatabase database = mongoClient.getDatabase("TicketDB");
            MongoCollection<Document> collection = database.getCollection("survey");

            Document surveyDocument = new Document()
                    .append("name", name)
                    .append("email", email)
                    .append("satisfactionLevel", satisfactionLevel)
                    .append("reportEase", reportEase)
                    .append("aspectsToImprove", aspectsToImprove);

            collection.insertOne(surveyDocument);

            // Prepare variables to send back to the workflow
            HashMap<String, Object> variables = new HashMap<>();
            variables.put("surveyId", surveyDocument.getObjectId("_id").toString());

            // Complete the job
            client.newCompleteCommand(job.getKey())
                    .variables(variables)
                    .send()
                    .exceptionally((throwable -> {
                        throw new RuntimeException("Could not complete job", throwable);
                    }));

        } catch (Exception e) {
            int retries = job.getRetries() - 1;

            LOGGER.severe("Error executing MongoDBSurvey: " + e.getMessage());

            client.newFailCommand(job.getKey())
                    .retries(retries)
                    .errorMessage("Error executing MongoDBSurvey")
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

        return String.format("mongodb+srv://%s:%s@cluster0r.p7maplx.mongodb.net/SurveyDB?retryWrites=true&w=majority", username, password);
    }
}