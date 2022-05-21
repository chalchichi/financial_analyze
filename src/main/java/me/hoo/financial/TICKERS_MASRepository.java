package me.hoo.financial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Date;
import java.util.List;

public interface TICKERS_MASRepository extends JpaRepository<TICKERS_MAS,Long>{

    @Query("select u from TICKERS_MAS u where u.Name = :name")
    Optional<TICKERS_MAS> findTICKERS_MASByCName(String name);

    @Query("select u from TICKERS_MAS u where u.IS_MAIN_STOCK = :ismainstock")
    List<TICKERS_MAS> findTICKERS_MASByISMAINSTOCK(Boolean ismainstock);
}

