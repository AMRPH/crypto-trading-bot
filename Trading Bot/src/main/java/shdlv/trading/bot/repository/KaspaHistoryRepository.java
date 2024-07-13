package shdlv.trading.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import shdlv.trading.bot.entity.Kline;

@Repository
public interface KaspaHistoryRepository extends JpaRepository<Kline, Long>{
}
