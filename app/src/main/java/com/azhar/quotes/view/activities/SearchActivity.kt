package com.azhar.quotes.view.activities

import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhar.quotes.R
import com.azhar.quotes.adapter.QuotesAdapter
import com.azhar.quotes.model.ModelQuotes
import com.azhar.quotes.viewmodel.QuotesVewModel
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*

class SearchActivity : AppCompatActivity() {

    var page = 0
    lateinit var strName: String
    lateinit var quotesAdapter: QuotesAdapter
    lateinit var quotesVewModel: QuotesVewModel
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }

        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true)
            supportActionBar.setDisplayShowTitleEnabled(false)
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait…")
        progressDialog.setCancelable(false)
        progressDialog.setMessage("display quotes")

        imageClear.setOnClickListener { view: View? ->
            searchAnime.getText().clear()
            imageClear.setVisibility(View.GONE)
            rvListQuotes.setVisibility(View.GONE)
            imageSearch.setVisibility(View.VISIBLE)
        }

        searchAnime.setOnEditorActionListener(OnEditorActionListener { v: TextView, actionId: Int, event: KeyEvent? ->
            strName = searchAnime.getText().toString()
            if (strName.isEmpty()) {
                Toast.makeText(this@SearchActivity, "Form cannot be empty!", Toast.LENGTH_SHORT).show()
            } else {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    progressDialog.show()
                    quotesVewModel.setSearchQuotes(strName, page)
                    val inputMethodManager = v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
                    imageClear.setVisibility(View.VISIBLE)
                    return@setOnEditorActionListener true
                }
            }
            false
        })

        quotesAdapter = QuotesAdapter(this)
        rvListQuotes.setLayoutManager(LinearLayoutManager(this))
        rvListQuotes.setAdapter(quotesAdapter)
        rvListQuotes.setHasFixedSize(true)

        //method set viewmodel
        quotesVewModel = ViewModelProvider(this, NewInstanceFactory()).get(QuotesVewModel::class.java)
        quotesVewModel.getSearchQuotes().observe(this, Observer<ArrayList<ModelQuotes?>> { modelSearchData: ArrayList<ModelQuotes?> ->
            imageSearch.setVisibility(View.GONE)
            rvListQuotes.setVisibility(View.VISIBLE)
            progressDialog.dismiss()
            if (modelSearchData.size != 0) {
                quotesAdapter.setQuotesAdapter(modelSearchData)
            } else {
                Toast.makeText(this@SearchActivity, "Quotes Not Found!", Toast.LENGTH_SHORT).show()
            }
        })

        //pagination or endless recyclerview
        nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                page++
                progressDialog.show()
                quotesVewModel.setSearchQuotes(strName, page)
                quotesAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
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