package com.example.oas.service;

import com.example.oas.controller.dto.HomeDto;
import com.example.oas.entity.HomeEntity;
import com.example.oas.exception.NotFoundException;
import com.example.oas.repository.HomeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HomeService {
  private final HomeRepository repository;

  @Transactional(readOnly = true)
  public HomeDto findById(String id) {
    return repository.findById(id).map(HomeDto::of).orElseThrow(NotFoundException::new);
  }

  @Transactional
  public HomeDto save(String id, String content) {
    return HomeDto.of(repository.save(HomeEntity.builder().id(id).content(content).build()));
  }

  @SuppressWarnings("unused")
  @Transactional
  public HomeDto save(String content) {
    return HomeDto.of(repository.save(HomeEntity.builder().content(content).build()));
  }

  @Transactional
  public HomeDto delete(String id) {
    return HomeDto.of(repository.deleteById(id));
  }

  @Transactional(readOnly = true)
  public Page<HomeDto> findAll(Pageable pageable) {
    return new PageImpl<>(repository.findAll(pageable).stream().map(HomeDto::of).toList(), pageable, repository.count());
  }

  @SuppressWarnings("unused")
  @Transactional
  public HomeDto modify(String id, String content) {
    HomeEntity homeEntity = repository.findById(id).orElseThrow(IllegalArgumentException::new);
    homeEntity.setContent(content);

    log.info(homeEntity.toString());
    return HomeDto.of(homeEntity);
  }


  @Transactional
  public HomeDto saveOrModify(String id, String content) {
    return HomeDto.of(repository.save(HomeEntity.builder().id(id).content(content).build()));
  }
}
