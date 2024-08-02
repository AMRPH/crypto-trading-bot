package shdlv.trading.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shdlv.trading.bot.entity.BotStat;

@Repository
public interface BotStatRepository extends JpaRepository<BotStat, Long>{
}
