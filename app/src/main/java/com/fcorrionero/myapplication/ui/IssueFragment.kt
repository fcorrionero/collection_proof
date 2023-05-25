package com.fcorrionero.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.fcorrionero.myapplication.R
import com.fcorrionero.myapplication.domain.CollectionProof
import com.fcorrionero.myapplication.domain.IssueData

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [IssueFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IssueFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_issue, container, false)

        view.findViewById<Button>(R.id.issueButton).setOnClickListener {
            fillViewModel(view)
            onButtonClick(view)
        }

        return view
    }

    private fun fillViewModel(view: View) {
        val type = view.findViewById<EditText>(R.id.editTextText).text
        val solution = view.findViewById<EditText>(R.id.editTextText2).text
        val observations = view.findViewById<EditText>(R.id.editTextText3).text
        val issueData = IssueData(
            type.toString(),
            solution.toString(),
            observations.toString()
        )
        this.collectionProofViewModel.setIssueData(issueData)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment IssueFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IssueFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun onButtonClick(view: View) {
        Navigation.findNavController(view).navigate(R.id.navigateToBudgetFragment)
    }
}