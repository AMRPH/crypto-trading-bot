package shdlv.trading.bot.entity;

import lombok.Data;

@Data
public class Order {

    String symbol;

    String orderId;

    Integer orderListId;

    String clientOrderId;

    String price;

    String origQty;

    String executedQty;

    String cummulativeQuoteQty;

    String status;

    String timeInForce;

    String type;

    String side;

    String stopPrice;

    String icebergQty;

    Long time;

    Long updateTime;

    Boolean isWorking;

    String origQuoteOrderQty;
}
