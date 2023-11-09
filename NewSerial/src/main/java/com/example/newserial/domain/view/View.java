package com.example.newserial.domain.view;

import com.example.newserial.domain.news.News;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class View {

    @Id
    private Long viewId;

    @MapsId //268p 일대일 식별 관계
    @OneToOne
    @JoinColumn(name="news_id")
    private News news;

    private Long count;

}
