package com.example.madlevel4task2

import android.content.Context
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HistoryFragment : Fragment() {

    private val historyList = arrayListOf<Game>()
    private val gameAdapter = GameAdapter(historyList)
    private val mainScope = CoroutineScope(Dispatchers.Main)

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var gameRepository: GameRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Game history"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        gameRepository = GameRepository(requireContext())
        getGamesListFromDatabase()
        initViews()
    }

    private fun initViews() {
        viewManager = LinearLayoutManager(activity)
        rvGameHistory.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )

        rvGameHistory.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = gameAdapter
        }
    }

    private fun getGamesListFromDatabase() {
        mainScope.launch {
            val gameList = withContext(Dispatchers.IO) {
                gameRepository.getAllGames()
            }
            this@HistoryFragment.historyList.clear()
            this@HistoryFragment.historyList.addAll(gameList)
            this@HistoryFragment.gameAdapter.notifyDataSetChanged()
        }
    }
}
