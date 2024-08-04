package shdlv.trading.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shdlv.trading.bot.entity.Kline;

import java.util.List;

@Repository
public interface KaspaHistoryRepository extends JpaRepository<Kline, Long>{

    @Query("SELECT k.timeClose FROM Kline k ORDER BY k.timeClose DESC LIMIT 1")
    Long getStartTime();
}
