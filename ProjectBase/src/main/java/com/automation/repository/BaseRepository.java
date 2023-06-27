package com.automation.repository;

import com.automation.model.entity.Base;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaseRepository extends JpaRepository<Base, String> {
    List<Base> findByState(String state);

    Optional<Base> findByNameAndState(String name, String state);
}
