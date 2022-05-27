package me.hoo.financial;


import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
public class MAIN_STOCK_20Y_INF {

    @Id
    private Integer ID;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date TDAY;


    private double Close;
    private double High;
    private double Open;
    private double Low;
    private long Volume;

    @ManyToOne(fetch =FetchType.EAGER)
    @JoinColumn(name = "TICKER")
    private TICKERS_MAS TICKER;
}
