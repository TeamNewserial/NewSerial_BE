package com.example.newserial.domain.view.repository;

import com.example.newserial.domain.news.repository.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ViewRepository extends JpaRepository<View, Long> {

    Optional<View> findByNews(News news);

    @Modifying
    @Query("update View v set v.count=v.count+1 where v.news.id= :id")
    void updateViews(@Param("id") Long id);
}
