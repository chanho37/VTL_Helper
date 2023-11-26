package com.example.uam_user

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DataActivity : AppCompatActivity() {

    private lateinit var textView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        textView = findViewById(R.id.textView)
        val downloadButton: Button = findViewById(R.id.downloadButton)

        downloadButton.setOnClickListener {
            // 네트워크에서 데이터 다운로드 및 표시
            downloadAndDisplayData()
        }
    }

    private fun downloadAndDisplayData() {
        // 코루틴을 사용하여 백그라운드에서 네트워크 작업 수행
        MainScope().launch {
            try {
                val result = fetchDataFromNetwork()
                // 결과 처리
                if (result != null) {
                    // 결과를 TextView에 표시
                    textView.text = "Data from server: $result"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@DataActivity, "Error downloading data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun fetchDataFromNetwork(): String? {
        return withContext(Dispatchers.IO) {
            try {
                // Flask 앱의 엔드포인트 URL
                val url = URL("http://127.0.0.1:5000/get_integer") // 안드로이드 에뮬레이터에서는 10.0.2.2를 사용
                val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection

                try {
                    // InputStream으로부터 데이터 읽기
                    val `in`: InputStream = urlConnection.inputStream
                    convertStreamToString(`in`)
                } finally {
                    urlConnection.disconnect()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun convertStreamToString(`is`: InputStream): String {
        // InputStream을 문자열로 변환
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()

        var line: String?
        try {
            while (reader.readLine().also { line = it } != null) {
                sb.append(line).append('\n')
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return sb.toString()
    }
}