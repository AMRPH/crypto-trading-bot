package shdlv.trading.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shdlv.trading.bot.entity.Kline;
import shdlv.trading.bot.entity.Order;
import shdlv.trading.bot.repository.KaspaHistoryRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class KaspaHistoryService {

    @Autowired
    private MEXCService mexcService;

    @Autowired
    private KaspaHistoryRepository kaspaHistoryRepository;

    @Scheduled(fixedRate = 7200000L)
    public void start(){
        Kline[] klines = mexcService.kline("KASUSDT");
        kaspaHistoryRepository.saveAll(Arrays.asList(klines));
    }
}
