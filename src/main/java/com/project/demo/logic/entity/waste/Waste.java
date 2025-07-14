package com.project.demo.logic.entity.waste;

import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

@Table(name = "waste")
@Entity
public class Waste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @Column(nullable = true, name = "product_type")
    private String productType;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private java.util.Date createdAt;

    private String answer;

    public Waste() {}

    public Waste(Long id, User userId, String productType, java.util.Date createdAt, String answer) {
        this.id = id;
        this.userId = userId;
        this.productType = productType;
        this.createdAt = createdAt;
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return userId;
    }

    public void setUser(User userId) {
        this.userId = userId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Waste{" +
                "id=" + id +
                ", userId=" + userId +
                ", productType='" + productType + '\'' +
                ", createdAt=" + createdAt +
                ", answer='" + answer + '\'' +
                '}';
    }
}