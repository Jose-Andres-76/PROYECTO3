package com.project.demo.logic.entity.rol;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query ("SELECT r FROM Role r WHERE r.name = ?1")
    Optional<Role> findByName(RoleEnum name);

    @Query ("SELECT r FROM Role r WHERE r.id = ?1")
    Optional<Role> findById(Integer id);
}