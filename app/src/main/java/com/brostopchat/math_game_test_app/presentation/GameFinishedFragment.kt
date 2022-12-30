package com.brostopchat.math_game_test_app.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.brostopchat.math_game_test_app.R
import com.brostopchat.math_game_test_app.databinding.FragmentGameFinishedBinding
import com.brostopchat.math_game_test_app.domain.entity.GameResult
import com.brostopchat.math_game_test_app.domain.entity.Level

class GameFinishedFragment: Fragment() {

    private val args by navArgs<GameFinishedFragmentArgs>()

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnClickListener()
        bindViews()
    }

    private fun bindViews() {
        binding.gameResult = args.gameResult
        with(binding) {
//            tvText1.text = String.format(
//                getString(R.string.needed_true_answers),
//                args.gameResult.gameSettings.minCountOfRightAnswers
//            )
//            tvText2.text = String.format(
//                getString(R.string.your_score),
//                args.gameResult.countOfRightAnswers
//            )
//            tvText3.text = String.format(
//                getString(R.string.needed_percent_true_answers),
//                args.gameResult.gameSettings.minPercentOfRightAnswers
//            )
            tvText4.text = String.format(
                getString(R.string.percent_true_answers),
                getPercentOfRightAnswers()
            )
        }
    }

    private fun getPercentOfRightAnswers() = with(args.gameResult) {
        if (countOfQuestions == 0) {
             0
        } else {
            ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
        }
    }

    private fun setupOnClickListener() {
        binding.buttonTryAgain.setOnClickListener() {
            retryGame()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }

}