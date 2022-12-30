package com.brostopchat.math_game_test_app.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brostopchat.math_game_test_app.R
import com.brostopchat.math_game_test_app.data.GameRepositoryImpl
import com.brostopchat.math_game_test_app.domain.entity.GameResult
import com.brostopchat.math_game_test_app.domain.entity.GameSettings
import com.brostopchat.math_game_test_app.domain.entity.Level
import com.brostopchat.math_game_test_app.domain.entity.Question
import com.brostopchat.math_game_test_app.domain.usecases.GenerateQuestionUseCase
import com.brostopchat.math_game_test_app.domain.usecases.GetGameSettingsUseCase

class GameViewModel(application: Application): AndroidViewModel(application) {

    private lateinit var level: Level
    private lateinit var gameSettings: GameSettings

    private val repository = GameRepositoryImpl
    private val context = application

    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)

    private var timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughCountOfRightAnswers = MutableLiveData<Boolean>()
    val enoughCountOfRightAnswers: LiveData<Boolean>
        get() = _enoughCountOfRightAnswers

    private val _enoughPercentOfRightAnswers = MutableLiveData<Boolean>()
    val enoughPercentOfRightAnswers: LiveData<Boolean>
        get() = _enoughPercentOfRightAnswers

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private var countOfRightAnswers = 0
    private var countOfQuestion = 0

    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
        updateProgress()
        generateQuestion()
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentOfRightAnswers.postValue(percent)
        _progressAnswers.postValue(
            String.format(
                context.resources.getString(R.string.percent_true_answers),
                countOfRightAnswers,
                gameSettings.minCountOfRightAnswers
            )
        )
        _enoughCountOfRightAnswers.value = countOfRightAnswers >= gameSettings.minCountOfRightAnswers
        _enoughPercentOfRightAnswers.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int {
        if (countOfQuestion == 0) {
            return 0
        }
        return ((countOfRightAnswers / countOfQuestion.toDouble()) * 100).toInt()
    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestion++
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.postValue(gameSettings.minPercentOfRightAnswers)
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        )  {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.postValue(formatTime(millisUntilFinished))
            }

            override fun onFinish() {
               finishGame()
            }
        }
        timer?.start()
    }

    private fun generateQuestion() {
        _question.postValue(generateQuestionUseCase(gameSettings.maxSumValue))
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun finishGame() {
        _gameResult.postValue(
            GameResult(
                enoughCountOfRightAnswers.value == true
                        && enoughPercentOfRightAnswers.value == true,
                countOfRightAnswers,
                countOfQuestion,
                gameSettings
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {

        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}