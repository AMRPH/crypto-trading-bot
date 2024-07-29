package shdlv.trading.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shdlv.trading.bot.entity.Bot;
import shdlv.trading.bot.entity.BotStat;
import shdlv.trading.bot.repository.BotsStatRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class MainService implements ApplicationRunner {

    @Autowired
    private BotsStatRepository botsStatRepository;

    @Value("classpath:/btc_history_prepared.csv")
    private Resource resource;

    Bot[] bots;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        botsStatRepository.deleteAll();
        try {
            Random random = new Random();
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                String date = row[2];
                double open = Double.parseDouble(row[3]);
                double high = Double.parseDouble(row[4]);
                double low = Double.parseDouble(row[5]);
                work(open);
                if (random.nextBoolean()){
                    work(high);
                    work(low);
                } else {
                    work(low);
                    work(high);
                }
                if (date.contains("00:00:00")){
                    System.out.println(date);
                    saveData(date);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void work(double price) throws InterruptedException {
        if (bots == null){
            initBots();
        }
        for (int i = 0; i < bots.length; i ++) {
            Bot bot = bots[i];
            bot.work(price);
            bots[i] = bot;
        }
    }

    public void saveData(String date){;
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
