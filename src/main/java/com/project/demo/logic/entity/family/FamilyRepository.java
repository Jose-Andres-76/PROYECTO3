package com.project.demo.logic.entity.family;

import com.project.demo.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyRepository extends JpaRepository<Family, Long>{
    Optional<Family> findById(long id);

    @Query ("SELECT f FROM Family f WHERE f.idFather = ?1")
    List<Family> findByFatherId(User father);

    @Query ("SELECT f FROM Family f WHERE f.idSon = ?1")
    List<Family> findBySonId(User son);
}