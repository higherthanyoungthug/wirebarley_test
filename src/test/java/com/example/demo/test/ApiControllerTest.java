package com.example.demo.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ApiControllerTest {

    @Test
    public void 환율_API_확인(){
        RestTemplate restTemplate = new RestTemplate();

        List<String> strings = Arrays.asList("KRW", "JPY", "PHP");
        String apiKey = "1470504085aeba09086c16146996b0af";
        String format = String.format("http://www.apilayer.net/api/live?access_key=%s", apiKey);

        String forObject = restTemplate.getForObject(format, String.class);

        JsonObject jsonObject = new Gson().fromJson(forObject, JsonObject.class);

        String success = jsonObject.get("success").getAsString();

        then("true").isEqualTo(success);
    }
}