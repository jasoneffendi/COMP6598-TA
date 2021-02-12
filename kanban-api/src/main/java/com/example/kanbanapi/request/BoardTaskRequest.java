package com.example.kanbanapi.request;


import com.example.kanbanapi.model.BoardColumn;

public class BoardTaskRequest {

    private Long id;

    private String title;

    private String description;

    private Long boardColumnId;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getBoardColumnId() {
        return boardColumnId;
    }
}