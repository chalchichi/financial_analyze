package me.hoo.financial.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class insertdataScheduler {

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @Scheduled(cron = "0 0 9 * * *")
    public void scheduleTaskUsingCronExpression() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        String s= restTemplate.getForObject("http://ohora.iptime.org:8000/stocktoday",String.class);
        System.out.println(s);
    }
}
