package shdlv.trading.bot.service;

import Mexc.Sdk.Spot;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shdlv.trading.bot.entity.Kline;
import shdlv.trading.bot.entity.Order;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class MEXCService {

    @Value("${bot.mexc.api.key}")
    private String apiKey;

    @Value("${bot.mexc.api.secret}")
    private String apiSecret;

    Spot spot;

    @PostConstruct
    public void init() {
        spot = new Spot(apiKey, apiSecret);
    }

    public void checkConnect(){
//        {{api_url}}/api/v3/ping
    }

    public void exchangeInfo(String symbol){
//        {{api_url}}/api/v3/exchangeInfo
    }

    public Double lastPrice(String symbol){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String respData = objectMapper.writeValueAsString(spot.ticker24hr(symbol));
            Map response = objectMapper.readValue(respData, Map.class);
            return Double.parseDouble(response.get("lastPrice").toString());
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public Kline[] kline(String symbol, Long startTime, Long endTime){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            HashMap options = new HashMap();
            options.put("startTime", startTime);
            options.put("endTime", endTime);
            String respData = objectMapper.writeValueAsString(spot.klines(symbol,"1m", options));
            String[][] response = objectMapper.readValue(respData, String[][].class);

            Kline[] klines = new Kline[response.length];
            for (int i = 0; i < response.length; i++){
                Kline kline = new Kline();
                kline.setTimeOpen(Long.parseLong(response[i][0]));
                kline.setOpen(Double.parseDouble(response[i][1]));
                kline.setHigh(Double.parseDouble(response[i][2]));
                kline.setLow(Double.parseDouble(response[i][3]));
                kline.setClose(Double.parseDouble(response[i][4]));
                kline.setVolume(Double.parseDouble(response[i][5]));
                kline.setTimeClose(Long.parseLong(response[i][6]));
                kline.setQuoteVolume(Double.parseDouble(response[i][7]));

                klines[i] = kline;
            }
            return klines;
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public void buyOrder(String symbol, Double quoteOrderQty){
        String side = "BUY";
        String type = "MARKET";


        HashMap options = new HashMap();
        options.put("quoteOrderQty", quoteOrderQty);

        System.out.println("new order" + spot.newOrder(symbol, side, type, options));
    }

    public void sellOrder(String symbol, Double quantity, Double price){
        String side = "SELL";
        String type = "LIMIT";

        HashMap options = new HashMap();
        options.put("quantity", quantity);
        options.put("price", price);

        System.out.println("new order" + spot.newOrder(symbol, side, type, options));
    }

    public Order[] allOrders(String symbol){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String respData = objectMapper.writeValueAsString(spot.allOrders(symbol));
            Order[] response = objectMapper.readValue(respData, Order[].class);
            return response;
        } catch (JsonProcessingException e) {
            return null;
        }
    }




}
