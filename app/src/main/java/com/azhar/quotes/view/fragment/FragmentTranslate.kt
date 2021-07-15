package com.azhar.quotes.view.fragment

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.azhar.quotes.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_translate.*
import org.json.JSONException
import org.json.JSONObject

class FragmentTranslate : BottomSheetDialogFragment() {

    lateinit var progressDialog: ProgressDialog
    lateinit var strGetQuotes: String
    lateinit var strGetCharacter: String
    lateinit var strResult: String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (requireView().parent as View).setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_translate, container, false)

        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Please wait…")
        progressDialog.setCancelable(false)
        progressDialog.setMessage("translate quotes")

        //get data translate
        val bundle = arguments
        if (bundle != null) {
            strGetQuotes = bundle.getString("Quotes")
            strGetCharacter = bundle.getString("CharacterAnime")
            getTranslate(strGetQuotes)
        }

        //close view
        fabClose.setOnClickListener({ view1: View? -> dismiss() })

        return view
    }

    private fun getTranslate(strGetQuotes: String) {
        progressDialog.show()
        AndroidNetworking.get("https://amm-api-translate.herokuapp.com/translate?engine=google&text={text}&to=id")
                .addPathParameter("text", strGetQuotes)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        progressDialog.dismiss()
                        try {
                            val jsonObject = response.getJSONObject("data")
                            strResult = jsonObject.getString("result")
                            tvTranslate.text = strResult
                            tvCharacterAnime.text = "-$strGetCharacter-"
                        } catch (e: JSONException) {
                            Toast.makeText(context, "Oops, failed to translate!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onError(anError: ANError) {
                        progressDialog.dismiss()
                        Toast.makeText(context, "Oops! There seems to be a problem with your internet connection.",
                                Toast.LENGTH_SHORT).show()
                    }
                })
    }

}