package com.ted.roomwordssample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.ted.roomwordssample.adapters.AddWordAdapter
import com.ted.roomwordssample.adapters.RemoveWordInterface
import com.ted.roomwordssample.databinding.ActivityMainBinding
import com.ted.roomwordssample.models.WordRoomDatabase
import com.ted.roomwordssample.repository.WordRepository
import com.ted.roomwordssample.viewmodels.ViewModelFactory
import com.ted.roomwordssample.viewmodels.WordViewModel

class MainActivity : AppCompatActivity(), RemoveWordInterface {

    private lateinit var adapter: AddWordAdapter
    private lateinit var binding: ActivityMainBinding
    val wordViewModel: WordViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory(
                WordRepository(
                    WordRoomDatabase.getDatabase(this)
                )
            )
        )[WordViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAddWord.setOnClickListener { switchToAddWordScreen() }

        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        val linearLayout: LayoutManager = LinearLayoutManager(this)
        binding.rvWords.layoutManager = linearLayout

        fetchWords()
    }

    private fun fetchWords() {
        wordViewModel.allWords.observe(this){ result ->
            adapter = AddWordAdapter(result)
            adapter.onWordSelected(this)
            binding.rvWords.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun switchToAddWordScreen() {
        startActivity(Intent(this, AddWordActivity::class.java))
        finish()
    }

    override fun removeWordOnClick(currentWord: String) {
        wordViewModel.deleteWord(currentWord)
    }

}