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
import com.fcorrionero.myapplication.domain.ClientData
import com.fcorrionero.myapplication.domain.CollectionProof

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClientFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClientFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_client, container, false)

        view.findViewById<Button>(R.id.clientButton).setOnClickListener {
            fillViewModel(view)
            val action = ClientFragmentDirections.navigateToDeviceFragment(22)
            Navigation.findNavController(view).navigate(action)
        }

        return view
    }

    private fun fillViewModel(view: View) {
        val clientName = view.findViewById<EditText>(R.id.textName).text
        val clientPhone = view.findViewById<EditText>(R.id.editTextPhone).text
        val clientAddress = view.findViewById<EditText>(R.id.editTextAddress).text
        val clientDni = view.findViewById<EditText>(R.id.editTextDNI).text
        val clientData = ClientData(
            clientName.toString(),
            clientPhone.toString(),
            clientAddress.toString(),
            clientDni.toString()
        )
        this.collectionProofViewModel.setClientData(clientData)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ClientFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ClientFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}