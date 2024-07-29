package shdlv.trading.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shdlv.trading.bot.entity.BotStat;

import java.time.LocalDateTime;

@Repository
public interface BotsStatRepository extends JpaRepository<BotStat, LocalDateTime>{
}
