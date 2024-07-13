package shdlv.trading.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import shdlv.trading.bot.entity.BotsStat;
import shdlv.trading.bot.entity.Kline;

import java.time.LocalDateTime;

@Repository
public interface BotsStatRepository extends JpaRepository<BotsStat, LocalDateTime>{
}
