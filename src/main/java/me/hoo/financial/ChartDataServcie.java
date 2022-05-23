package me.hoo.financial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ChartDataServcie {
    @Autowired
    MAIN_STOCK_20Y_INFRepository main_stock_20Y_infRepository;

    @Autowired
    TICKERS_MASRepository tickers_masRepository;
    public List<MAIN_STOCK_20Y_INF> gettargetdata(String ticker, List<Map<String, Date>> targetdatelist)
    {
        TICKERS_MAS tickers_mas = tickers_masRepository.findTICKERS_MASByCName(ticker).get();

        List<MAIN_STOCK_20Y_INF> totaltargetlist = new ArrayList<>();
        for(Map<String,Date> m : targetdatelist)
        {
            List<MAIN_STOCK_20Y_INF> tempdate =
                    main_stock_20Y_infRepository.findMAIN_STOCK_20Y_INFByTDAYBetweenAndTICKEREquals(m.get("from"),m.get("to"),tickers_mas);
            tempdate.forEach(x->totaltargetlist.add(x));
        }
        return totaltargetlist;
    }
}
