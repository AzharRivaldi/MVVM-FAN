package com.azhar.quotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.azhar.quotes.model.ModelQuotes
import org.json.JSONArray
import org.json.JSONException
import java.util.*

/**
 * Created by Azhar Rivaldi on 03-07-2021
 * Youtube Channel : https://bit.ly/2PJMowZ
 * Github : https://github.com/AzharRivaldi
 * Twitter : https://twitter.com/azharrvldi_
 * Instagram : https://www.instagram.com/azhardvls_
 * LinkedIn : https://www.linkedin.com/in/azhar-rivaldi
 */

class QuotesVewModel : ViewModel() {

    private val modelQuotesLiveData = MutableLiveData<ArrayList<ModelQuotes>>()
    private val modelSearchLiveData = MutableLiveData<ArrayList<ModelQuotes>>()

    fun setRandomQuotes() {
        val modelQuotesList = ArrayList<ModelQuotes>()
        AndroidNetworking.get("https://animechan.vercel.app/api/quotes")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray) {
                        try {
                            for (i in 0 until response.length()) {
                                val jsonObject = response.getJSONObject(i)
                                val modelQuotes = ModelQuotes()
                                modelQuotes.anime = jsonObject.getString("anime")
                                modelQuotes.character = jsonObject.getString("character")
                                modelQuotes.quote = jsonObject.getString("quote")
                                modelQuotesList.add(modelQuotes)
                            }
                            modelQuotesLiveData.postValue(modelQuotesList)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onError(anError: ANError) {
                        anError.errorDetail
                    }
                })
    }

    fun setSearchQuotes(strName: String, strPage: Int) {
        val modelSearchList = ArrayList<ModelQuotes>()
        //AndroidNetworking.get("https://animechan.vercel.app/api/quotes/anime?title={title}&?page={page}")
        AndroidNetworking.get("https://animechan.vercel.app/api/quotes/character?name={name}&page={page}")
                .addPathParameter("name", strName)
                .addPathParameter("page", strPage.toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray) {
                        try {
                            for (i in 0 until response.length()) {
                                val jsonObject = response.getJSONObject(i)
                                val modelQuotes = ModelQuotes()
                                modelQuotes.anime = jsonObject.getString("anime")
                                modelQuotes.character = jsonObject.getString("character")
                                modelQuotes.quote = jsonObject.getString("quote")
                                modelSearchList.add(modelQuotes)
                            }
                            modelSearchLiveData.postValue(modelSearchList)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onError(anError: ANError) {
                        anError.errorDetail
                    }
                })
    }

    fun getRandomQuotes(): LiveData<ArrayList<ModelQuotes>> = modelQuotesLiveData

    fun getSearchQuotes(): LiveData<ArrayList<ModelQuotes>> = modelSearchLiveData

}