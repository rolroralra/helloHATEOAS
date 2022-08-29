package com.example.oas.repository;

import com.example.oas.entity.HomeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

@Repository
@Slf4j
public class HomeRepository implements CustomRepository<HomeEntity, String> {
  private final Map<String, HomeEntity> map = new HashMap<>();

  @PostConstruct
  void postConstruct() {
    IntStream.range(0, 10)
        .mapToObj(i -> generateEntity())
        .forEach(this::save);
  }

  @Override
  public HomeEntity save(HomeEntity homeEntity) {
    if (homeEntity.getId() == null) {
      homeEntity.setId(generateId());
    }

    map.put(homeEntity.getId() , homeEntity);
    return homeEntity;
  }

  @Override
  public Optional<HomeEntity> findById(String id) {
    return Optional.ofNullable(map.getOrDefault(id,  null));
  }

  @Override
  public HomeEntity deleteById(String id) {
    return map.remove(id);
  }

  @Override
  public long count() {
    return map.size();
  }

  private String generateId() {
    String id;
    do {
      id = UUID.randomUUID().toString();

    } while (map.containsKey(id));

    return id;
  }

  private HomeEntity generateEntity() {
    return HomeEntity.builder().id(generateId()).content(LocalDateTime.now().toString()).build();
  }

  public Page<HomeEntity> findAll(Pageable pageable) {
    Page<HomeEntity> page = new PageImpl<>(map.values().stream().skip(pageable.getOffset()).limit(pageable.getPageSize()).toList(), pageable, count());


    log.info(pageable.toString());
    log.info(page.toString());

    return page;
  }

  @Override
  public List<HomeEntity> findAll() {
    return map.values().stream().toList();
  }

}
