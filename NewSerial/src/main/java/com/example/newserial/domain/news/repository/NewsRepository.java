package com.example.newserial.domain.news.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(
            value = "SELECT n FROM News n WHERE n.title LIKE %:keyword% OR n.body LIKE %:keyword%",
            countQuery = "SELECT COUNT(n.id) FROM News n WHERE n.title LIKE %:keyword% OR n.body LIKE %:keyword%"
    )
    Page<News> findByTitleOrBodyContaining(@Param("keyword") String keyword, Pageable pageable);
}
