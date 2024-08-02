package shdlv.trading.bot.entity;

import lombok.Data;

@Data
public class TOrder {

    double priceBuy;

    double amountBuy;

    double priceSell;

    double quantity;
}
