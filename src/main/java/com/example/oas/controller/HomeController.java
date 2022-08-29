package com.example.oas.controller;

import com.example.oas.controller.dto.HomeDto;
import com.example.oas.service.HomeService;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {
  private final HomeService homeService;

  private final RepresentationModelAssembler<HomeDto, EntityModel<HomeDto>> representationModelAssembler;

  private final PagedResourcesAssembler<HomeDto> pagedResourcesAssembler;

  @GetMapping
  public ResponseEntity<PagedModel<EntityModel<HomeDto>>> hello(@PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(pagedModelOf(homeService.findAll(pageable)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<HomeDto>> get(@PathVariable String id) {
    return ResponseEntity.ok(entityModelOf(homeService.findById(id), id));
  }

  @PostMapping
  public ResponseEntity<EntityModel<HomeDto>> post(@RequestBody HomeDto homeDto) {
    HomeDto savedHomeDto = homeService.save(homeDto.getId(), homeDto.getContent());
    return ResponseEntity.ok(entityModelOf(savedHomeDto, savedHomeDto.getId()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<EntityModel<HomeDto>> put(@PathVariable String id, @RequestParam String content) {
    Preconditions.checkNotNull(content);

    return ResponseEntity.ok(entityModelOf(homeService.saveOrModify(id, content), id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<EntityModel<HomeDto>> delete(@PathVariable String id) {
    return ResponseEntity.ok(entityModelOf(homeService.delete(id), id));
  }

  private WebMvcLinkBuilder selfLinkBuilder(String id) {
    return linkTo(methodOn(this.getClass()).get(id));
  }

  private EntityModel<HomeDto> entityModelOf(HomeDto homeDto, String id) {
    return EntityModel.of(homeDto, selfLinkBuilder(id).withSelfRel());
  }

  private PagedModel<EntityModel<HomeDto>> pagedModelOf(Page<HomeDto> page) {
    return pagedResourcesAssembler.toModel(page, representationModelAssembler);
  }
}
