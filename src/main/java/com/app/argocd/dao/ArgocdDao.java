package com.app.argocd.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Locale;

@Repository
public class ArgocdDao {
    @Autowired
    private ObjectMapper objectMapper;

    public JsonNode persons (){
        Faker faker = new Faker();
        ArrayNode persons = objectMapper.createArrayNode();

        for (int i = 0; i < 15; i++) {
            persons.add(objectMapper.createObjectNode()
                    .put("firstName", faker.name().firstName())
                    .put("lastName", faker.name().lastName())
                    .put("title", faker.name().title())
                    .put("suffix", faker.name().suffix())
                    .put("address", faker.address().streetAddress())
                    .put("city", faker.address().cityName())
                    .put("country", faker.address().country()));
        }
        return persons;
    }

    public JsonNode foods (){
        Faker faker = new Faker(new Locale("de"));
        ArrayNode foods = objectMapper.createArrayNode();

        for (int i = 0; i < 10; i++) {
            foods.add(objectMapper.createObjectNode()
                    .put("ingredients", faker.food().ingredient())
                    .put("spices", faker.food().spice())
                    .put("measurements", faker.food().measurement()));
        }
        return foods;
    }

    public JsonNode getRandomBook (){
        Faker faker = new Faker(new Locale("en-US"));
        ArrayNode books = objectMapper.createArrayNode();

        for (int i = 0; i < 10; i++) {
            books.add(objectMapper.createObjectNode()
                    .put("author", faker.book().author())
                    .put("genre", faker.book().genre())
                    .put("publisher", faker.book().publisher())
                    .put("title", faker.book().title()));
        }

        return books;
    }
}
