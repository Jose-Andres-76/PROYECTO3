package com.project.demo.logic.entity.family;

import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;

@Table(name = "family")
@Entity
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_son_", referencedColumnName = "id", nullable = false)
    private User idSon;

    @ManyToOne
    @JoinColumn(name = "id_father_", referencedColumnName = "id", nullable = false)
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

    public User getIdSon() {
        return idSon;
    }

    public void setIdSon(User idSon) {
        this.idSon = idSon;
    }

    public User getIdFather() {
        return idFather;
    }

    public void setIdFather(User idFather) {
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
