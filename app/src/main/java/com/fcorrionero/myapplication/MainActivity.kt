package com.fcorrionero.myapplication

import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
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
        val details = this.findViewById<EditText>(R.id.editTextDetails)
        val signature = this.findViewById<SignaturePad>(R.id.signature_pad)
        val bitmap = signature.getTransparentSignatureBitmap()

        val pdfDocument = PdfDocument()
        val text = Paint()
        val paint = Paint()

        val pageInfo = PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create()
        val myPage = pdfDocument.startPage(pageInfo)
        text.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        text.textSize = 15F
        text.color = ContextCompat.getColor(this, R.color.black)
        text.textAlign = Paint.Align.LEFT

        val canvas: Canvas = myPage.canvas
        val directions = getString(R.string.shop_direction).split("<br/>")
        var height = 30F
        directions.forEach {
            canvas.drawText(it, 30F, height, text)
            height += 20F
        }

        val logo: Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.logo_appinformatica)
        // ResourcesCompat.getDrawable(resources, R.drawable.logo_appinformatica, null)

        if (null != logo) {
            canvas.drawBitmap(
                Bitmap.createScaledBitmap(logo, logo.width / 5, logo.height / 5, false),
                100F,
                30F,
                paint
            )
        }
        canvas.drawText(
            "${phone.text} ${name.text} ${surname.text}\n${details.text}",
            56F,
            220F,
            text
        )

        val scaledBmp = Bitmap.createScaledBitmap(bitmap, 140, 140, false)
        canvas.drawBitmap(scaledBmp, 56F, 350F, paint)

        pdfDocument.finishPage(myPage)

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "GFG.pdf"
        )
        try {
            pdfDocument.writeTo(FileOutputStream(file))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        pdfDocument.close()
    }

}