package com.example.oas.repository

import com.example.oas.entity.HomeEntity
import com.example.oas.repository.common.CustomRepositoryTest

import java.time.LocalDateTime

class HomeRepositoryTest extends CustomRepositoryTest<HomeEntity, String> {
    private HomeRepository homeRepository

    @Override
    protected CustomRepository<HomeEntity, String> repository() {
        if (homeRepository == null) {
            homeRepository = new HomeRepository()
        }

        return homeRepository
    }

    @Override
    protected HomeEntity createTestInstance() {
        return HomeEntity.builder()
                .id(UUID.randomUUID().toString())
                .content(LocalDateTime.now().toString())
                .build()
    }

    @Override
    protected String idFromEntity(HomeEntity entity) {
        return entity.getId()
    }
}
