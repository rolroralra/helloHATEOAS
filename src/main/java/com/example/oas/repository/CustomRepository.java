package com.example.oas.repository;

import com.example.oas.entity.HomeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomRepository<T, ID> {
  T save(T entity);

  Optional<T> findById(ID id);

  Page<HomeEntity> findAll(Pageable pageable);

  List<HomeEntity> findAll();

  T deleteById(ID id);

  long count();
}
