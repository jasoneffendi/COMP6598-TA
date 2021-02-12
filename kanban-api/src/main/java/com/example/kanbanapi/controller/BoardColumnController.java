package com.example.kanbanapi.controller;

import com.example.kanbanapi.exception.ResourceNotFoundException;
import com.example.kanbanapi.model.BoardColumn;
import com.example.kanbanapi.model.BoardTask;
import com.example.kanbanapi.repository.BoardColumnRepository;
import com.example.kanbanapi.repository.BoardTaskRepository;
import com.example.kanbanapi.request.BoardColumnPosRequest;
import com.example.kanbanapi.request.BoardColumnResponse;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class BoardColumnController {

    @Autowired
    private BoardColumnRepository boardColumnRepository;

    @Autowired
    private BoardTaskRepository boardTaskRepository;

//    @GetMapping("/boardColumn")
//    public Page<BoardColumn> getAllBoardColumns(Pageable pageable) {
//        return boardColumnRepository.findAll((pageable));
//    }

    @GetMapping("/boardColumn")
    public List<BoardColumnResponse> getAllBoardColumns() {
        List<BoardColumnResponse> responseList =  new ArrayList<>();
        boardColumnRepository.findAllByOrderByDisplayOrderAsc().forEach(boardColumn -> {
            List<BoardTask> boardTaskList = boardTaskRepository.findByBoardColumnId(boardColumn.getId());
            BoardColumnResponse boardColumnResponse = new BoardColumnResponse();
            boardColumnResponse.setId(boardColumn.getId());
            boardColumnResponse.setTitle(boardColumn.getTitle());
            boardColumnResponse.setDisplayOrder(boardColumn.getDisplayOrder());
            boardColumnResponse.setCards(boardTaskList);
            responseList.add(boardColumnResponse);
        });
        return responseList;
    }

    @PostMapping("/boardColumn")
    public BoardColumn createBoardColumn(@Valid @RequestBody BoardColumn boardColumn) {
        return boardColumnRepository.save(boardColumn);
    }

    @PostMapping("/boardColumnPos")
    public ResponseEntity<?> updateBoardColumn(@Valid @RequestBody BoardColumnPosRequest boardColumnPosRequest) {
        Long fromColumnId = boardColumnPosRequest.getFromId();
        if(fromColumnId != null && !boardColumnRepository.existsById(fromColumnId)) {
            throw new ResourceNotFoundException("Target From BoardColumn " + fromColumnId + " not found");
        }

        Long toColumnId = boardColumnPosRequest.getToId();
        if(toColumnId != null && !boardColumnRepository.existsById(toColumnId)) {
            throw new ResourceNotFoundException("Target To BoardColumn " + toColumnId + " not found");
        }

        BoardColumn fromBoardColumn = boardColumnRepository.getOne(fromColumnId);
        Integer fromBoardColumnDisplayOrder = fromBoardColumn.getDisplayOrder();
        BoardColumn toBoardColumn = boardColumnRepository.getOne(toColumnId);
        Integer toBoardColumnDisplayOrder = toBoardColumn.getDisplayOrder();

        fromBoardColumn.setDisplayOrder(toBoardColumnDisplayOrder);
        toBoardColumn.setDisplayOrder(fromBoardColumnDisplayOrder);
        boardColumnRepository.save(fromBoardColumn);
        boardColumnRepository.save(toBoardColumn);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/boardColumn/{boardColumnId}")
    public BoardColumn updateBoardColumn(@PathVariable Long boardColumnId, @Valid @RequestBody BoardColumn postRequest) {
        return boardColumnRepository.findById(boardColumnId).map(boardColumn -> {
            boardColumn.setTitle(postRequest.getTitle());
            return boardColumnRepository.save(boardColumn);
        }).orElseThrow(() -> new ResourceNotFoundException("BoardColumn " + boardColumnId + " not found"));
    }

    @DeleteMapping("/boardColumn/{boardColumnId}")
    public ResponseEntity<?> deleteBoardColumn(@PathVariable Long boardColumnId) {
        return boardColumnRepository.findById(boardColumnId).map(boardColumn -> {
            boardColumnRepository.delete(boardColumn);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("BoardColumn " + boardColumnId + " not found"));
    }
}
