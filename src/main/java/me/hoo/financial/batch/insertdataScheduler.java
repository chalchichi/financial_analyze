package me.hoo.financial.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class insertdataScheduler {

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @Value("${flask.insert.server}")
    String insertURL;

    @Scheduled(cron = "0 0 9 * * *")
    public void scheduleTaskUsingCronExpression() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        String s= restTemplate.getForObject(insertURL,String.class);
        System.out.println(s);
    }
}
