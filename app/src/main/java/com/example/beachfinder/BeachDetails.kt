package com.example.beachfinder

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.MapView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [BeachDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class BeachDetails : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        arguments?.let {
            param1 = it.getString("passBName")
            param2 = it.getString("passBAdd")

            Log.d("arguments in", "${param1}, ${param2}")
        }
        rootView = inflater.inflate(R.layout.fragment_beach_details, container, false)
        val pName: TextView = rootView.findViewById(R.id.pName)
        val pAdd: TextView = rootView.findViewById(R.id.pAdd)

        pName.setText(param1)
        pAdd.setText(param2)
        Log.d("beach details ok! ", "beach details ok ok ok ${rootView}")

        return rootView
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment BeachDetails.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            BeachDetails().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }

    override fun onStart() {
        Log.d("beach detail", "beach detail on start!")
        super.onStart()
    }

    override fun onResume() {

        Log.d("beach detail", "beach detail on resume!")
        super.onResume()
    }

    override fun onPause() {
        Log.d("beach detail", "beach detail on pause!")
        val fragment = BeachDetails()

        super.onPause()
    }

}