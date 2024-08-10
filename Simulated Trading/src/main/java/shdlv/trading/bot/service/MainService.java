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
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class MainService implements ApplicationRunner {

    @Autowired
    private BotsStatRepository botsStatRepository;

    @Value("classpath:/btc_history_prepared.csv")
    private Resource btc;

    @Value("classpath:/kaspa_history.csv")
    private Resource kas;

    boolean btcOrKas = false;

    List<Bot> bots;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        botsStatRepository.deleteAll();
        initBots();
        if (btcOrKas){
            try {
                Random random = new Random();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(btc.getInputStream()));
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
                    if (date.contains("23:59:00")){
                        System.out.println(date);
                        saveData(date);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                Boolean flag = false;
                Random random = new Random();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(kas.getInputStream()));
                br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] row = line.split(",");

                    Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
                    String date = format.format(new Date(Long.parseLong(row[1])));
                    if (date.equals("2023 08 03 00:00:00")){
                        flag = true;
                    }

                    if (flag){
                        double open = Double.parseDouble(row[2]);
                        double high = Double.parseDouble(row[3]);
                        double low = Double.parseDouble(row[4]);
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

                        if (date.contains("01 00:00:00")){
                            updateBots();
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void work(double price) throws InterruptedException {
        for (Bot bot : bots) {
            bot.work(price);
        }
    }

    public void saveData(String date){
        for (Bot bot : bots) {
            BotStat botStat = new BotStat();
            botStat.setDate(date);
            botStat.setName(bot.name);
            botStat.setProfit(bot.profit);
            botStat.setTrades(bot.tradeCount);
            botStat.setSizeorderlist(bot.maxsizeorderslist);
            botStat.setDeposit(bot.deposit);
            botsStatRepository.save(botStat);

            bot.resetInfo();
        }
    }

    public void updateBots(){
        for (Bot bot : bots) {
            bot.updateAmount();
        }
    }

    private void initBots(){
        bots = new ArrayList<>();
        List<Double> profitList = List.of(0.003, 0.005, 0.01, 0.02, 0.03);
        List<Double> dropList = List.of(0.001, 0.003, 0.005, 0.01, 0.02, 0.03);
        List<Integer> partList = List.of(10, 20, 30, 40, 50, 60, 70, 80, 90, 100);
        for (Double profit : profitList){
            for (Double drop : dropList){
                for (Integer part : partList){
                    bots.add(new Bot(profit, drop, part));
                }
            }
        }
    }
}
