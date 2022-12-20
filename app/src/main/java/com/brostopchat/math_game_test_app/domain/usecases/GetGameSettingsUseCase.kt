package com.brostopchat.math_game_test_app.domain.usecases

import com.brostopchat.math_game_test_app.domain.entity.GameSettings
import com.brostopchat.math_game_test_app.domain.entity.Level
import com.brostopchat.math_game_test_app.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}