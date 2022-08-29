package com.example.oas.controller.dto.assembler;

import com.example.oas.controller.HomeController;
import com.example.oas.controller.dto.HomeDto;
import com.google.common.base.Preconditions;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HomeDtoAssembler implements RepresentationModelAssembler<HomeDto, EntityModel<HomeDto>> {
  @Override
  public EntityModel<HomeDto> toModel(HomeDto dto) {
    Preconditions.checkNotNull(dto);

    Link selfLink = linkTo(methodOn(HomeController.class).get(dto.getId())).withSelfRel();

    return EntityModel.of(
        dto,
        selfLink
    );
  }
}
