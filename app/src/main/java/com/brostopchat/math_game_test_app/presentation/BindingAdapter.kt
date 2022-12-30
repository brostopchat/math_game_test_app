package com.brostopchat.math_game_test_app.presentation

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.brostopchat.math_game_test_app.R
import com.brostopchat.math_game_test_app.domain.entity.GameResult

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.needed_true_answers),
        count
    )
}

@BindingAdapter("countAnswers")
fun bindCountAnswers(textView: TextView, score: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.your_score),
        score
    )
}

@BindingAdapter("requiredPercent")
fun bindRequiredPercent(textView: TextView, percentage: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.needed_percent_true_answers),
        percentage
    )
}

@BindingAdapter("scorePercentage")
fun bindScorePercentage(textView: TextView, gameResult: GameResult) {
    textView.text = String.format(
        textView.context.getString(R.string.percent_true_answers),
        getPercentOfRightAnswers(gameResult)
    )
}

private fun getPercentOfRightAnswers(gameResult: GameResult) = with(gameResult) {
    if (countOfQuestions == 0) {
        0
    } else {
        ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }
}