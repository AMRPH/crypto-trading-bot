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


    double initDeposit;
    boolean soft = false;
    boolean reinvestment = false;

    int sleepCount;
    double lastPriceBuy;
    public int tradeCount;
    public double profit;
    public List<Order> orderList;

    public Bot(double profitPercent, double dropPercent){
        this.profitPercent = profitPercent;
        this.dropPercent = dropPercent;
        this.sleepTime = 0;
        this.initDeposit = 10000;
        this.deposit = initDeposit;
        this.amount = 500;

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
        this.initDeposit = deposit;
        this.deposit = initDeposit;

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
        if (soft){
            double amountSoft = amount * (deposit/initDeposit);
            if (deposit - amountSoft >= 0 && amountSoft >= 10){
                deposit -= amountSoft;

                double quantity = amountSoft/price;

                Order order = new Order();
                order.setPriceBuy(price);
                order.setAmountBuy(amountSoft);
                order.setPriceSell(price*(1 + profitPercent));
                order.setQuantity(quantity);
                orderList.add(order);
            }

        } else {
            if (deposit - amount >= 0){
                deposit -= amount;

                double quantity = amount/price;

                Order order = new Order();
                order.setPriceBuy(price);
                order.setAmountBuy(amount);
                order.setPriceSell(price*(1 + profitPercent));
                order.setQuantity(quantity);
                orderList.add(order);
            }
        }
    }

    private void sell(Order order){
        double profitAmount = order.getQuantity() * order.getPriceSell();
        if (reinvestment){
            deposit += profitAmount;
            initDeposit += profitAmount - order.getAmountBuy();
        } else {
            deposit += order.getAmountBuy();
        }
        profit += profitAmount - order.getAmountBuy();
        tradeCount += 1;
    }

    public void resetInfo(){
        tradeCount = 0;
    }
}
