package com.example.oas.repository.common

import com.example.oas.repository.CustomRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.util.stream.IntStream
import java.util.stream.Stream

import static org.assertj.core.api.Assertions.assertThat

@DataJpaTest
@Rollback
@ActiveProfiles(value = "test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
abstract class CustomRepositoryTest<T, ID> extends Specification {
    private static final int INIT_ENTITY_TOTAL_COUNT = 50
    private static final int DEFAULT_PAGEABLE_SIZE = 20

    protected abstract CustomRepository<T, ID> repository();
    protected abstract T createTestInstance();

    protected abstract ID idFromEntity(T entity);

    static Stream<Arguments> providePageableArguments() {
        return IntStream.rangeClosed(0, INIT_ENTITY_TOTAL_COUNT / DEFAULT_PAGEABLE_SIZE + 1 as int)
                .mapToObj(page -> Arguments.of(page, DEFAULT_PAGEABLE_SIZE))
    }

    @BeforeEach
    void beforeEach() {
        IntStream.rangeClosed(1, INIT_ENTITY_TOTAL_COUNT)
                .mapToObj(i -> createTestInstance())
                .forEach(repository()::save)
    }

    @Test
    void findById() {
        // given
        T entity = createTestInstance()
        T savedEntity = repository().save(entity)

        // when
        Optional<T> findById = repository().findById(idFromEntity(savedEntity))

        // then
        assertThat(findById)
                .isNotEmpty()
                .get()
                .isEqualTo(savedEntity)
    }

    @ParameterizedTest
    @MethodSource(value = "providePageableArguments")
    void findAll(int page, int size) {
        // given
        List<T> expectedEntityList = repository().findAll().stream()
                .skip(((long) page) * size)
                .limit(size)
                .toList() as List<T>

        PageRequest pageRequest = PageRequest.of(page, size)

        // when
        Page<T> result = repository().findAll(pageRequest) as Page<T>

        // then
        assertThat(result.getContent())
                .containsExactlyElementsOf(expectedEntityList)
    }

    @Test
    void save() {
        // given
        T entity = createTestInstance()

        // when
        T savedEntity = repository().save(entity)

        // then
        assertThat(savedEntity)
                .isNotNull()
    }

    @Test
    void delete() {
        // given
        T entity = createTestInstance()
        T savedEntity = repository().save(entity)

        // when
        repository().deleteById(idFromEntity(savedEntity))
        Optional<T> optionalUser = repository().findById(idFromEntity(savedEntity))

        // then
        assertThat(optionalUser).isEmpty()
    }
}
