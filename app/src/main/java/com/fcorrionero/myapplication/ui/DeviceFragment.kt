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
import androidx.navigation.fragment.navArgs
import com.fcorrionero.myapplication.R
import com.fcorrionero.myapplication.domain.CollectionProof
import com.fcorrionero.myapplication.domain.DeviceData

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DeviceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeviceFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val collectionProofViewModel: CollectionProof by activityViewModels()

    private val args: DeviceFragmentArgs by navArgs()

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
        val view = inflater.inflate(R.layout.fragment_device, container, false)

        val number = args.number
        view.findViewById<Button>(R.id.button).setOnClickListener {
            fillViewModel(view)
            onButtonClick(view)
        }

        return view
    }

    private fun fillViewModel(view: View) {
        val accesories = view.findViewById<EditText>(R.id.deviceAccessoriesText).text
        val brandAndModel = view.findViewById<EditText>(R.id.brandAndModelText).text
        val serialImei = view.findViewById<EditText>(R.id.serialImeiText).text
        val deviceData = DeviceData(
            accesories.toString(),
            brandAndModel.toString(),
            serialImei.toString()
        )
        this.collectionProofViewModel.setDeviceData(deviceData)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DeviceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DeviceFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun onButtonClick(view: View) {
        Navigation.findNavController(view).navigate(R.id.navigateToIssueFragment)
    }

}