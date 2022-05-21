package me.hoo.financial;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class TICKERS_MAS {
    @Id
    private String Name;

    private String COMPANY_NAME;

    private String INDUSTRY;

    private String INDUSTRY_CODE;

    private Boolean IS_MAIN_STOCK;
}
