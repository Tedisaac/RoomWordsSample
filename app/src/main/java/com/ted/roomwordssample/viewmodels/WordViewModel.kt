package com.ted.roomwordssample.viewmodels

import androidx.lifecycle.*
import com.ted.roomwordssample.repository.WordRepository
import com.ted.roomwordssample.models.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordViewModel(private val wordRepository: WordRepository) : ViewModel() {

    var allWords: LiveData<List<Word>> = wordRepository.getAllWords()

    fun addWord(word: Word){
        viewModelScope.launch (Dispatchers.IO){ wordRepository.insertWord(word) }
    }

    fun deleteWords(){
        viewModelScope.launch (Dispatchers.IO){ wordRepository.deleteAllWords() }
    }

    fun deleteWord(word: String){
        viewModelScope.launch (Dispatchers.IO){ wordRepository.deleteWord(word) }
    }

}

class ViewModelFactory(private val wordRepository: WordRepository) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)){
            return WordViewModel(wordRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}