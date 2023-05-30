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

            val tableClientDevice = PdfPTable(floatArrayOf(4f, 1f, 4f))
            val cellClient = PdfPCell(getClientData(context, collectionProof))
            val middleCell = PdfPCell()
            middleCell.border = Rectangle.NO_BORDER
            val cellDevice = PdfPCell(getDeviceData(context, collectionProof))
            tableClientDevice.addCell(cellClient)
            tableClientDevice.addCell(middleCell)
            tableClientDevice.addCell(cellDevice)
            document.add(Paragraph(Chunk.NEWLINE))
            document.add(tableClientDevice)

            document.add(Paragraph(Chunk.NEWLINE))
            document.add(getIssueData(context, collectionProof))

            document.add(Paragraph(Chunk.NEWLINE))
            document.add(getBudget(context, collectionProof))
            document.add(getSignature(context, collectionProof))


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
        val table = PdfPTable(floatArrayOf(1f, 1f, 6.5f, 1.5f)) // Relative width of each column

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
        val cellL1 = PdfPCell(Phrase(context.getString(R.string.client_name), fontBold))
        val cellR1 = PdfPCell(Phrase(collectionProof.getClientData()?.name ?: "", font))
        tableBody.addCell(cellL1)
        tableBody.addCell(cellR1)

        val cellL2 = PdfPCell(Phrase(context.getString(R.string.client_phone), fontBold))
        val cellR2 = PdfPCell(Phrase(collectionProof.getClientData()?.phone ?: "", font))
        tableBody.addCell(cellL2)
        tableBody.addCell(cellR2)

        val cellL3 = PdfPCell(Phrase(context.getString(R.string.client_address), fontBold))
        val cellR3 = PdfPCell(Phrase(collectionProof.getClientData()?.address ?: "", font))
        tableBody.addCell(cellL3)
        tableBody.addCell(cellR3)

        val cellL4 =
            PdfPCell(Phrase(context.getString(R.string.client_identification_number), fontBold))
        val cellR4 = PdfPCell(Phrase(collectionProof.getClientData()?.dni ?: "", font))
        tableBody.addCell(cellL4)
        tableBody.addCell(cellR4)

        val table = PdfPTable(1)
        val cell1 = PdfPCell(tableHeader)
        cell1.border = Rectangle.NO_BORDER
        val cell2 = PdfPCell(tableBody)
        cell2.border = Rectangle.NO_BORDER
        table.addCell(cell1)
        table.addCell(cell2)

        return table
    }

    private fun getDeviceData(context: Context, collectionProof: CollectionProof): PdfPTable {
        val fontBoldHeader = FontFactory.getFont(FontFactory.HELVETICA, 18f, Font.BOLD)
        val fontBold = FontFactory.getFont(FontFactory.HELVETICA, 10f, Font.BOLD)
        val font = FontFactory.getFont(FontFactory.HELVETICA, 10f)

        val tableHeader = PdfPTable(1)
        val cellHeader = PdfPCell(Phrase(context.getString(R.string.device), fontBoldHeader))
        cellHeader.backgroundColor = BaseColor.LIGHT_GRAY
        cellHeader.horizontalAlignment = Element.ALIGN_CENTER
        tableHeader.addCell(cellHeader)

        val tableBody = PdfPTable(2)
        val cellL1 = PdfPCell(Phrase(context.getString(R.string.device_accessories), fontBold))
        val cellR1 =
            PdfPCell(Phrase(collectionProof.getDeviceData()?.deviceAccessories ?: "", font))
        tableBody.addCell(cellL1)
        tableBody.addCell(cellR1)

        val cellL2 = PdfPCell(Phrase(context.getString(R.string.device_brand_model), fontBold))
        val cellR2 = PdfPCell(Phrase(collectionProof.getDeviceData()?.deviceBrandModel ?: "", font))
        tableBody.addCell(cellL2)
        tableBody.addCell(cellR2)

        val cellL3 = PdfPCell(Phrase(context.getString(R.string.device_serial_imei), fontBold))
        val cellR3 = PdfPCell(Phrase(collectionProof.getDeviceData()?.deviceSerialImei ?: "", font))
        tableBody.addCell(cellL3)
        tableBody.addCell(cellR3)

        val table = PdfPTable(1)
        val cell1 = PdfPCell(tableHeader)
        cell1.border = Rectangle.NO_BORDER
        val cell2 = PdfPCell(tableBody)
        cell2.border = Rectangle.NO_BORDER
        table.addCell(cell1)
        table.addCell(cell2)

        return table
    }

    private fun getIssueData(context: Context, collectionProof: CollectionProof): PdfPTable {
        val fontBoldHeader = FontFactory.getFont(FontFactory.HELVETICA, 18f, Font.BOLD)
        val fontBold = FontFactory.getFont(FontFactory.HELVETICA, 10f, Font.BOLD)
        val font = FontFactory.getFont(FontFactory.HELVETICA, 10f)

        val tableHeader = PdfPTable(1)
        val cellHeader = PdfPCell(Phrase(context.getString(R.string.issue_title), fontBoldHeader))
        cellHeader.backgroundColor = BaseColor.LIGHT_GRAY
        cellHeader.horizontalAlignment = Element.ALIGN_CENTER
        tableHeader.addCell(cellHeader)

        val tableBody = PdfPTable(2)
        val cellL1 = PdfPCell(Phrase(context.getString(R.string.issue_type), fontBold))
        val cellR1 = PdfPCell(Phrase(collectionProof.getIssueData()?.issueType ?: "", font))
        tableBody.addCell(cellL1)
        tableBody.addCell(cellR1)

        val cellL2 = PdfPCell(Phrase(context.getString(R.string.issue_solution), fontBold))
        val cellR2 = PdfPCell(Phrase(collectionProof.getIssueData()?.issueSolution ?: "", font))
        tableBody.addCell(cellL2)
        tableBody.addCell(cellR2)

        val cellL3 = PdfPCell(Phrase(context.getString(R.string.issue_observations), fontBold))
        val cellR3 = PdfPCell(Phrase(collectionProof.getIssueData()?.issueObservations ?: "", font))
        tableBody.addCell(cellL3)
        tableBody.addCell(cellR3)

        val table = PdfPTable(1)
        val cell1 = PdfPCell(tableHeader)
        cell1.border = Rectangle.NO_BORDER
        val cell2 = PdfPCell(tableBody)
        cell2.border = Rectangle.NO_BORDER
        table.addCell(cell1)
        table.addCell(cell2)

        return table
    }

    private fun getBudget(context: Context, collectionProof: CollectionProof): PdfPTable {
        val fontBoldHeader = FontFactory.getFont(FontFactory.HELVETICA, 18f, Font.BOLD)
        val fontBold = FontFactory.getFont(FontFactory.HELVETICA, 10f, Font.BOLD)
        val font = FontFactory.getFont(FontFactory.HELVETICA, 10f)

        val tableHeader = PdfPTable(1)
        val cellHeader = PdfPCell(Phrase(context.getString(R.string.budget_title), fontBoldHeader))
        cellHeader.backgroundColor = BaseColor.LIGHT_GRAY
        cellHeader.horizontalAlignment = Element.ALIGN_CENTER
        tableHeader.addCell(cellHeader)

        val tableBody = PdfPTable(floatArrayOf(30f, 15f, 5f, 25f, 25f))
        val cell1 = PdfPCell(Phrase(context.getString(R.string.budget_quantity), fontBold))
        val cell2 = PdfPCell(Phrase(collectionProof.getBudgetData()?.budgetQuantity ?: "", font))
        tableBody.addCell(cell1)
        tableBody.addCell(cell2)
        val cell3 = PdfPCell()
        cell3.border = Rectangle.NO_BORDER
        tableBody.addCell(cell3)
        val cell4 = PdfPCell(Phrase(context.getString(R.string.budget_acceptation), fontBold))
        val acceptation = if (collectionProof.getBudgetData()?.budgetAcceptation == true) {
            "SI"
        } else {
            "NO"
        }
        val cell5 = PdfPCell(Phrase(acceptation, font))
        tableBody.addCell(cell4)
        tableBody.addCell(cell5)

        val table = PdfPTable(1)
        val cellM1 = PdfPCell(tableHeader)
        cellM1.border = Rectangle.NO_BORDER
        val cellM2 = PdfPCell(tableBody)
        cellM2.border = Rectangle.NO_BORDER
        table.addCell(cellM1)
        table.addCell(cellM2)

        return table
    }

    private fun getSignature(context: Context, collectionProof: CollectionProof): PdfPTable {
        val fontBold = FontFactory.getFont(FontFactory.HELVETICA, 10f, Font.BOLD)
        val tableBody = PdfPTable(floatArrayOf(5f, 5f))

        val emptyCell = PdfPCell()
        emptyCell.border = Rectangle.NO_BORDER
        tableBody.addCell(emptyCell)
        val cell2 = PdfPCell(Phrase(context.getString(R.string.client_signature), fontBold))
        cell2.backgroundColor = BaseColor.LIGHT_GRAY
        cell2.horizontalAlignment = Element.ALIGN_CENTER
        tableBody.addCell(cell2)
        tableBody.addCell(emptyCell)

        val stream = ByteArrayOutputStream()
        collectionProof.getBudgetData()?.signature?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        val img = Image.getInstance(byteArray)
        val cellSignature = PdfPCell(img, true)
        tableBody.addCell(cellSignature)

        return tableBody
    }

}