package shdlv.trading.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shdlv.trading.bot.entity.BotStat;
import shdlv.trading.bot.entity.Order;
import shdlv.trading.bot.entity.TOrder;
import shdlv.trading.bot.repository.BotStatRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class Bot {

    @Autowired
    private MEXCService mexcService;

    @Autowired
    private BotStatRepository botsStatRepository;

    //parameters
    String name;
    Double profitPercent;
    Double dropPercent;
    Integer sleepTime;
    Double amount;
    Double initDeposit;
    Double deposit;


    Boolean reinvestment = true;

    Integer sleepCount;
    Double lastPriceBuy;
    Integer tradeCount;
    Double profit;

    List<TOrder> orderList;


    private void initBot(){
        this.profitPercent = 0.001;
        this.dropPercent = 0.001;
        this.sleepTime = 10;
        this.initDeposit = 450.0;
        this.deposit = initDeposit;
        this.amount = 10.0;

        this.name = "BOT_" + profitPercent + "_" + dropPercent;

        this.sleepCount = 0;
        this.lastPriceBuy = 100000.0;
        this.tradeCount = 0;
        this.profit = 0.0;
        this.orderList = new ArrayList<>();
    }

    public void work(Double price) {
        if (orderList.isEmpty()){
            if (!Objects.equals(sleepCount, sleepTime)){
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
            }
            List<TOrder> removeOrders = new ArrayList<>();
            for (TOrder order : orderList){
                if (order.getPriceSell() <= price){
                    sell(order);
                    removeOrders.add(order);
                }
            }
            orderList.removeAll(removeOrders);

            lastPriceBuy = orderList.stream()
                    .mapToDouble(TOrder::getPriceBuy)
                    .summaryStatistics()
                    .getMin();
        }
    }

    private void buy(Double price){
        if (deposit - amount >= 0){
//            mexcService.buyOrder("KASUSDT", amount);
//            mexcService.sellOrder("KASUSDT", amount/price, price*(1 + profitPercent));
            deposit -= amount;

            double quantity = amount/price;

            TOrder order = new TOrder();
            order.setPriceBuy(price);
            order.setAmountBuy(amount);
            order.setPriceSell(price*(1 + profitPercent));
            order.setQuantity(quantity);
            orderList.add(order);
        }
    }


    private void sell(TOrder order){
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
        profit = 0.0;
        tradeCount = 0;
    }


//    @Scheduled(fixedRate = 500L)
    public void start(){
        if (name == null || profitPercent == null ||
                dropPercent == null || sleepTime == null ||
                amount == null || deposit == null){
            initBot();
        }
        Double price = mexcService.lastPrice("KASUSDT");
        if (price != null){
            work(price);
        }
    }

//    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Moscow")
    public void saveData(){
        System.out.println("save_data");
        LocalDateTime date = LocalDateTime.now();
        BotStat botStat = new BotStat();
        botStat.setDate(date);
        botStat.setName(name);
        botStat.setProfit(profit);
        botStat.setTrades(tradeCount);
        botStat.setSizeorderlist(orderList.size());
        botStat.setDeposit(deposit);
        botsStatRepository.save(botStat);
        resetInfo();
    }
}
