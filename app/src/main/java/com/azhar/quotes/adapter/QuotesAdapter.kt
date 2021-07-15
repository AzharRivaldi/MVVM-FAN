package com.azhar.quotes.adapter

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.azhar.quotes.R
import com.azhar.quotes.adapter.QuotesAdapter.MainViewHolder
import com.azhar.quotes.model.ModelQuotes
import com.azhar.quotes.view.fragment.FragmentTranslate
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.list_item_quotes.view.*
import java.util.*

/**
 * Created by Azhar Rivaldi on 03-07-2021
 * Youtube Channel : https://bit.ly/2PJMowZ
 * Github : https://github.com/AzharRivaldi
 * Twitter : https://twitter.com/azharrvldi_
 * Instagram : https://www.instagram.com/azhardvls_
 * LinkedIn : https://www.linkedin.com/in/azhar-rivaldi
 */

class QuotesAdapter() : RecyclerView.Adapter<MainViewHolder>() {

    private lateinit var context: Context
    private val modelQuotesArrayList = ArrayList<ModelQuotes>()

    fun setQuotesAdapter(items: ArrayList<ModelQuotes>?) {
        modelQuotesArrayList.clear()
        modelQuotesArrayList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_quotes, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val modelQuotes = modelQuotesArrayList[position]

        holder.tvTitleAnime.text = modelQuotes.anime
        holder.tvCharacterAnime.text = "Character : " + modelQuotes.character
        holder.tvQuotes.text = "“" + modelQuotes.quote + "”"

        holder.imageCopy.setOnClickListener { view: View? ->
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.text = modelQuotes.quote
            val snackBar = Snackbar.make(view, "Quotes copied to clipboard", Snackbar.LENGTH_INDEFINITE)
            snackBar.setActionTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            snackBar.setAction("DISMISS") { v: View? -> snackBar.dismiss() }
            snackBar.show()
        }

        holder.imageTranslate.setOnClickListener { view: View? ->
            val fragment: Fragment = FragmentTranslate()
            val bundle = Bundle()
            bundle.putString("Quotes", modelQuotes.quote)
            bundle.putString("CharacterAnime", modelQuotes.character)
            fragment.arguments = bundle
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameContainer, fragment)
            fragmentTransaction.commit()
        }
    }

    override fun getItemCount(): Int {
        return modelQuotesArrayList.size
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitleAnime: TextView
        var tvCharacterAnime: TextView
        var tvQuotes: TextView
        var imageCopy: ImageView
        var imageTranslate: ImageView

        init {
            tvTitleAnime = itemView.tvTitleAnime
            tvCharacterAnime = itemView.tvCharacterAnime
            tvQuotes = itemView.tvQuotes
            imageCopy = itemView.imageCopy
            imageTranslate = itemView.imageTranslate
        }
    }

}