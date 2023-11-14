package com.example.newserial.domain.memo.repository;

import com.example.newserial.domain.memo.repository.Memo;
import com.example.newserial.domain.memo.repository.MemoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, MemoId> {

}

