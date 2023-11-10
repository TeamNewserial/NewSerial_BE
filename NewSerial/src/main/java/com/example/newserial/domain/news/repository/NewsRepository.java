package com.example.newserial.domain.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
}
