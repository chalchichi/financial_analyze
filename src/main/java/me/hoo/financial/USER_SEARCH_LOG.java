package me.hoo.financial;

import lombok.Getter;
import lombok.Setter;
import me.hoo.financial.Authentication.User;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class USER_SEARCH_LOG {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @CreationTimestamp // 생성 시간 자동 입력
    @Column(updatable = false) // Entity 업데이트 시 해당 컬럼 제외 여부
    private LocalDateTime SearchTime;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date StartDate;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date EndDate;

    @ManyToOne(fetch =FetchType.EAGER)
    @JoinColumn(name = "TICKER")
    private TICKERS_MAS TICKER;

    @ManyToOne(fetch =FetchType.EAGER)
    @JoinColumn(name = "UserID")
    private User UserID;

    private Integer add_days;

    private Integer limitcount;
}
