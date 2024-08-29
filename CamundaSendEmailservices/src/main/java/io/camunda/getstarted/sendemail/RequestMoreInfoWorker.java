package io.camunda.getstarted.sendemail;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@SpringBootApplication
@EnableZeebeClient
public class RequestMoreInfoWorker {

    @Autowired
    private JavaMailSender javaMailSender;

    public static void main(String[] args) {
        SpringApplication.run(RequestMoreInfoWorker.class, args);
    }

    @ZeebeWorker(type = "RequestAdditionalInfo")
    public void requestMoreInfo(final JobClient client, final ActivatedJob job) {

        Map<String, Object> variablesAsMap = job.getVariablesAsMap();

        String sender = variablesAsMap.get("sender").toString();
        String receiver = variablesAsMap.get("receiver").toString();
        String subject = "More Information Required";
        String body = "Dear " + sender + ",\n\nWe require more information regarding your request.\nPlease provide additional details.";

        try {
            sendMail(sender, receiver, subject, body);
            String resultMessage = "Request for more information sent to " + sender;

            HashMap<String, Object> variables = new HashMap<>();
            variables.put("requestStatus", "AdditionalInfoRequested");

            client.newCompleteCommand(job.getKey())
                    .variables(variables)
                    .send()
                    .exceptionally((throwable -> {
                        throw new RuntimeException("Could not complete job", throwable);
                    }));
        } catch (MessagingException e) {
            e.printStackTrace();
            client.newFailCommand(job.getKey());
        }
    }

    private void sendMail(String sender, String receiver, String subject, String body) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(receiver);
        helper.setTo(sender);
        helper.setSubject(subject);
        helper.setText(body, true);

        javaMailSender.send(message);
    }
}
