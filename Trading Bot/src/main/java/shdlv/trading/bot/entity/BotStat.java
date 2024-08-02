package shdlv.trading.bot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "bot_stats")
@Data
public class BotStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @JsonFormat(pattern="dd.MM.yyyy HH:mm:ss")
    private LocalDateTime date;

    String name;

    double profit;

    int trades;

    int sizeorderlist;

    double deposit;
}
