package com.cars.carrental;

import static org.assertj.core.api.Assertions.assertThat;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@ExtendWith(SpringExtension.class)
public class MongoDbSpringIntegrationTest {
    @Test
    public void test(@Autowired MongoTemplate mongoTemplate) {
        
        DBObject objectToSave = BasicDBObjectBuilder.start()
            .add("key", "value")
            .get();

        mongoTemplate.save(objectToSave, "collection");

        assertThat(mongoTemplate.findAll(DBObject.class, "collection")).extracting("key")
            .containsOnly("value");
    }
}
