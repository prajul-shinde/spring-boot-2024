package com.omsai.reactivemongo.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    @Bean
    MongoClient mongoClient() {
        return MongoClients.create();
    }

    @Override
    protected String getDatabaseName() {
        return "sfg";
    }

    /* only required in case mongo installed as docker or there is password configured */
//    @Override
//    protected void configureClientSettings(MongoClientSettings.Builder builder) {
//        builder.credential(MongoCredential.createCredential("root",
//                        "admin", "example".toCharArray()))
//                .applyToClusterSettings(settings -> {
//                    settings.hosts((Collections.singletonList(
//                            new ServerAddress("127.0.0.1", 27017)
//                    )));
//                });
//    }
}
