package com.ted.roomwordssample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.ted.roomwordssample.databinding.ActivityAddWordBinding
import com.ted.roomwordssample.models.Word
import com.ted.roomwordssample.models.WordRoomDatabase
import com.ted.roomwordssample.repository.WordRepository
import com.ted.roomwordssample.viewmodels.ViewModelFactory
import com.ted.roomwordssample.viewmodels.WordViewModel

class AddWordActivity : AppCompatActivity() {
    private lateinit var addWordBinding: ActivityAddWordBinding
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
        addWordBinding = ActivityAddWordBinding.inflate(layoutInflater)
        setContentView(addWordBinding.root)

        addWordBinding.btnAddWord.setOnClickListener { validateWord() }
    }

    private fun validateWord() {
        if (addWordBinding.edWord.text.isEmpty()){
            Snackbar.make(this, addWordBinding.root, "Please input word", Snackbar.LENGTH_SHORT).show()
        }else{
            wordViewModel.addWord(Word(word = addWordBinding.edWord.text.toString()))
            addWordBinding.edWord.text.clear()
        }
    }

    private fun switchToMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        switchToMainScreen()
    }
}