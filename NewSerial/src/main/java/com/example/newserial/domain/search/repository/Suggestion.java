package com.example.newserial.domain.search.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Suggestion {

    public Suggestion(String query) {
        this.query = query;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long suggestionId;

    @Column(length = 20)
    private String query;

}
