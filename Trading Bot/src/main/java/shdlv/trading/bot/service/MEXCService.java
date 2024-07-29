package shdlv.trading.bot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shdlv.trading.bot.entity.Kline;
import shdlv.trading.bot.entity.Ticker24hr;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

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

    public double lastPrice(String symbol){
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
            ObjectMapper objectMapper = new ObjectMapper();
            Ticker24hr ticker24hr = objectMapper.readValue(response.body().toString(), Ticker24hr.class);
            return Double.parseDouble(ticker24hr.getLastPrice());
        } catch (IOException | InterruptedException e) {
            return 0.0;
        }
    }

    public Kline[] kline(String symbol){
        String request = apiUrl +
                "/api/v3/klines" +
                "?symbol=" + symbol +
                "&interval=" + "1m" +
                "&limit=" + "1000";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(request))
                .GET()
                .build();

        HttpResponse response = null;
        try {
            response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            Object[][] objects = objectMapper.readValue(response.body().toString(), Object[][].class);
            Kline[] klines = new Kline[objects.length];
            for (int i = 0; i < objects.length; i++){
                Kline kline = new Kline();
                kline.setTimeOpen((Long) objects[i][0]);
                kline.setOpen(Double.parseDouble(objects[i][1].toString()));
                kline.setHigh(Double.parseDouble(objects[i][2].toString()));
                kline.setLow(Double.parseDouble(objects[i][3].toString()));
                kline.setClose(Double.parseDouble(objects[i][4].toString()));
                kline.setVolume(Double.parseDouble(objects[i][5].toString()));
                kline.setTimeClose((Long) objects[i][6]);
                kline.setQuoteVolume(Double.parseDouble(objects[i][7].toString()));

                klines[i] = kline;
            }
            return klines;
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    public void buyOrder(String symbol, float quoteOrderQty){
//        {{api_url}}/api/v3/order
    }

    public void sellOrder(String symbol, float quantity, float price){
//        {{api_url}}/api/v3/order
    }




}
