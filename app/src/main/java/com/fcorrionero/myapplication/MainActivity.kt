package com.fcorrionero.myapplication

import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.text.TextPaint
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.gcacace.signaturepad.views.SignaturePad
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class MainActivity : AppCompatActivity() {

    val pageHeight = 1120
    val pagewidth = 792
    private val INITIAL_PAGE_HEIGHT = 60F
    private val LEFT_PAGE_MARGIN = 30F
    private val LABEL_WIDTH_SPACING = 11F
    private val LABEL_HEIGHT_SPACING = 1.2F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onSaveBtnClick(v: View) {
//        val phone = this.findViewById<EditText>(R.id.editTextPhone)
//        val name = this.findViewById<EditText>(R.id.textName)
//        val surname = this.findViewById<EditText>(R.id.textSurname)
//        val details = this.findViewById<EditText>(R.id.editTextDetails)
        val signature = this.findViewById<SignaturePad>(R.id.signature_pad)
        val bitmap = signature.getTransparentSignatureBitmap()

        val pdfDocument = PdfDocument()
        val defaultText = TextPaint()
        val paint = Paint()

        val pageInfo = PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create()
        val myPage = pdfDocument.startPage(pageInfo)
        defaultText.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        defaultText.textSize = 15F
        defaultText.color = ContextCompat.getColor(this, R.color.black)
        defaultText.textAlign = Paint.Align.LEFT

        val canvas: Canvas = myPage.canvas

        var height = drawShopAddress(canvas, defaultText)
        drawLogo(canvas, paint, myPage)
        height = drawBillTitle(canvas, height)
        height = drawAENumber(canvas, height, defaultText)
        val labelText = TextPaint()
        labelText.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        labelText.textSize = 15F
        labelText.color = ContextCompat.getColor(this, R.color.black)
        labelText.textAlign = Paint.Align.LEFT
        height = drawDeviceEnterDate(canvas, labelText, defaultText, height)
        drawDeviceData(canvas, labelText, defaultText, height)
        height = drawClientData(canvas, labelText, defaultText, height)
        height = drawIssueData(canvas, labelText, defaultText, height)
        height = drawBudgetData(canvas, labelText, defaultText, height)

        val scaledBmp = Bitmap.createScaledBitmap(bitmap, 140, 140, false)
        canvas.drawBitmap(scaledBmp, 56F, height, paint)

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

    private fun drawBudgetData(
        canvas: Canvas,
        labelText: TextPaint,
        dataText: TextPaint,
        height: Float
    ): Float {
        val dataWidth = 250F
        var tempHeight = height + labelText.textSize * LABEL_HEIGHT_SPACING
        canvas.drawText(getString(R.string.budget_title), 250F, tempHeight, labelText)
        tempHeight += labelText.textSize * LABEL_HEIGHT_SPACING
        canvas.drawText(
            getString(R.string.budget_quantity),
            LEFT_PAGE_MARGIN,
            tempHeight,
            labelText
        )
        canvas.drawText(
            getString(R.string.budget_quantity),
            dataWidth,
            tempHeight,
            dataText
        )

        val extraWidth = dataWidth + labelText.textSize * LABEL_HEIGHT_SPACING + 200F
        val dataExtraWidth = extraWidth + getString(R.string.budget_acceptation).length * LABEL_WIDTH_SPACING
        canvas.drawText(
            getString(R.string.budget_acceptation),
            extraWidth,
            tempHeight,
            labelText
        )
        canvas.drawText(
            getString(R.string.budget_acceptation),
            dataExtraWidth,
            tempHeight,
            dataText
        )
        tempHeight += labelText.textSize * LABEL_HEIGHT_SPACING
        canvas.drawText(
            getString(R.string.client_signature),
            extraWidth,
            tempHeight,
            labelText
        )
        return tempHeight
    }

    private fun drawIssueData(
        canvas: Canvas,
        labelText: TextPaint,
        dataText: TextPaint,
        height: Float
    ): Float {
        val dataWidth = 250F
        var tempHeight = height + labelText.textSize * LABEL_HEIGHT_SPACING
        canvas.drawText(getString(R.string.issue_title), 250F, height, labelText)
        canvas.drawText(
            getString(R.string.issue_type),
            LEFT_PAGE_MARGIN,
            tempHeight,
            labelText
        )
        canvas.drawText(
            getString(R.string.issue_type),
            dataWidth,
            tempHeight,
            dataText
        )
        tempHeight += labelText.textSize * LABEL_HEIGHT_SPACING
        canvas.drawText(
            getString(R.string.issue_solution),
            LEFT_PAGE_MARGIN,
            tempHeight,
            labelText
        )
        canvas.drawText(
            getString(R.string.issue_solution),
            dataWidth,
            tempHeight,
            dataText
        )
        tempHeight += labelText.textSize * LABEL_HEIGHT_SPACING
        canvas.drawText(
            getString(R.string.issue_observations),
            LEFT_PAGE_MARGIN,
            tempHeight,
            labelText
        )
        canvas.drawText(
            getString(R.string.issue_observations),
            dataWidth,
            tempHeight,
            dataText
        )
        tempHeight += labelText.textSize * LABEL_HEIGHT_SPACING
        canvas.drawText(
            getString(R.string.issue_photos),
            LEFT_PAGE_MARGIN,
            tempHeight,
            labelText
        )
        canvas.drawText(
            getString(R.string.issue_photos),
            dataWidth,
            tempHeight,
            dataText
        )
        return tempHeight + labelText.textSize * LABEL_HEIGHT_SPACING
    }

    private fun drawDeviceData(
        canvas: Canvas,
        labelText: TextPaint,
        dataText: TextPaint,
        height: Float
    ): Float {
        val width = 370F
        val dataWidth = width + getString(R.string.device_serial_imei).length * LABEL_WIDTH_SPACING
        canvas.drawText(getString(R.string.device), width + 100F, height, labelText)
        var tempHeight = height + labelText.textSize * LABEL_HEIGHT_SPACING
        canvas.drawText(
            getString(R.string.device_accessories),
            width,
            tempHeight,
            labelText
        )
        canvas.drawText(
            getString(R.string.device_accessories),
            dataWidth,
            tempHeight,
            dataText
        )
        tempHeight += labelText.textSize * LABEL_HEIGHT_SPACING
        canvas.drawText(
            getString(R.string.device_brand_model),
            width,
            tempHeight,
            labelText
        )
        canvas.drawText(
            getString(R.string.device_brand_model),
            dataWidth,
            tempHeight,
            dataText
        )
        tempHeight += labelText.textSize * LABEL_HEIGHT_SPACING
        canvas.drawText(
            getString(R.string.device_serial_imei),
            width,
            tempHeight,
            labelText
        )
        canvas.drawText(
            getString(R.string.device_serial_imei),
            dataWidth,
            tempHeight,
            dataText
        )
        return height
    }

    private fun drawClientData(
        canvas: Canvas,
        labelText: TextPaint,
        dataText: TextPaint,
        height: Float
    ): Float {
        var tempHeight = height + labelText.textSize * LABEL_HEIGHT_SPACING
        canvas.drawText(getString(R.string.client_data), 100F, height, labelText)
        canvas.drawText(
            getString(R.string.client_name),
            LEFT_PAGE_MARGIN,
            tempHeight,
            labelText
        )
        canvas.drawText(
            getString(R.string.client_name),
            LEFT_PAGE_MARGIN + getString(R.string.client_name).length * LABEL_WIDTH_SPACING,
            tempHeight,
            dataText
        )
        tempHeight += labelText.textSize * LABEL_HEIGHT_SPACING
        canvas.drawText(
            getString(R.string.client_phone),
            LEFT_PAGE_MARGIN,
            tempHeight,
            labelText
        )
        canvas.drawText(
            getString(R.string.client_phone),
            LEFT_PAGE_MARGIN + getString(R.string.client_name).length * LABEL_WIDTH_SPACING,
            tempHeight,
            dataText
        )
        tempHeight += labelText.textSize * LABEL_HEIGHT_SPACING
        canvas.drawText(
            getString(R.string.client_address),
            LEFT_PAGE_MARGIN,
            tempHeight,
            labelText
        )
        canvas.drawText(
            getString(R.string.client_address),
            LEFT_PAGE_MARGIN + getString(R.string.client_name).length * LABEL_WIDTH_SPACING,
            tempHeight,
            dataText
        )
        tempHeight += labelText.textSize * LABEL_HEIGHT_SPACING
        canvas.drawText(
            getString(R.string.client_identification_number),
            LEFT_PAGE_MARGIN,
            tempHeight,
            labelText
        )
        canvas.drawText(
            getString(R.string.client_identification_number),
            LEFT_PAGE_MARGIN + getString(R.string.client_name).length * LABEL_WIDTH_SPACING,
            tempHeight,
            dataText
        )
        tempHeight += labelText.textSize * LABEL_HEIGHT_SPACING

        return tempHeight
    }

    private fun drawDeviceEnterDate(
        canvas: Canvas,
        labelText: TextPaint,
        dataText: TextPaint,
        height: Float
    ): Float {
        val simpleDateFormat = SimpleDateFormat("mm/dd/yyyy", Locale.ENGLISH)
        val date = simpleDateFormat.format(Date.from(Instant.now()))
        val width = 250F + getString(R.string.bill_enter_date).length * 8F
        canvas.drawText(getString(R.string.bill_enter_date), 250F, height, labelText)
        canvas.drawText(date, width, height, dataText)

        return height + labelText.textSize + 15F
    }

    private fun drawAENumber(
        canvas: Canvas,
        height: Float,
        text: TextPaint
    ): Float {
        canvas.drawText(getString(R.string.ae_number), LEFT_PAGE_MARGIN, height, text)
        return height
    }

    private fun drawBillTitle(
        canvas: Canvas,
        height: Float
    ): Float {
        val titleText = TextPaint()
        titleText.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        titleText.textSize = 30F
        titleText.color = ContextCompat.getColor(this, R.color.black)
        titleText.textAlign = Paint.Align.LEFT
        canvas.drawText(
            getString(R.string.bill_title),
            LEFT_PAGE_MARGIN,
            height + titleText.fontSpacing + 30F,
            titleText
        )
        return (height + titleText.fontSpacing + 60F)
    }

    private fun drawLogo(
        canvas: Canvas,
        paint: Paint,
        page: PdfDocument.Page
    ) {
        val logo: Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.logo)
        // ResourcesCompat.getDrawable(resources, R.drawable.logo, null)
        if (null != logo) {
            val bitmap = Bitmap.createScaledBitmap(logo, logo.width / 5, logo.height / 5, false)
            canvas.drawBitmap(
                bitmap,
                page.info.pageWidth - bitmap.width - LEFT_PAGE_MARGIN,
                INITIAL_PAGE_HEIGHT,
                paint
            )
        }
    }

    private fun drawShopAddress(
        canvas: Canvas,
        text: TextPaint
    ): Float {
        val directions = getString(R.string.shop_direction).split("\n")
        var height = INITIAL_PAGE_HEIGHT
        directions.forEach {
            canvas.drawText(it, LEFT_PAGE_MARGIN, height, text)
            height += text.fontSpacing
        }
        return height
    }

}