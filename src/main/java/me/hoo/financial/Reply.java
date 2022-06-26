package me.hoo.financial;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @CreationTimestamp // 생성 시간 자동 입력
    @Column(updatable = false) // Entity 업데이트 시 해당 컬럼 제외 여부
    private LocalDateTime Writetime;

    private String Writer;
    private String Commentreply;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TitleIndex")
    private Comment comment;
}
