<<<<<<< HEAD:NewSerial/src/main/java/com/example/newserial/domain/news/Briefing.java
package com.example.newserial.domain.news;
=======
package com.example.newserial.domain.news.repository;
>>>>>>> 51772be23a5a9daf7bfd060e07f0519e3cf6ff25:NewSerial/src/main/java/com/example/newserial/domain/news/repository/Briefing.java

import com.example.newserial.domain.BaseTimeEntity;
import com.example.newserial.domain.category.repository.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Briefing extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
