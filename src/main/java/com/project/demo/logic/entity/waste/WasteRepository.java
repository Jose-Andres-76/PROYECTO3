package com.project.demo.logic.entity.waste;

import com.project.demo.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WasteRepository extends JpaRepository<Waste, Long> {

    List<Waste> findByUserId(User user);

    List<Waste> findByProductType(String productType);

    List<Waste> findByUserIdAndProductType(User user, String productType);

    @Query("SELECT w FROM Waste w WHERE w.userId = :user ORDER BY w.createdAt DESC")
    List<Waste> findRecentByUser(@Param("user") User user);

    long countByUserId(User user);

    long countByProductType(String productType);
}