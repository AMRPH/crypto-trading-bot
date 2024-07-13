package shdlv.trading.bot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "kaspa_history")
@Data
public class Kline {

    @Id
    long timeOpen;

    double open;

    double high;

    double low;

    double close;

    double volume;

    long timeClose;

    double quoteVolume;
}
