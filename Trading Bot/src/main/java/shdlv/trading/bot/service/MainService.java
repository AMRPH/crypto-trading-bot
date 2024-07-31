package shdlv.trading.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shdlv.trading.bot.entity.Bot;
import shdlv.trading.bot.entity.BotStat;
import shdlv.trading.bot.repository.BotsStatRepository;

import java.time.LocalDateTime;

@Service
public class MainService {

    @Autowired
    private MEXCService mexcService;

    @Autowired
    private BotsStatRepository botsStatRepository;

    Bot[] bots;


    @Async
    @Scheduled(fixedRate = 1000L)
    public void work(){
        if (bots == null){
            initBots();
        }
        double price = mexcService.lastPrice("KASUSDT");
        for (Bot bot : bots) {
            bot.work(price);
        }
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Moscow")
    public void saveData(){
        System.out.println("save_data");
        LocalDateTime date = LocalDateTime.now();
        for (Bot bot : bots) {
            BotStat botStat = new BotStat();
            botStat.setDate(date);
            botStat.setName(bot.name);
            botStat.setProfit(bot.profit);
            botStat.setTrades(bot.tradeCount);
            botStat.setSizeorderlist(bot.orderList.size());
            botStat.setDeposit(bot.deposit);
            botsStatRepository.save(botStat);

            bot.resetInfo();
        }
    }

    private void initBots(){
        bots = new Bot[]{
                new Bot(0.003, 0.005),
                new Bot(0.003, 0.01),
                new Bot(0.003, 0.02),
                new Bot(0.003, 0.02),
                new Bot(0.005, 0.005),
                new Bot(0.005, 0.01),
                new Bot(0.005, 0.02),
                new Bot(0.005, 0.03),
                new Bot(0.01, 0.005),
                new Bot(0.01, 0.01),
                new Bot(0.01, 0.02),
                new Bot(0.01, 0.03),
                new Bot(0.02, 0.005),
                new Bot(0.02, 0.01),
                new Bot(0.02, 0.02),
                new Bot(0.02, 0.03)};
    }
}
