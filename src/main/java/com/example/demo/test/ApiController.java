package com.example.demo.test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class ApiController {

    @GetMapping("getRate")
    public JsonObject getRate(){
        RestTemplate restTemplate = new RestTemplate();

        List<String> strings = Arrays.asList("KRW", "JPY", "PHP");
        String apiKey = "1470504085aeba09086c16146996b0af";
        String format = String.format("http://www.apilayer.net/api/live?access_key=%s", apiKey);

        String forObject = restTemplate.getForObject(format, String.class);

        JsonObject jsonObject = new Gson().fromJson(forObject, JsonObject.class);

        JsonObject quotes = jsonObject.get("quotes").getAsJsonObject();

        JsonObject result = new JsonObject();
        strings.forEach(country -> {
            BigDecimal rate = quotes.get("USD" + country)
                    .getAsBigDecimal()
                    .setScale(2, RoundingMode.HALF_UP);
            result.addProperty(country, rate);
        });

        return result;
    }

    @PostMapping("calRate")
    public JsonObject calRate(String amount, String rate, String country){

        JsonObject result = new JsonObject();
        
        if(amount == null) amount = "0";
        if(rate == null) rate = "0";
        
        int reAmount = Integer.parseInt(amount);
        double reRate = Double.parseDouble(rate);
        
        if(reRate == 0){
            result.addProperty("status", HttpStatus.BAD_REQUEST.value());
            result.addProperty("message", "환율정보가 없습니다.");
            return result;
        }

        if(reAmount == 0 || reAmount > 10000){
            result.addProperty("status", HttpStatus.BAD_REQUEST.value());
            result.addProperty("message", "송금액이 바르지 않습니다.");
            return result;
        }
        
        BigDecimal resultAmount = BigDecimal.valueOf(reAmount).multiply(BigDecimal.valueOf(reRate));

        String message = "수취금액은" +
                resultAmount +
                country +
                " 입니다";

        result.addProperty("status", HttpStatus.OK.value());
        result.addProperty("message", message);

        return result;
    }
}
