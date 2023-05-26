package com.fcorrionero.myapplication.infrastructure

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import com.fcorrionero.myapplication.R
import com.fcorrionero.myapplication.domain.CollectionProof
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
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

    fun generatePdf(context: Context, collectionProof: CollectionProof) {
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

            document.add(getHeader(context))

            document.add(getTitle(context))

            document.add(Paragraph(Chunk.NEWLINE))
            document.add(getBillHeader(context))

            document.add(Paragraph(Chunk.NEWLINE))
            document.add(getClientData(context, collectionProof))

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // Close the document
            document.close()
        }
    }

    private fun getHeader(context: Context): PdfPTable {
        val table = PdfPTable(3) // 2 columns.
        val text = context.getString(R.string.shop_direction)
        val paragraph = Paragraph()
        for (line in text.split("\n")) {
            paragraph.add(Phrase(line))
            paragraph.add(Chunk.NEWLINE)
        }
        val cell1 = PdfPCell(paragraph)
        cell1.border = Rectangle.NO_BORDER
        table.addCell(cell1)

        val cell2 = PdfPCell(Paragraph(""))
        cell2.border = Rectangle.NO_BORDER
        table.addCell(cell2)

        val cell3 = PdfPCell()
        cell3.border = Rectangle.NO_BORDER
        cell3.horizontalAlignment = Element.ALIGN_CENTER

        val logo: Bitmap? = BitmapFactory.decodeResource(context.resources, R.drawable.logo)
        if (null != logo) {
            // Convert the bitmap to an iText Image
            val stream = ByteArrayOutputStream()
            logo.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val image: Image = Image.getInstance(stream.toByteArray())
            stream.close()
            // Scale image to 10%
            image.scalePercent(20f)
            cell3.addElement(image)
            cell3.border = Rectangle.NO_BORDER
        }
        table.addCell(cell3)
        return table
    }

    private fun getTitle(context: Context): PdfPTable {
        val table = PdfPTable(1)
        val font = FontFactory.getFont(FontFactory.HELVETICA, 24f, Font.BOLD)
        val paragraph = Paragraph()
        paragraph.add(Chunk.NEWLINE)
        paragraph.add(Phrase(context.getString(R.string.bill_title), font))
        val cell = PdfPCell(paragraph)
        cell.border = Rectangle.NO_BORDER
        cell.horizontalAlignment = Element.ALIGN_RIGHT
        table.addCell(cell)
        return table
    }

    private fun getBillHeader(context: Context): PdfPTable {
        val font = FontFactory.getFont(FontFactory.HELVETICA, 10f)
        val fontBold = FontFactory.getFont(FontFactory.HELVETICA, 10f, Font.BOLD)
        val table = PdfPTable(floatArrayOf(1f,1f,6.5f,1.5f)) // Relative width of each column

        val cellA1 = PdfPCell(Phrase(context.getString(R.string.ae_number), font))
        cellA1.border = Rectangle.NO_BORDER

        val cellA2 = PdfPCell(Phrase("1", font))// TODO
        cellA2.border = Rectangle.NO_BORDER

        val cellB1 = PdfPCell(Phrase(context.getString(R.string.bill_enter_date), fontBold))
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val currentDateAndTime: String = sdf.format(Date())
        val cellB2 = PdfPCell(Phrase(currentDateAndTime, font))
        cellB2.backgroundColor = BaseColor.LIGHT_GRAY

        table.addCell(cellA1)
        table.addCell(cellA2)
        table.addCell(cellB1)
        table.addCell(cellB2)

        return table
    }

    private fun getClientData(context: Context, collectionProof: CollectionProof): PdfPTable {
        val fontBoldHeader = FontFactory.getFont(FontFactory.HELVETICA, 18f, Font.BOLD)
        val fontBold = FontFactory.getFont(FontFactory.HELVETICA, 10f, Font.BOLD)
        val font = FontFactory.getFont(FontFactory.HELVETICA, 10f)
        val tableHeader = PdfPTable(1)
        val cellHeader = PdfPCell(Phrase(context.getString(R.string.client_data), fontBoldHeader))
        cellHeader.backgroundColor = BaseColor.LIGHT_GRAY
        cellHeader.horizontalAlignment = Element.ALIGN_CENTER
        tableHeader.addCell(cellHeader)

        val tableBody = PdfPTable(2)
        val cellL1 = PdfPCell(Phrase(context.getString(R.string.client_name) , fontBold))
        val cellR1 = PdfPCell(Phrase(collectionProof.getClientData()?.name ?: "", font))
        cellR1.backgroundColor = BaseColor.LIGHT_GRAY
        tableBody.addCell(cellL1)
        tableBody.addCell(cellR1)

        val cellL2 = PdfPCell(Phrase(context.getString(R.string.client_phone) , fontBold))
        val cellR2 = PdfPCell(Phrase(collectionProof.getClientData()?.phone ?: "", font))
        cellR2.backgroundColor = BaseColor.LIGHT_GRAY
        tableBody.addCell(cellL2)
        tableBody.addCell(cellR2)

        val cellL3 = PdfPCell(Phrase(context.getString(R.string.client_address) , fontBold))
        val cellR3 = PdfPCell(Phrase(collectionProof.getClientData()?.address ?: "", font))
        cellR3.backgroundColor = BaseColor.LIGHT_GRAY
        tableBody.addCell(cellL3)
        tableBody.addCell(cellR3)

        val cellL4 = PdfPCell(Phrase(context.getString(R.string.client_identification_number) , fontBold))
        val cellR4 = PdfPCell(Phrase(collectionProof.getClientData()?.dni ?: "", font))
        cellR4.backgroundColor = BaseColor.LIGHT_GRAY
        tableBody.addCell(cellL4)
        tableBody.addCell(cellR4)

        val table = PdfPTable(1)
        val cell1 = PdfPCell(tableHeader)
        cell1.border =  Rectangle.NO_BORDER
        val cell2 = PdfPCell(tableBody)
        cell2.border =  Rectangle.NO_BORDER
        table.addCell(cell1)
        table.addCell(cell2)

        return table
    }

}