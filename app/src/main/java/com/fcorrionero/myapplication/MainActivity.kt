package com.fcorrionero.myapplication

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.gcacace.signaturepad.views.SignaturePad
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    val pageHeight = 1120
    val pagewidth = 792
    private val PERMISSION_REQUEST_CODE = 200

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

        val pdfDocument = PdfDocument()
        val text = Paint()
        val paint = Paint()

        val pageInfo = PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create()
        val myPage = pdfDocument.startPage(pageInfo)
        text.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        text.textSize = 15F
        text.color = ContextCompat.getColor(this, R.color.purple_200)
        text.textAlign = Paint.Align.CENTER

        // creating a variable for canvas
        // from our page of PDF.
        // creating a variable for canvas
        // from our page of PDF.
        val canvas: Canvas = myPage.canvas


        val scaledbmp = Bitmap.createScaledBitmap(bitmap, 140, 140, false)
        canvas.drawBitmap(scaledbmp, 56F, 40F, paint)
        canvas.drawText("This is sample document which we have created.", 396F, 560F, text)

        pdfDocument.finishPage(myPage)

        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "GFG.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
        }catch (e: IOException) {
            e.printStackTrace()
        }

        pdfDocument.close()
    }

}