package com.example.newserial.domain.memo;

import com.example.newserial.domain.news.News;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    List<Memo> findByNews(News news); //pageable 옵션..
}

