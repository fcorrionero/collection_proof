package com.fcorrionero.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.github.gcacace.signaturepad.views.SignaturePad

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onSaveBtnClick(v: View) {
        val phone = this.findViewById<EditText>(R.id.editTextPhone)
        val name = this.findViewById<EditText>(R.id.textName)
        val surname = this.findViewById<EditText>(R.id.textSurname)
        val signature = this.findViewById<SignaturePad>(R.id.signature_pad)
        val bitmap = signature.getTransparentSignatureBitmap()
        val a = 1
    }
}