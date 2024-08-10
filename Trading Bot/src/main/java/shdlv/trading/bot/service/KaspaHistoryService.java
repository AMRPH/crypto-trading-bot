package shdlv.trading.bot.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shdlv.trading.bot.entity.Kline;
import shdlv.trading.bot.repository.KaspaHistoryRepository;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

@Service
public class KaspaHistoryService implements ApplicationRunner {

    @Autowired
    private MEXCService mexcService;

    @Autowired
    private KaspaHistoryRepository kaspaHistoryRepository;

    private Long getStartTime(){
        Long time = kaspaHistoryRepository.getStartTime();
//        Long time = 1679097600000L;
        return time - 3600000L*24;
    }

    @Override
    public void run(ApplicationArguments args){
        Long startTime = getStartTime();
        Long endTime = (new Timestamp(System.currentTimeMillis())).getTime();
        Long peroid = 3600000L*6L;
        for (Long time = startTime; time < endTime; time += peroid){
            Kline[] klines = mexcService.kline("KASUSDT", time, time + peroid);
            if (klines != null){
                kaspaHistoryRepository.saveAll(Arrays.asList(klines));
            }
            System.out.println(new Date(time));
        }
    }
}
