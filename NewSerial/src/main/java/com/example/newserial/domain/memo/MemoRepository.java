package com.example.newserial.domain.memo;

import com.example.newserial.domain.memo.entity.Memo;
import com.example.newserial.domain.memo.entity.MemoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, MemoId> {

}

