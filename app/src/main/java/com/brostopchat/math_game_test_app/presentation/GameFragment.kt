package com.brostopchat.math_game_test_app.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.brostopchat.math_game_test_app.R
import com.brostopchat.math_game_test_app.databinding.FragmentGameBinding
import com.brostopchat.math_game_test_app.domain.entity.GameResult
import com.brostopchat.math_game_test_app.domain.entity.GameSettings
import com.brostopchat.math_game_test_app.domain.entity.Level

class GameFragment: Fragment() {

    private lateinit var level: Level

    private val gameResult = GameResult(
        false,
        5,
        10,
        GameSettings(
            15,
            7,
            90,
            8
        )
    )

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
    get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

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
        binding.tvSum.setOnClickListener() {
            launchGameFinishFragment(gameResult)
        }
    }

    private fun launchGameFinishFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        level = requireArguments().getSerializable(KEY_LEVEL) as Level
    }

    companion object {

        private const val KEY_LEVEL = "level"
        const val NAME = "GameFragment"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_LEVEL, level)
                }
            }
        }
    }
}