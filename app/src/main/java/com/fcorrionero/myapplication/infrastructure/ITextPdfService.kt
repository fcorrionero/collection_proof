package com.fcorrionero.myapplication.infrastructure

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import com.fcorrionero.myapplication.R
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.Rectangle
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ITextPdfService {

    fun generatePdf(context: Context) {
        val document = Document()

        try {
            val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            val currentDateAndTime: String = sdf.format(Date())
            val fileName = "$currentDateAndTime.pdf"
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "$fileName.pdf"
            )
            PdfWriter.getInstance(document, FileOutputStream(file.absolutePath))

            // Open the document
            document.open()

            val table = PdfPTable(2) // 2 columns.
            val text = context.getString(R.string.shop_direction)
            val paragraph = Paragraph()
            for (line in text.split("\n")) {
                paragraph.add(Phrase(line))
                paragraph.add(Chunk.NEWLINE)
            }
            val cell1 = PdfPCell(paragraph)
            cell1.border = Rectangle.NO_BORDER
            table.addCell(cell1)

            val cell2 = PdfPCell()
            cell2.border = Rectangle.NO_BORDER
            cell2.horizontalAlignment = Element.ALIGN_RIGHT

            val logo: Bitmap? = BitmapFactory.decodeResource(context.resources, R.drawable.logo)
            if (null != logo) {
                // Convert the bitmap to an iText Image
                val stream = ByteArrayOutputStream()
                logo.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val image: Image = Image.getInstance(stream.toByteArray())
                stream.close()
                // Scale image to 50%
                image.scalePercent(10f)
                cell2.addElement(image)
                cell2.border = Rectangle.NO_BORDER
            }
            table.addCell(cell2)


            document.add(table)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // Close the document
            document.close()
        }
    }

}