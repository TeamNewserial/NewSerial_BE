package com.example.newserial.domain.category.repository;

import com.example.newserial.domain.news.repository.News;
import jakarta.persistence.*;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30)
    private String name;

    @OneToMany(mappedBy="category")
    private List<News> news;

    public Category(Integer id) {
        this.id = id;
    }
}
