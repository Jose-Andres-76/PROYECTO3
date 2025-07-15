package com.project.demo.logic.entity.family;

import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;

@Table(name = "family")
@Entity
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_son_", referencedColumnName = "id")
    private User idSon;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_father_", referencedColumnName = "id")
    private User idFather;

    public Family() {
    }

    public Family(Long id, User idSon, User idFather) {
        this.id = id;
        this.idSon = idSon;
        this.idFather = idFather;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSon() {
        return idSon;
    }

    public void setSon(User idSon) {
        this.idSon = idSon;
    }

    public User getFather() {
        return idFather;
    }

    public void setFather(User idFather) {
        this.idFather = idFather;
    }

    @Override
    public String toString() {
        return "Family{" +
                "id=" + id +
                ", idSon=" + idSon +
                ", idFather=" + idFather +
                '}';
    }
}