package me.hoo.financial;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
class mongoServiceTest {
    @Autowired
    ChartDataService chartDataServcie;
    @Autowired
    MongoTemplate mongoTemplate;


    @Test
    public void insertTest() {

        Criteria criteria = new Criteria("date");
        criteria.is("2022-04-01");
        Query query = new Query(criteria);

        HashMap<String,String> findPet = mongoTemplate.findOne(query, HashMap.class, "newscollections");

    }
}