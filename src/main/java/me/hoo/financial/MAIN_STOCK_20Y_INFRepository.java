package me.hoo.financial;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MAIN_STOCK_20Y_INFRepository extends JpaRepository<MAIN_STOCK_20Y_INF,Long> {


    List<MAIN_STOCK_20Y_INF> findMAIN_STOCK_20Y_INFByTICKER(TICKERS_MAS ticker, Sort tday);

    @Query("select u from MAIN_STOCK_20Y_INF u where u.TDAY BETWEEN :start and :end" +
            " and u.TICKER = :ticker")
    List<MAIN_STOCK_20Y_INF> findMAIN_STOCK_20Y_INFByTDAYBetweenAndTICKEREquals(Date start , Date end , TICKERS_MAS ticker);


}
