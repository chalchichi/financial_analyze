package me.hoo.financial;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class STOCK_INFRepositoryTest {

    @Autowired
    MAIN_STOCK_20Y_INFRepository stock_infRepository;

    @Autowired
    TICKERS_MASRepository tickers_masRepository;

    @Test
    public void findbyidTest()
    {
        LocalDate lstart = LocalDate.of(2020,03,03);
        LocalDate lend = LocalDate.now();

        Date end = java.sql.Date.valueOf(lend);
        Date start = java.sql.Date.valueOf(lstart);

/*
        List<STOCK_INF> stock_infs = stock_infRepository.findSTOCK_INFSByTDAYBetween(start,end);
        stock_infs.stream().map(
                s -> s.getTicker() + " " + s.getTDAY() + " " +s.getClose()
        ).forEach(System.out::println);
*/

//        Optional<MAIN_STOCK_20Y_INF> byId = stock_infRepository.findById(157468l);
//        TICKERS_MAS tickers = byId.get().getTICKER();
//        tickers.setCOMPANY_NAME("AAPL2");
//        MAIN_STOCK_20Y_INF newstock = stock_infRepository.save(byId.get());
//        newstock.getTDAY();

        Optional<TICKERS_MAS> res = tickers_masRepository.findTICKERS_MASByCName("AAPL");
        TICKERS_MAS ticker = res.get();

        List<MAIN_STOCK_20Y_INF> stocklist = stock_infRepository.findMAIN_STOCK_20Y_INFByTDAYBetweenAndTICKEREquals(start,end,ticker);
        stocklist.stream().map(
                s -> s.getTICKER().getCOMPANY_NAME() + " " + s.getTDAY() + " " +s.getClose()
        ).forEach(System.out::println);
    }

}