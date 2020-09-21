package com.example.beachfinder

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.json.JSONException
import org.json.JSONObject


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

    lateinit var dbHelper : DBHelper
    lateinit var database : SQLiteDatabase

    private lateinit var rootView: View
    val preference by lazy{ getActivity()?.getSharedPreferences("BeachFavorite", Context.MODE_MULTI_PROCESS)}
    var numb = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreate(savedInstanceState)

        //sql
        try {
            dbHelper = DBHelper(getActivity(), "newdb.db", null, 1)
            database = dbHelper.writableDatabase
        } catch(e: JSONException) {
            Log.d("e: JSONException", "Error Occur")
        }

        arguments?.let {
            param1 = it.getString("passBName") //이름
            param2 = it.getString("passBAdd") //주소

            Log.d("arguments in", "${param1}, ${param2}")
        }
        rootView = inflater.inflate(R.layout.fragment_beach_details, container, false)

        val pName: TextView = rootView.findViewById(R.id.pName)
        val pAdd: TextView = rootView.findViewById(R.id.pAdd)

        pName.setText(param1)
        pAdd.setText(param2)

        var buttonX   = rootView.findViewById<View>(R.id.buttonX) as Button
        var buttonFav = rootView.findViewById<View>(R.id.buttonFav) as Button
        var buttonChk = rootView.findViewById<View>(R.id.buttonChk) as Button
        var buttonFvRemove = rootView.findViewById<View>(R.id.buttonFvRemove) as Button

        val sName = "sName"
        val sAdd = "sAdd"

        //종료 버튼 눌렀을 때 핸들링
        buttonX.setOnClickListener{
            val context = activity as AppCompatActivity
            context.onBackPressed()
        }

        //즐겨찾기 버튼 눌렀을 때 핸들링
        buttonFav.setOnClickListener{
        try{
            var query = "INSERT INTO mytable('name', 'addr') values('${param1}', '${param2}');"
            database.execSQL(query)
        }
        catch(e: Exception) {
            Log.d("", "ok")
        }

//            var myJSONObject = JSONObject()
//            myJSONObject.put("name","양양" + "${numb}")
//            myJSONObject.put("add","강원도양양시" + "${numb}")
//            val myString: String = myJSONObject.toString()
//
//            preference?.edit()?.putString("양양", myString)?.apply()
//            preference?.edit()?.commit();

            numb++

        }

        buttonChk.setOnClickListener{

            var query = "SELECT * FROM mytable;"
            var c = database.rawQuery(query,null)
            while(c.moveToNext()){
                System.out.println("my table : "+c.getString(c.getColumnIndex("name"))+",  "+c.getString(c.getColumnIndex("addr")));
            }
        }

        buttonFvRemove.setOnClickListener{

            var arr : Array<String> = arrayOf("${param1}")
//            database.delete("mytable","txt=? AND _id=?",arr)
            database.delete("mytable","name=?",arr)
        }

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
        super.onPause()
    }

    override fun onStop() {
        Log.d("beach detail", "beach detail on stop!")
        super.onStop()
    }

}

