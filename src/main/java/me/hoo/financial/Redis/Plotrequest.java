package me.hoo.financial.Redis;

import lombok.Builder;
import lombok.ToString;

import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;


@Builder
@ToString
public class Plotrequest {
    String useremail;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date start;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date end;

    String ticker;
    String add_days;
    String limitcount;
    String chk_info;
}
