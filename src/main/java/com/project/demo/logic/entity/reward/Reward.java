package com.project.demo.logic.entity.reward;

import com.project.demo.logic.entity.family.Family;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Table(name = "reward")
@Entity
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "family_id", nullable = false)
    private Family familyId;

    @Column(nullable = false)
    private int cost;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean status;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private java.util.Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private java.util.Date updatedAt;

    public Reward() {
    }

    public Reward(Long id, Family familyId, int cost, String description, boolean status) {
        this.id = id;
        this.familyId = familyId;
        this.cost = cost;
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Family getFamily() {
        return familyId;
    }
    public void setFamily(Family familyId) {
        this.familyId = familyId;
    }
    public int getCost() {
        return cost;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public java.util.Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }
    public java.util.Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(java.util.Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    @Override
    public String toString() {
        return "Reward{" +
                "id=" + id +
                ", family=" + familyId +
                ", cost=" + cost +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}
