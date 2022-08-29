## HATEOAS Tutorial
[https://www.baeldung.com/spring-hateoas-tutorial](https://www.baeldung.com/spring-hateoas-tutorial)

---
## RepresentationModelAssembler
[https://chaibin0.tistory.com/entry/Spring-HATEOAS-%EC%A0%81%EC%9A%A9](https://chaibin0.tistory.com/entry/Spring-HATEOAS-%EC%A0%81%EC%9A%A9)

---
## Example
### RepresentationModelAssembler<T, D extends RepresentationModel<?>>
```java
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
```

### Controller
```java
@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {
  private final HomeService homeService;

  private final RepresentationModelAssembler<HomeDto, EntityModel<HomeDto>> representationModelAssembler;

  private final PagedResourcesAssembler<HomeDto> pagedResourcesAssembler;

  @GetMapping
  public ResponseEntity<PagedModel<EntityModel<HomeDto>>> searchAll(@PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(pagedModelOf(homeService.findAll(pageable)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<HomeDto>> searchById(@PathVariable String id) {
    return ResponseEntity.ok(entityModelOf(homeService.findById(id), id));
  }
  
  private WebMvcLinkBuilder selfLinkBuilder(String id) {
    return linkTo(methodOn(this.getClass()).searchById(id));
  }

  private EntityModel<HomeDto> entityModelOf(HomeDto homeDto, String id) {
    return EntityModel.of(homeDto, selfLinkBuilder(id).withSelfRel());
  }

  private PagedModel<EntityModel<HomeDto>> pagedModelOf(Page<HomeDto> page) {
    return pagedResourcesAssembler.toModel(page, representationModelAssembler);
  }
}
```