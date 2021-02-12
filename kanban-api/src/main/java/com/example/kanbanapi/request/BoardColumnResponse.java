package com.example.kanbanapi.request;

import com.example.kanbanapi.model.BoardTask;

import java.util.List;

public class BoardColumnResponse {
    private Long id;

    private String title;

    private Integer displayOrder;

    private List<BoardTask> cards;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }


    public List<BoardTask> getCards() {
        return cards;
    }

    public void setCards(List<BoardTask> boardTask) {
        this.cards = boardTask;
    }
}
