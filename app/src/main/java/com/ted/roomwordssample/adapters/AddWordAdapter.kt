package com.ted.roomwordssample.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ted.roomwordssample.R
import com.ted.roomwordssample.databinding.WordLayoutBinding
import com.ted.roomwordssample.models.Word

class AddWordAdapter(private val words: List<Word>) :
    RecyclerView.Adapter<AddWordAdapter.ItemViewHolder>() {

    lateinit var removeWordInterface: RemoveWordInterface

    class ItemViewHolder(val wordLayoutBinding: WordLayoutBinding) : RecyclerView.ViewHolder(wordLayoutBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val wordLayoutBinding = WordLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ItemViewHolder(wordLayoutBinding)
    }

    fun onWordSelected(removeWordInterface: RemoveWordInterface) {
        this.removeWordInterface = removeWordInterface
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        with(holder){
            with(words[position]){
                wordLayoutBinding.txtWord.text = word
                wordLayoutBinding.imgBtnDeleteWord.setOnClickListener {
                    if (removeWordInterface != null && position != RecyclerView.NO_POSITION){
                        removeWordInterface.removeWordOnClick(word)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return words.size
    }

}

interface RemoveWordInterface{
    fun removeWordOnClick(currentWord: String)
}