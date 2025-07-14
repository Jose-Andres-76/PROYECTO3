package com.project.demo.logic.entity.challenge;
import com.project.demo.logic.entity.family.Family;
import com.project.demo.logic.entity.game.Game;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Table(name = "challenge")
@Entity
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "family_id", referencedColumnName = "id", nullable = false)
    private Family familyId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id", referencedColumnName = "id", nullable = true)
    private Game gameId;

    @Column(nullable = false)
    private int points;

    @Column(nullable = false)
    private boolean challengeStatus;

    @Column(nullable = false)
    private String description;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private java.util.Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private java.util.Date updatedAt;

    public Challenge() {}

    public Challenge(long id, Family familyId, Game gameId, int points, boolean challengeStatus, String description) {
        this.id = id;
        this.familyId = familyId;
        this.gameId = gameId;
        this.points = points;
        this.challengeStatus = challengeStatus;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Family getFamily() {
        return familyId;
    }

    public void setFamily(Family familyId) {
        this.familyId = familyId;
    }

    public Game getGame() {
        return gameId;
    }

    public void setGame(Game gameId) {
        this.gameId = gameId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isChallengeStatus() {
        return challengeStatus;
    }

    public void setChallengeStatus(boolean challengeStatus) {
        this.challengeStatus = challengeStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "Challenge{" +
                "id=" + id +
                ", familyId=" + familyId +
                ", gameId=" + gameId +
                ", points=" + points +
                ", challengeStatus=" + challengeStatus +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}
