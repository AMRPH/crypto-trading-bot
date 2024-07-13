package shdlv.trading.bot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "bots_stats")
@Data
public class BotsStat {

    @Id
    @JsonFormat(pattern="dd.MM.yyyy HH:mm:ss")
    private LocalDateTime date;

    double profit_01_02;
    int trades_01_02;

    double profit_01_015;
    int trades_01_015;

    double profit_003_01;
    int trades_003_01;

    double profit_003_02;
    int trades_003_02;

    double profit_003_005;
    int trades_003_005;

    double profit_003_015;
    int trades_003_015;

    double profit_005_01;
    int trades_005_01;

    double profit_005_02;
    int trades_005_02;

    double profit_005_005;
    int trades_005_005;

    double profit_005_015;
    int trades_005_015;

    double profit_007_01;
    int trades_007_01;

    double profit_007_02;
    int trades_007_02;

    double profit_007_005;
    int trades_007_005;

    double profit_007_015;
    int trades_007_015;

    double profit_009_01;
    int trades_009_01;

    double profit_009_02;
    int trades_009_02;

    double profit_009_005;
    int trades_009_005;

    double profit_009_015;
    int trades_009_015;
}
