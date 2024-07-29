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
    public double deposit;


    int sleepCount;
    double lastPriceBuy;
    public int tradeCount;
    public double profit;
    public List<Order> orderList;

    public Bot(double profitPercent, double dropPercent){
        this.profitPercent = profitPercent;
        this.dropPercent = dropPercent;
        this.sleepTime = 0;
        this.amount = 500;
        this.deposit = 10000;

        this.name = "BOT_" + profitPercent + "_" + dropPercent;

        this.sleepCount = 0;
        this.lastPriceBuy = 0;
        this.tradeCount = 0;
        this.profit = 0;
        this.orderList = new ArrayList<>();
    }

    public Bot(double profitPercent, double dropPercent, long sleepTimeSec, double amount, double deposit){
        this.profitPercent = profitPercent;
        this.dropPercent = dropPercent;
        this.sleepTime = sleepTimeSec;
        this.amount = amount;
        this.deposit = deposit;

        this.name = "BOT_" + profitPercent + "_" + dropPercent;

        this.sleepCount = 0;
        this.lastPriceBuy = 0;
        this.tradeCount = 0;
        this.profit = 0;
        this.orderList = new ArrayList<>();
    }

    public void work(double price) {
        if (orderList.isEmpty()){
            if (sleepCount != sleepTime){
                sleepCount += 1;
            } else {
                buy(price);
                lastPriceBuy = price;

                sleepCount = 0;
            }
        } else {
            if ((price - lastPriceBuy) / lastPriceBuy <= -dropPercent){
                buy(price);
                lastPriceBuy = price;
            } else {
                List<Order> removeOrders = new ArrayList<>();
                for (Order order : orderList){
                    if (order.getPriceSell() <= price){
                        sell(order);
                        removeOrders.add(order);
                    }
                }
                orderList.removeAll(removeOrders);

                lastPriceBuy = orderList.stream()
                        .mapToDouble(Order::getPriceBuy)
                        .summaryStatistics()
                        .getMin();
            }
        }
    }

    private void buy(double price){
        if (deposit - amount >= 0){
            deposit -= amount;
            double quantity = amount/price;

            Order order = new Order();
            order.setPriceBuy(price);
            order.setPriceSell(price*(1 + profitPercent));
            order.setQuantity(quantity);
            orderList.add(order);
        }
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
    }
}
