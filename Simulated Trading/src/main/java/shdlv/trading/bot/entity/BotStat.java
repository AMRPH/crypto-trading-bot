package shdlv.trading.bot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "simulated_stats")
@Data
public class BotStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String date;

    String name;

    double profit;

    int trades;

    int sizeorderlist;

    double deposit;

}
