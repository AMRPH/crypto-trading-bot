package shdlv.trading.bot.entity;

import java.util.ArrayList;
import java.util.List;

public class Bot {
    //parameters

    public String name;
    Double profitPercent;
    Double dropPercent;
    Double amount;
    public double deposit;
    int part;


    Double initDeposit;
    boolean reinvestment = true;
    boolean dynamicalAmount = false;


    Double lastPriceBuy;
    public int tradeCount;
    public int maxsizeorderslist;
    public Double profit;
    public List<Order> orderList;

    public Bot(double profitPercent, double dropPercent, double deposit, int part){
        this.profitPercent = profitPercent;
        this.dropPercent = dropPercent;
        this.part = part;
        this.initDeposit = deposit;
        this.deposit = initDeposit;
        this.amount = deposit/part;

        this.name = "BOT_" + (profitPercent*100) + "%_" + (dropPercent*100) + "%_" + part;

        this.lastPriceBuy = 0.0;
        this.tradeCount = 0;
        this.profit = 0.0;
        this.maxsizeorderslist = 0;
        this.orderList = new ArrayList<>();
    }

    public void work(double currentPrice) {
        if (orderList.isEmpty()){
            buy(currentPrice);
            lastPriceBuy = currentPrice;
        } else {
            if ((currentPrice - lastPriceBuy) / lastPriceBuy <= -dropPercent){
                while (lastPriceBuy * (1-dropPercent) >= currentPrice){
                    lastPriceBuy = lastPriceBuy * (1-dropPercent);
                    buy(lastPriceBuy);
                }
            } else {
                List<Order> removeOrders = new ArrayList<>();
                for (Order order : orderList){
                    if (order.getPriceSell() <= currentPrice){
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

        maxsizeorderslist = Math.max(maxsizeorderslist, orderList.size());
    }

    private void buy(double price){
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

    public void updateAmount(){
        if (dynamicalAmount){
            amount = initDeposit/part;
        }
        deposit -= 150.0;
        initDeposit -= 150.0;
    }

    public void resetInfo(){
        tradeCount = 0;
        profit = 0.0;
        maxsizeorderslist = 0;
    }
}
