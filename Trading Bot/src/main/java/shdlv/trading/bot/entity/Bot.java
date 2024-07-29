package shdlv.trading.bot.entity;

import java.util.ArrayList;
import java.util.List;

public class Bot {
    //parameters

    public String name;
    double profitPercent;
    double dropPercent;
    long sleepTime;
    double amount;
    double deposit;


    int sleepCount = 0;

    double lastPriceBuy = 0;


    public int tradeCount = 0;

    public int maxSizeOrderList = 0;

    public double profit = 0;

    public Bot(double profitPercent, double dropPercent){
        this.profitPercent = profitPercent;
        this.dropPercent = dropPercent;
        this.sleepTime = 10;
        this.amount = 500;
        this.deposit = 10000;

        this.name = "BOT_" + profitPercent + "_" + dropPercent;
    }

    public Bot(double profitPercent, double dropPercent, long sleepTimeSec, double amount, double deposit){
        this.profitPercent = profitPercent;
        this.dropPercent = dropPercent;
        this.sleepTime = sleepTimeSec;
        this.amount = amount;
        this.deposit = deposit;

        this.name = "BOT_" + profitPercent + "_" + dropPercent;
    }

    List<Order> orderList = new ArrayList<>();

    public void work(double price) throws InterruptedException {
        if (orderList.isEmpty()){
            if (sleepCount != sleepTime){
                sleepCount += 1;
            } else {
                buy(price);
                sleepCount = 0;
            }
        } else {
            if ((price - lastPriceBuy) / lastPriceBuy <= -dropPercent){
                buy(price);
            }
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

        maxSizeOrderList = Math.max(maxSizeOrderList, orderList.size());
    }

    private void sell(Order order){
        double profitAmount = order.getQuantity() * order.getPriceSell();
        deposit += profitAmount;
        profit += profitAmount - amount;
        tradeCount += 1;
    }

    public void resetInfo(){
        tradeCount = 0;
        profit = 0;
        maxSizeOrderList = 0;
    }
}
