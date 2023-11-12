package com.example.newserial.domain.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    @Query("SELECT n FROM News n WHERE DATE(n.date) = DATE(:targetDate) ORDER BY n.date DESC")
    List<News> findAllByDate(@Param("targetDate") Timestamp targetDate);
}
