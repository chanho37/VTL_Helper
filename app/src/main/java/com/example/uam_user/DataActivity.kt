package com.example.uam_user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DataActivity : AppCompatActivity() {

    private lateinit var editText1: EditText
    private lateinit var editText2: EditText
    private lateinit var editText3: EditText
    private lateinit var editText4: EditText
    private lateinit var resultTextView: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        editText1 = findViewById(R.id.editText1)
        editText2 = findViewById(R.id.editText2)
        editText3 = findViewById(R.id.editText3)
        editText4 = findViewById(R.id.editText4)
        resultTextView = findViewById(R.id.resultTextView)

    }


    fun onCompareButtonClick(view: View) {
        val num1 = editText1.text.toString().toDoubleOrNull() ?: 0.0
        val num2 = editText2.text.toString().toDoubleOrNull() ?: 0.0
        val num3 = editText3.text.toString().toDoubleOrNull() ?: 0.0
        val num4 = editText4.text.toString().toDoubleOrNull() ?: 0.0

        val squareSum1 = num1 * num1 + num2 * num2
        val squareSum2 = num3 * num3 + num4 * num4

        val result = when {
            squareSum1 > squareSum2 -> "Number 2 is closer"
            squareSum1 < squareSum2 -> "Number 1 is closer"
            else -> "Numbers are equal"
        }


        showToast(result)
        resultTextView.text = "Result: $result"
    }



    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }



}