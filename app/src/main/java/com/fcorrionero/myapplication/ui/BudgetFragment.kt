package com.fcorrionero.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.fcorrionero.myapplication.R
import com.fcorrionero.myapplication.domain.BudgetData
import com.fcorrionero.myapplication.domain.CollectionProof
import com.fcorrionero.myapplication.infrastructure.ITextPdfService
import com.github.gcacace.signaturepad.views.SignaturePad

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BudgetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BudgetFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val collectionProofViewModel: CollectionProof by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_budget, container, false)

        view.findViewById<Button>(R.id.budgetButton).setOnClickListener {
            fillViewModel(view)
            //val pdfService = PdfService()
            //pdfService.generatePdf(collectionProofViewModel, this.requireContext())
            val documentService = ITextPdfService()
            documentService.generatePdf(this.requireContext(), this.collectionProofViewModel)
        }

        return view
    }

    private fun fillViewModel(view: View) {
        val quantity = view.findViewById<EditText>(R.id.editTextText4).text
        val acceptation = view.findViewById<CheckBox>(R.id.checkBox).isChecked
        val signature = view.findViewById<SignaturePad>(R.id.signature_pad)
        val budgetData = BudgetData(
            quantity.toString(),
            acceptation,
            signature.transparentSignatureBitmap
        )
        this.collectionProofViewModel.setBudgetData(budgetData)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BudgetFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BudgetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}