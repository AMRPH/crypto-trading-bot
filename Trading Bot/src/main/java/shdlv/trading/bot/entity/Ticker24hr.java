package shdlv.trading.bot.entity;

import lombok.Data;

@Data
public class Ticker24hr {
    String symbol;

    String priceChange;

    String priceChangePercent;

    String prevClosePrice;

    String lastPrice;

    String bidPrice;

    String bidQty;

    String askPrice;

    String askQty;

    String openPrice;

    String highPrice;

    String lowPrice;

    String volume;

    String quoteVolume;

    long openTime;

    long closeTime;

    String count;
}
