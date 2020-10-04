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
/**
 * A simple [Fragment] subclass.
 * Use the [BeachDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class BeachDetails : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null
    private var param4: String? = null
    private var param5: String? = null
    private var param6: String? = null
    private var param7: String? = null
    private var param8: String? = null
    private var param9: String? = null
    private var param10: String? = null
    private var param11: String? = null
    private var param12: String? = null


    lateinit var dbHelper : DBHelper
    lateinit var database : SQLiteDatabase

    private lateinit var rootView: View
    val preference by lazy{ getActivity()?.getSharedPreferences("BeachFavorite", Context.MODE_MULTI_PROCESS)}
    var numb = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("beachdetail", "beachdetail")
        super.onCreate(savedInstanceState)

        //sql
        try {
            dbHelper = DBHelper(getActivity(), "newdb.db", null, 1)
            database = dbHelper.writableDatabase
        } catch(e: JSONException) {
            Log.d("e: JSONException", "Error Occur")
        }

        arguments?.let {

            param1 = it.getString("pBeach_id") //아이디
            param2 = it.getString("pSido_nm") //시도
            param3 = it.getString("pGugun_nm") //구군
            param4 = it.getString("pSta_nm") //해변
            param5 = it.getString("pBeach_wid") //폭
            param6 = it.getString("pBeach_len") //길이
            param7 = it.getString("pBeach_knd") //해변종류
            param8 = it.getString("pLink_addr") //링크주소
            param9 = it.getString("pLink_nm") //링크이름
            param10 = it.getString("pLink_tel") //링크전화화
            param11 = it.getString("pLat") //위도
            param12 = it.getString("pLon") //경도

            Log.d("arguments in", "${param1}, ${param2},${param3}, ${param4},${param5}, ${param6},${param7}, ${param8},${param9}, ${param10},${param11}, ${param12}")
        }
        rootView = inflater.inflate(R.layout.fragment_beach_details, container, false)

        //title card
        val pName: TextView = rootView.findViewById(R.id.pName)
        val pAdd: TextView = rootView.findViewById(R.id.pAdd)
        //detail card
        val txtWid: TextView = rootView.findViewById(R.id.txt_wid)
        val txtLen: TextView = rootView.findViewById(R.id.txt_len)
        val txtKnd: TextView = rootView.findViewById(R.id.txt_knd)
        val txtLkAddr: TextView = rootView.findViewById(R.id.txt_addr)
        val txtLkNm: TextView = rootView.findViewById(R.id.txt_lknm)
        val txtLkTel: TextView = rootView.findViewById(R.id.txt_lktel)
        val txtLatLon: TextView = rootView.findViewById(R.id.txt_latlon)

        //title card
        pName.setText(param4+" 해변")
        pAdd.setText(param2+" "+ param3)
        //detail card
        if (param5 == "null")   txtWid.setText("정보없음") else txtWid.setText(param5)
        if (param6 == "null")   txtLen.setText("정보없음") else txtLen.setText(param6)
        if (param7 == "null")   txtKnd.setText("정보없음") else txtKnd.setText(param7)
        txtLkAddr.setText(param8)
        txtLkNm.setText(param9)
        txtLkTel.setText(param10)
        txtLatLon.setText("("+param11+", "+param12+")")

        var buttonX   = rootView.findViewById<View>(R.id.buttonX) as Button
        var buttonFav = rootView.findViewById<View>(R.id.buttonFav) as Button
        var buttonChk = rootView.findViewById<View>(R.id.buttonChk) as Button
        var buttonFvRemove = rootView.findViewById<View>(R.id.buttonFvRemove) as Button

        //종료 버튼 눌렀을 때 핸들링
        buttonX.setOnClickListener{
            val context = activity as AppCompatActivity
//            context.onBackPressed()
            context.finishFragment(this)
        }

        //즐겨찾기 버튼 눌렀을 때 핸들링
        buttonFav.setOnClickListener{
        try{
//            var query = "INSERT INTO mytable('name', 'addr') values('${param1}', '${param2}');"
            var query = "INSERT INTO mytable values('${param1}','${param2}','${param3}','${param4}','${param5}','${param6}','${param7}','${param8}','${param9}','${param10}','${param11}','${param12}');"
            database.execSQL(query)
        }
        catch(e: Exception) {
            Log.d("SQL Insert", "ok")
        }

            numb++

        }

        buttonChk.setOnClickListener{

            var query = "SELECT * FROM mytable;"
            var c = database.rawQuery(query,null)
            while(c.moveToNext()){
                Log.d("table>", c.getString(c.getColumnIndex("beach_id"))+", "+c.getString(c.getColumnIndex("sido_nm"))+", "
                    +c.getString(c.getColumnIndex("gugun_nm"))+", "+c.getString(c.getColumnIndex("sta_nm"))+", "
                        +c.getString(c.getColumnIndex("beach_wid"))+", "+c.getString(c.getColumnIndex("beach_len"))+", "
                        +c.getString(c.getColumnIndex("beach_knd"))+", "+c.getString(c.getColumnIndex("link_addr"))+", "
                        +c.getString(c.getColumnIndex("link_nm"))+", "+c.getString(c.getColumnIndex("link_tel"))+", "
                        +c.getString(c.getColumnIndex("lat"))+", "+c.getString(c.getColumnIndex("lon")))
            }
        }

        buttonFvRemove.setOnClickListener{

            var arr : Array<String> = arrayOf("${param1}")
//          database.delete("mytable","txt=? AND _id=?",arr)
            database.delete("mytable","beach_id=?",arr)
        }

        return rootView
    }

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

