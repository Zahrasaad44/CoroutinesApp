package com.example.coroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.coroutinesapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.adviceBtn.setOnClickListener { requestAPI() }
    }

    private fun requestAPI() {
        CoroutineScope(IO).launch {
            val data = async { fetchAdvice() }.await()

            if (data.isNotEmpty()) {
                showAdvice(data)
            }
        }
    }

    private fun fetchAdvice(): String {
        var response = ""

        try {
            response = URL("https://api.adviceslip.com/advice").readText()
        } catch (exc: Exception) {
            Log.d("main", "ISSUE OCCURRED: $exc")
        }
        return response
    }

    private suspend fun showAdvice(advice: String) {
        withContext(Main) {      //Note to myself: it can be witten "withContext(Dispatchers.Main)
            val jsonObject = JSONObject(advice)
            val slip = jsonObject.getJSONObject("slip")
            // val id = slip.getInt("id")
            val advice = slip.getString("advice")

            binding.adviceTV.text = advice

        }
    }
}