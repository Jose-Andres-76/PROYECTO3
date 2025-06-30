package com.project.demo.logic.entity.game;

import jakarta.persistence.*;

@Table(name = "game")
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GameEnum typesOfGames;
    private String description;

    public Game() {}

    public Game(Long id, GameEnum typesOfGames) {
        this.id = id;
        this.typesOfGames = typesOfGames;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public GameEnum getTypesOfGames() {
        return typesOfGames;
    }
    public void setTypesOfGames(GameEnum typesOfGames) {
        this.typesOfGames = typesOfGames;
    }
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", typesOfGames=" + typesOfGames +
                ", description='" + description + '\'' +
                '}';
    }

}
