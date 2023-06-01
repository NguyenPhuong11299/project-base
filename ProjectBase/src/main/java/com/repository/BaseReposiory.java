package com.repository;

import com.model.entity.Base;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseReposiory extends JpaRepository<Base, String> {
    List<Base> findByState(String state);
}
