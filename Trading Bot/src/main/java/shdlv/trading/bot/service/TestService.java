package shdlv.trading.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shdlv.trading.bot.entity.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private MEXCService mexcService;

    //parameters
    float profitPercent = 0.003F;
    float dropPercent = 0.01F;

    int tradeCount = 0;
    float profit = 0f;

    float deposit = 10000F;

    float amount = 300F;


    float lastPriceBuy = 0F;

    List<Order> orderList = new ArrayList<>();

    @Scheduled(fixedRate = 1000L)
    public void start() throws InterruptedException {
        float price = mexcService.tickerPrice("KASUSDT");
        if (orderList.isEmpty()){
            Thread.sleep(10000L);
            buy(price);
            System.out.println("New buy " + price);
        }
        if ((price - lastPriceBuy) / lastPriceBuy <= -dropPercent){
            buy(price);
            System.out.println("buy " + price);
        }
        if (!orderList.isEmpty()){
            List<Order> removeOrders = new ArrayList<>();
            for (Order order : orderList){
                if (order.getPriceSell() <= price){
                    sell(order);
                    removeOrders.add(order);
                }
            }
            orderList.removeAll(removeOrders);
        }
    }

    private void buy(float price){
        deposit -= amount;
        float quantity = amount/price;

        Order order = new Order();
        order.setPriceSell(price*(1F+ profitPercent));
        order.setQuantity(quantity);
        orderList.add(order);

        lastPriceBuy = price;
    }

    private void sell(Order order){
        float profitAmount = order.getQuantity() * order.getPriceSell();
        deposit += profitAmount;
        profit += profitAmount - amount;
        tradeCount +=1;
        System.out.println("sell, profit " + (profitAmount - amount));
    }

    @Scheduled(fixedRate = 61000L)
    public void printDeposit(){
        System.out.println(deposit);
        System.out.println(profit);
        if (!orderList.isEmpty()){
            System.out.println(orderList);
        }
    }
}
