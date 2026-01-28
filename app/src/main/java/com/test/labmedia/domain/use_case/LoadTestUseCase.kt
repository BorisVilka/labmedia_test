package com.test.labmedia.domain.use_case

import com.test.labmedia.domain.model.TestModel
import com.test.labmedia.domain.repository.Repository

class LoadTestUseCase(
    private val repository: Repository
) {

    suspend fun execute(): TestModel {
        return repository.loadTest()
    }
}