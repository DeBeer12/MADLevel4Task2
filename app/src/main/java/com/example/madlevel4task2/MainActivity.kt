package com.example.madlevel4task2

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var menu: Menu
    private lateinit var gameRepository: GameRepository
    private lateinit var history: HistoryFragment
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        gameRepository = GameRepository(applicationContext)
        history = HistoryFragment()
        navController = findNavController(R.id.nav_host_fragment)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        this.menu = menu
        return true
    }

    override fun onBackPressed() {
        menu.clear()
        menuInflater.inflate(R.menu.menu_main, menu)
        navController.navigate(
            R.id.action_historyFragment_to_homeFragment
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.btnHistory || item.itemId == android.R.id.home) {
            menu.clear()
            navController.navigate(
                if (navController.currentDestination?.id == R.id.historyFragment) {
                    menuInflater.inflate(R.menu.menu_main, menu)
                    R.id.action_historyFragment_to_homeFragment
                } else {
                    menuInflater.inflate(R.menu.menu_history, menu)
                    R.id.action_homeFragment_to_historyFragment
                }
            )
        } else {
            mainScope.launch {
                withContext(Dispatchers.IO) {
                    gameRepository.deleteAllGames()
                }
            }
            Toast.makeText(
                applicationContext,
                "All the games are removed, please reload the page",
                Toast.LENGTH_LONG
            ).show()
        }

        return when (item.itemId) {
            R.id.btnHistory -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}