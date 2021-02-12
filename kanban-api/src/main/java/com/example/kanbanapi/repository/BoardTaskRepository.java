package com.example.kanbanapi.repository;

import com.example.kanbanapi.model.BoardTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardTaskRepository extends JpaRepository<BoardTask, Long> {
    List<BoardTask> findByBoardColumnId(Long boardColumnId);
    Optional<BoardTask> findByIdAndBoardColumnId(Long id, Long boardColumnId);
}
