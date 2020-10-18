package com.example.madlevel4task2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var gameRepository: GameRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "MadLevel4Task2"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        gameRepository = GameRepository(requireContext())

        ivRock.setImageResource(R.drawable.rock)
        ivPaper.setImageResource(R.drawable.paper)
        ivScissors.setImageResource(R.drawable.scissors)

        ivRock.setOnClickListener {
            startGame(GameElementsEnum.ROCK)
        }
        ivPaper.setOnClickListener {
            startGame(GameElementsEnum.PAPER)
        }
        ivScissors.setOnClickListener {
            startGame(GameElementsEnum.SCISSER)
        }
    }

    fun startGame(user: GameElementsEnum) {
        val computer = computerAnswer()
        ivComputer.setImageResource(computer.element)
        ivYou.setImageResource(user.element)

        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.insertGame(
                    Game(
                        computer.element,
                        user.element,
                        Date(),
                        getWinner(computer, user)
                    )
                )
            }
        }
    }

    fun computerAnswer(): GameElementsEnum {
        return GameElementsEnum.values().toList().shuffled().get(0)
    }

    private fun getWinner(computer: GameElementsEnum, user: GameElementsEnum): String {

        if (computer == GameElementsEnum.ROCK && user == GameElementsEnum.SCISSER ||
            computer == GameElementsEnum.PAPER && user == GameElementsEnum.ROCK ||
            computer == GameElementsEnum.SCISSER && user == GameElementsEnum.PAPER
        ) {
            mainScope.launch {
                withContext(Dispatchers.Main) {
                    tvWinner.setText(R.string.computerWins)
                }
            }

            return "Computer wins!"
        } else if (
            computer == GameElementsEnum.ROCK && user == GameElementsEnum.PAPER ||
            computer == GameElementsEnum.PAPER && user == GameElementsEnum.SCISSER ||
            computer == GameElementsEnum.SCISSER && user == GameElementsEnum.ROCK
        ) {
            mainScope.launch {
                withContext(Dispatchers.Main) {
                    tvWinner.setText(R.string.youWins)
                }
            }
            return "You win!"
        } else {
              mainScope.launch {
                withContext(Dispatchers.Main) {
                    tvWinner.setText(R.string.draw)
                }
            }
            return "Draw"
        }
    }
}