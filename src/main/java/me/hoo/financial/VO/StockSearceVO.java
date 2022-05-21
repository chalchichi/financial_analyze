package me.hoo.financial.VO;

import me.hoo.financial.TICKERS_MAS;

import java.util.Date;

public class StockSearceVO {
    Date start;

    Date end;
    String ticker;

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

}
