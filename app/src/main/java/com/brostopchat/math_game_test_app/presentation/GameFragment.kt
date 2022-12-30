package com.brostopchat.math_game_test_app.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.brostopchat.math_game_test_app.R
import com.brostopchat.math_game_test_app.databinding.FragmentGameBinding
import com.brostopchat.math_game_test_app.domain.entity.GameResult
import com.brostopchat.math_game_test_app.domain.entity.GameSettings
import com.brostopchat.math_game_test_app.domain.entity.Level

class GameFragment: Fragment() {

    private val args by navArgs<GameFragmentArgs>()

    private val viewModelFactory by lazy {
        GameViewModelFactory(args.level, requireActivity().application)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
    }

    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }
    }

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
    get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setClickListenersToOptions()
    }

    private fun setClickListenersToOptions() {
        for (tvOption in tvOptions) {
            tvOption.setOnClickListener() {
                viewModel.chooseAnswer(tvOption.text.toString().toInt())
            }
        }
    }

    private fun launchGameFinishFragment(gameResult: GameResult) {
        findNavController().navigate(
            GameFragmentDirections.actionGameFragmentToGameFinishedFragment(gameResult)
        )
    }

    private fun observeViewModel() {
        with(viewModel) {
            question.observe(viewLifecycleOwner) {
                binding.tvSum.text = it.sum.toString()
                binding.tvLeftNumber.text = it.visibleNumber.toString()
                for (i in 0 until tvOptions.size) {
                    tvOptions[i].text = it.options[i].toString()
                }
            }
            percentOfRightAnswers.observe(viewLifecycleOwner) {
                binding.tvProgressBar.setProgress(it, true)
            }
            enoughCountOfRightAnswers.observe(viewLifecycleOwner) {
                binding.tvAnswerProgress.setTextColor(getColorByState(it))
            }
            enoughPercentOfRightAnswers.observe(viewLifecycleOwner) {
                val color = getColorByState(it)
                binding.tvProgressBar.progressTintList = ColorStateList.valueOf(color)
            }
            formattedTime.observe(viewLifecycleOwner) {
                binding.tvTimer.text = it
            }
            minPercent.observe(viewLifecycleOwner) {
                binding.tvProgressBar.secondaryProgress = it
            }
            gameResult.observe(viewLifecycleOwner) {
                launchGameFinishFragment(it)
            }
            progressAnswers.observe(viewLifecycleOwner) {
                binding.tvAnswerProgress.text = it
            }
        }
    }

    private fun getColorByState(goodState: Boolean): Int {
        val resColorId = if (goodState) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(), resColorId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}