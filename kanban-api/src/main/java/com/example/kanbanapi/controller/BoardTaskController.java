package com.example.kanbanapi.controller;

import com.example.kanbanapi.exception.ResourceNotFoundException;
import com.example.kanbanapi.model.BoardColumn;
import com.example.kanbanapi.model.BoardTask;
import com.example.kanbanapi.repository.BoardTaskRepository;
import com.example.kanbanapi.repository.BoardColumnRepository;
import com.example.kanbanapi.request.BoardTaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class BoardTaskController {

    @Autowired
    private BoardTaskRepository boardTaskRepository;

    @Autowired
    private BoardColumnRepository boardColumnRepository;

    @GetMapping("/boardColumn/{boardColumnId}/boardTasks")
    public List<BoardTask> getAllBoardTaskById(@PathVariable (value = "boardColumnId") Long boardColumnId) {
        return boardTaskRepository.findByBoardColumnId(boardColumnId);
    }

    @PostMapping("/boardColumn/{boardColumnId}/boardTasks")
    public BoardTask createBoardTask(@PathVariable (value = "boardColumnId") Long boardColumnId,
                                 @Valid @RequestBody BoardTask boardTask) {
        return boardColumnRepository.findById(boardColumnId).map(boardColumn -> {
            boardTask.setBoardColumn(boardColumn);
            return boardTaskRepository.save(boardTask);
        }).orElseThrow(() -> new ResourceNotFoundException("BoardColumn " + boardColumnId + " not found"));
    }

    @PutMapping("/boardTasks/{boardTaskId}")
    public BoardTask updateComment(
            @PathVariable (value = "boardTaskId") Long boardTaskId,
                                 @Valid @RequestBody BoardTaskRequest boardTaskRequest) {

        Long targetBoardColumnId = boardTaskRequest.getBoardColumnId();
        if(targetBoardColumnId != null && !boardColumnRepository.existsById(targetBoardColumnId)) {
            throw new ResourceNotFoundException("Target BoardColumn " + targetBoardColumnId + " not found");
        }

        BoardColumn targetBoardColumn = boardColumnRepository.getOne(targetBoardColumnId);

        return boardTaskRepository.findById(boardTaskId).map(boardTask -> {
            boardTask.setTitle(boardTaskRequest.getTitle());
            boardTask.setDescription(boardTaskRequest.getDescription());
            boardTask.setBoardColumn(targetBoardColumn);

            return boardTaskRepository.save(boardTask);
        }).orElseThrow(() -> new ResourceNotFoundException("Board Task " + boardTaskId + "not found"));
    }

    @DeleteMapping("/boardTasks/{boardTaskId}")
    public ResponseEntity<?> deleteComment(@PathVariable (value = "boardTaskId") Long boardTaskId) {
        return boardTaskRepository.findById(boardTaskId).map(boardTask -> {
            boardTaskRepository.delete(boardTask);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + boardTaskId));
    }

}
