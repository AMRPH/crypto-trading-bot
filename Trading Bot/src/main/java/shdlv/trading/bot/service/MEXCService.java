package shdlv.trading.bot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class MEXCService {

    @Value("${bot.mexc.api.key}")
    private String apiKey;

    @Value("${bot.mexc.api.url}")
    private String apiUrl;

    public void checkConnect(){
//        {{api_url}}/api/v3/ping
    }

    public void exchangeInfo(String symbol){
//        {{api_url}}/api/v3/exchangeInfo
    }

    public float tickerPrice(String symbol){
        String request = apiUrl +
                "/api/v3/ticker/24hr" +
                "?symbol=" + symbol;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(request))
                .GET()
                .build();

        HttpResponse response = null;
        try {
            response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            float price = Float.parseFloat(response.body().toString().split("\"lastPrice\":\"")[1].split("\"")[0]);
            return price;
        } catch (IOException | InterruptedException e) {
            return 0.0F;
        }
    }

    public void buyOrder(String symbol, float quoteOrderQty){
//        {{api_url}}/api/v3/order
    }

    public void sellOrder(String symbol, float quantity, float price){
//        {{api_url}}/api/v3/order
    }




}
