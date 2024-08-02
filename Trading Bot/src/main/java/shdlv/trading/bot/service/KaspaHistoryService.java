package shdlv.trading.bot.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shdlv.trading.bot.entity.Kline;
import shdlv.trading.bot.repository.KaspaHistoryRepository;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

@Service
public class KaspaHistoryService {

    @Autowired
    private MEXCService mexcService;

    @Autowired
    private KaspaHistoryRepository kaspaHistoryRepository;

//    @PostConstruct
    public void start(){
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

    private Long getStartTime(){
        Long time = kaspaHistoryRepository.getStartTime();
        return time - 3600000L*24;
    }
}
