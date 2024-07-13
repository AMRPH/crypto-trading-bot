package shdlv.trading.bot.service;

import org.springframework.stereotype.Service;
import shdlv.trading.bot.entity.Order;

import java.util.ArrayList;
import java.util.List;

@Service
public class Service007_02 {
    //parameters
    double profitPercent = 0.007;
    double dropPercent = 0.02;
    long sleepTime = 10;
    double amount = 300;
    double deposit = 10000;



    int tradeCount = 0;

    int sleepCount = 0;

    double profit = 0;

    double lastPriceBuy = 0F;


    List<Order> orderList = new ArrayList<>();

    public void start(double price) throws InterruptedException {
        if (orderList.isEmpty()){
            if (sleepCount != sleepTime){
                sleepCount += 1;
            } else {
                buy(price);
                sleepCount = 0;
            }
        }
        if ((price - lastPriceBuy) / lastPriceBuy <= -dropPercent){
            buy(price);
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

    private void buy(double price){
        deposit -= amount;
        double quantity = amount/price;

        Order order = new Order();
        order.setPriceSell(price*(1 + profitPercent));
        order.setQuantity(quantity);
        orderList.add(order);

        lastPriceBuy = price;
    }

    private void sell(Order order){
        double profitAmount = order.getQuantity() * order.getPriceSell();
        deposit += profitAmount;
        profit += profitAmount - amount;
        tradeCount +=1;
    }

    public void reset(){
        deposit = 10000;
        tradeCount = 0;
        sleepCount = 0;
        profit = 0;
        lastPriceBuy = 0;
        orderList = new ArrayList<>();
    }


}
