package com.example.kanbanapi.repository;

import com.example.kanbanapi.model.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
    public List<BoardColumn> findAllByOrderByDisplayOrderAsc();
}
