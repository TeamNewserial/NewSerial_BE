package com.example.newserial.domain.view.repository;

import com.example.newserial.domain.news.repository.News;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor
@Entity
public class View {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewId;

    @MapsId //268p 일대일 식별 관계
    @OneToOne
    @JoinColumn(name="news_id")
    private News news;

    private Long count;

    @Builder
    public View(News news, Long count) {
        this.news = news;
        this.count = count;
    }
}
