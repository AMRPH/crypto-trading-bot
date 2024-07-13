package shdlv.trading.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shdlv.trading.bot.entity.BotsStat;
import shdlv.trading.bot.repository.BotsStatRepository;
import shdlv.trading.bot.repository.KaspaHistoryRepository;

import java.time.LocalDateTime;

@Service
public class MainService {

    @Autowired
    private MEXCService mexcService;

    @Autowired
    private BotsStatRepository botsStatRepository;

    @Autowired
    private Service01_02 service01_02;

    @Autowired
    private Service01_015 service01_015;

    @Autowired
    private Service003_01 service003_01;

    @Autowired
    private Service003_02 service003_02;

    @Autowired
    private Service003_005 service003_005 ;

    @Autowired
    private Service003_015 service003_015 ;

    @Autowired
    private Service005_01 service005_01 ;

    @Autowired
    private Service005_02 service005_02;

    @Autowired
    private Service005_005 service005_005;

    @Autowired
    private Service005_015 service005_015;

    @Autowired
    private Service007_01 service007_01;

    @Autowired
    private Service007_02 service007_02;

    @Autowired
    private Service007_005 service007_005;

    @Autowired
    private Service007_015 service007_015;

    @Autowired
    private Service009_01 service009_01;

    @Autowired
    private Service009_02 service009_02;

    @Autowired
    private Service009_005 service009_005;

    @Autowired
    private Service009_015 service009_015;

    @Async
    @Scheduled(fixedRate = 1000L)
    public void start() throws InterruptedException {
        double price = mexcService.lastPrice("KASUSDT");
        service01_02.start(price);
        service01_015.start(price);
        service003_01.start(price);
        service003_02.start(price);
        service003_005.start(price);
        service003_015.start(price);
        service005_01.start(price);
        service005_02.start(price);
        service005_005.start(price);
        service005_015.start(price);
        service007_01.start(price);
        service007_02.start(price);
        service007_005.start(price);
        service007_015.start(price);
        service009_01.start(price);
        service009_02.start(price);
        service009_005.start(price);
        service009_015.start(price);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkProfit(){
        LocalDateTime date = LocalDateTime.now();
        BotsStat botsStat = new BotsStat();
        botsStat.setDate(date);
        botsStat.setProfit_01_02(service01_02.profit);
        botsStat.setTrades_01_02(service01_02.tradeCount);

        botsStat.setProfit_01_015(service01_015.profit);
        botsStat.setTrades_01_015(service01_015.tradeCount);

        botsStat.setProfit_003_01(service003_01.profit);
        botsStat.setTrades_003_01(service003_01.tradeCount);

        botsStat.setProfit_003_02(service003_02.profit);
        botsStat.setTrades_003_02(service003_02.tradeCount);

        botsStat.setProfit_003_005(service003_005.profit);
        botsStat.setTrades_003_005(service003_005.tradeCount);

        botsStat.setProfit_003_015(service003_015.profit);
        botsStat.setTrades_003_015(service003_015.tradeCount);

        botsStat.setProfit_005_01(service005_01.profit);
        botsStat.setTrades_005_01(service005_01.tradeCount);

        botsStat.setProfit_005_02(service005_02.profit);
        botsStat.setTrades_005_02(service005_02.tradeCount);

        botsStat.setProfit_005_005(service005_005.profit);
        botsStat.setTrades_005_005(service005_005.tradeCount);

        botsStat.setProfit_005_015(service005_015.profit);
        botsStat.setTrades_005_015(service005_015.tradeCount);

        botsStat.setProfit_007_01(service007_01.profit);
        botsStat.setTrades_007_01(service007_01.tradeCount);

        botsStat.setProfit_007_02(service007_02.profit);
        botsStat.setTrades_007_02(service007_02.tradeCount);

        botsStat.setProfit_007_005(service007_005.profit);
        botsStat.setTrades_007_005(service007_005.tradeCount);

        botsStat.setProfit_007_015(service007_015.profit);
        botsStat.setTrades_007_015(service007_015.tradeCount);

        botsStat.setProfit_009_01(service009_01.profit);
        botsStat.setTrades_009_01(service009_01.tradeCount);

        botsStat.setProfit_009_02(service009_02.profit);
        botsStat.setTrades_009_02(service009_02.tradeCount);

        botsStat.setProfit_009_005(service009_005.profit);
        botsStat.setTrades_009_005(service009_005.tradeCount);

        botsStat.setProfit_009_015(service009_015.profit);
        botsStat.setTrades_009_015(service009_015.tradeCount);
        botsStatRepository.save(botsStat);

        resetBots();
    }

    private void resetBots(){
        service01_02.reset();
        service01_015.reset();
        service003_01.reset();
        service003_02.reset();
        service003_005.reset();
        service003_015.reset();
        service005_01.reset();
        service005_02.reset();
        service005_005.reset();
        service005_015.reset();
        service007_01.reset();
        service007_02.reset();
        service007_005.reset();
        service007_015.reset();
        service009_01.reset();
        service009_02.reset();
        service009_005.reset();
        service009_015.reset();
    }

}
