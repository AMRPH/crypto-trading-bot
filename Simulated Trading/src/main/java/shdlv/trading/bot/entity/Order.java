package shdlv.trading.bot.entity;

import lombok.Data;

@Data
public class Order {

    double priceBuy;

    double amountBuy;

    double priceSell;

    double quantity;
}
