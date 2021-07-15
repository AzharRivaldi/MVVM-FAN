package com.azhar.quotes.view.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhar.quotes.R
import com.azhar.quotes.adapter.QuotesAdapter
import com.azhar.quotes.model.ModelQuotes
import com.azhar.quotes.viewmodel.QuotesVewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var quotesVewModel: QuotesVewModel
    lateinit var quotesAdapter: QuotesAdapter
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait…")
        progressDialog.setCancelable(false)
        progressDialog.setMessage("display quotes")

        //set data adapter
        quotesAdapter = QuotesAdapter(this)
        rvRandomQuotes.setLayoutManager(LinearLayoutManager(this))
        rvRandomQuotes.setAdapter(quotesAdapter)
        rvRandomQuotes.setHasFixedSize(true)

        //set viewmodel
        quotesVewModel = ViewModelProvider(this, NewInstanceFactory()).get(QuotesVewModel::class.java)
        quotesVewModel.setRandomQuotes()
        progressDialog.show()
        quotesVewModel.getRandomQuotes().observe(this, Observer<ArrayList<ModelQuotes?>> { modelQuotes: ArrayList<ModelQuotes?> ->
            if (modelQuotes.size != 0) {
                quotesAdapter.setQuotesAdapter(modelQuotes)
            } else {
                Toast.makeText(this, "Quotes Not Found!", Toast.LENGTH_SHORT).show()
            }
            progressDialog.dismiss()
        })

        //open search activity
        fabSearch.setOnClickListener { view: View? ->
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }

    }

    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val window = activity.window
            val layoutParams = window.attributes
            if (on) {
                layoutParams.flags = layoutParams.flags or bits
            } else {
                layoutParams.flags = layoutParams.flags and bits.inv()
            }
            window.attributes = layoutParams
        }
    }

}
