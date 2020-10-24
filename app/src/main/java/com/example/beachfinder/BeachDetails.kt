package com.example.beachfinder

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.json.JSONException
import java.util.regex.Matcher
import java.util.regex.Pattern


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

    private var favItemYN: Boolean = false

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
        val txtLkNm: TextView = rootView.findViewById(R.id.txt_lknm)
        val txtLkTel: TextView = rootView.findViewById(R.id.txt_lktel)
        val txtLatLon: TextView = rootView.findViewById(R.id.txt_latlon)

        //title card
        pName.setText(param4+" 해변")
        pAdd.setText(param2+" "+ param3)

        //detail card
        if (param5 == "null")   txtWid.setText("정보없음") else txtWid.setText(param5 + "m")
        if (param6 == "null")   txtLen.setText("정보없음") else txtLen.setText(param6 + "m")
        if (param7 == "null")   txtKnd.setText("정보없음") else txtKnd.setText(param7)
        //링크연결 추가
        txtLkNm.setText(param9)
        //Transform 정의
        val transform =
            Linkify.TransformFilter(object : Linkify.TransformFilter, (Matcher, String) -> String {
                override fun transformUrl(p0: Matcher?, p1: String?): String {
                    return ""
                }

                override fun invoke(p1: Matcher, p2: String): String {
                    return ""
                }

            })
        //링크달 패턴 정의
        val pattern1 = Pattern.compile(param9)

        Linkify.addLinks(txtLkNm, pattern1, param8, null, transform)

        //링크연 추가
//      txtLkTel.setText(param10)
        val str = param10
        val arr = str?.split("(", ")")
        println(arr)

        txtLkTel.setText(arr?.get(1) + "\n("+arr?.get(0)+")")

        txtLatLon.setText("("+param11+", "+param12+")")

        var buttonX   = rootView.findViewById<View>(R.id.buttonX) as Button
        var buttonFav = rootView.findViewById<View>(R.id.buttonFav) as Button

        //즐겨찾기db에 속해있는지 확인
        checkHaveFavItem()

        //즐겨찾기db에 속해있다면 하트모양 변경
        if(favItemYN){
            buttonFav.setBackgroundResource(R.drawable.heart_full)
        }

        //종료 버튼 눌렀을 때 핸들링
        buttonX.setOnClickListener {
            val context = activity as AppCompatActivity
//            context.onBackPressed()
            context.finishFragment(this)
        }

        //즐겨찾기 버튼 눌렀을 때 핸들링
        buttonFav.setOnClickListener{
        try{
            //이미 즐겨찾기 상태 -> 즐겨찾기 삭
//            var query = "INSERT INTO mytable('name', 'addr') values('${param1}', '${param2}');"
            if(favItemYN) {
                var arr : Array<String> = arrayOf("${param1}")
                //인자가 여러개일땐 이런 방법으로
//              database.delete("mytable","txt=? AND _id=?",arr)
                database.delete("mytable","beach_id=?",arr)
                buttonFav.setBackgroundResource(R.drawable.heart_empty)
                favItemYN = false

                Toast.makeText(activity, "즐겨찾기 삭제되었습니다!", Toast.LENGTH_SHORT).show()
            //즐겨찾기 아닌 상태 -> 즐겨찾기 추가
            } else {
                var query =
                    "INSERT INTO mytable values('${param1}','${param2}','${param3}','${param4}','${param5}','${param6}','${param7}','${param8}','${param9}','${param10}','${param11}','${param12}');"
                database.execSQL(query)
                buttonFav.setBackgroundResource(R.drawable.heart_full)
                favItemYN = true

                Toast.makeText(activity, "즐겨찾기 추가되었습니다!", Toast.LENGTH_SHORT).show()
            }
        }
        catch(e: Exception) {
            Log.d("SQL Insert", "Not ok")
        }
            numb++
        }
        return rootView
    }

    //즐겨찾기db에 포함되어 있는지 확인하는 함수
    fun checkHaveFavItem(){
        var query = "SELECT * FROM mytable WHERE beach_id = ${param1};"
        var c = database.rawQuery(query,null)

        Log.d("checkHaveFavItem>", "${c.count}")

        if(c.count >0) favItemYN = true

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

//            즐겨찾기 전체 불러오는 방
//            var query = "SELECT * FROM mytable;"
//            var c = database.rawQuery(query,null)
//            while(c.moveToNext()){
//                Log.d("table>", c.getString(c.getColumnIndex("beach_id")))
//            즐겨찾기 특정 로우 지우기
//            var arr : Array<String> = arrayOf("${param1}")
//            database.delete("mytable","beach_id=?",arr)

