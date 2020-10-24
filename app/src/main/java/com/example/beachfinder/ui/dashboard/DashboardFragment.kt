package com.example.beachfinder.ui.dashboard

import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beachfinder.*
import kotlinx.android.synthetic.main.text_row_item.view.*
import org.json.JSONException
import org.json.JSONObject
import java.util.Objects.isNull

class BInfo {
    var beachId:String = ""
    var sidoNm:String = ""
    var gugunNm:String = ""
    var staNm:String = ""
    var beachWid:String = ""
    var beachLen:String = ""
    var beachKnd:String = ""
    var linkAddr:String = ""
    var linkNm:String = ""
    var linkTel:String = ""
    var blat:String = ""
    var blon:String = ""
}

class DashboardFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var dataset: Array<String>
    private lateinit var dataset2: Array<String>
    private var names : ArrayList<String> = arrayListOf<String>()
    private var binfos : ArrayList<BInfo> = arrayListOf<BInfo>()


    //DB
    lateinit var dbHelper : DBHelper
    lateinit var database : SQLiteDatabase

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        selectFavList()

        val rootView = inflater.inflate(R.layout.fragment_notifications, container, false)

        recyclerView = rootView.findViewById(R.id.recyclerView)

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        layoutManager = LinearLayoutManager(activity)

        setRecyclerViewLayoutManager()
        // Set CustomAdapter as the adapter for RecyclerView.
//        recyclerView.adapter = CustomAdapter(dataset2)

        recyclerView.adapter = FavRecyclerViewAdapter()

        return rootView

    }

    private fun setRecyclerViewLayoutManager() {
        var scrollPosition = 0

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.layoutManager != null) {
            scrollPosition = (recyclerView.layoutManager as LinearLayoutManager)
                .findFirstCompletelyVisibleItemPosition()
        }

        layoutManager = LinearLayoutManager(activity)

        with(recyclerView) {
            layoutManager = this@DashboardFragment.layoutManager
            scrollToPosition(scrollPosition)
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun selectFavList() {
        binfos.clear()
        names.clear()

        Log.d("SelectFavList", "SelectFavList in ok")

        try {
            dbHelper = DBHelper(getActivity(), "newdb.db", null, 1)
            database = dbHelper.writableDatabase
        } catch(e: JSONException) {
            Log.d("e: JSONException", "Error Occur")
        }

        //전체 db 데이타 select
        var query = "SELECT * FROM mytable;"

        var c = database.rawQuery(query,null)
        Log.d("db COunt", c.count.toString())

        while(c.moveToNext()){
            //객체 정의
            var binfo = BInfo()

            //db값 무브
            if(isNull(c.getString(c.getColumnIndex("beach_id")))) binfo.beachId    = ""
            else binfo.beachId  = c.getString(c.getColumnIndex("beach_id"))

            if(isNull(c.getString(c.getColumnIndex("sido_nm")))) binfo.sidoNm    = ""
            else binfo.sidoNm  = c.getString(c.getColumnIndex("sido_nm"))

            if(isNull(c.getString(c.getColumnIndex("gugun_nm")))) binfo.gugunNm    = ""
            else binfo.gugunNm  = c.getString(c.getColumnIndex("gugun_nm"))

            if(isNull(c.getString(c.getColumnIndex("sta_nm")))) binfo.staNm    = ""
            else binfo.staNm  = c.getString(c.getColumnIndex("sta_nm"))

            if(isNull(c.getString(c.getColumnIndex("beach_wid")))) binfo.beachWid    = ""
            else binfo.beachWid  = c.getString(c.getColumnIndex("beach_wid")).toString()

            if(isNull(c.getString(c.getColumnIndex("beach_len")))) binfo.beachLen    = ""
            else binfo.beachLen  = c.getString(c.getColumnIndex("beach_len")).toString()

            if(isNull(c.getString(c.getColumnIndex("beach_knd")))) binfo.beachKnd    = ""
            else binfo.beachKnd  = c.getString(c.getColumnIndex("beach_knd")).toString()

            if(isNull(c.getString(c.getColumnIndex("link_addr")))) binfo.linkAddr    = ""
            else binfo.linkAddr  = c.getString(c.getColumnIndex("link_addr"))

            if(isNull(c.getString(c.getColumnIndex("link_nm")))) binfo.linkNm    = ""
            else binfo.linkNm  = c.getString(c.getColumnIndex("link_nm"))

            if(isNull(c.getString(c.getColumnIndex("link_tel")))) binfo.linkTel    = ""
            else binfo.linkTel  = c.getString(c.getColumnIndex("link_tel"))

            if(isNull(c.getString(c.getColumnIndex("lat")))) binfo.blat    = ""
            else binfo.blat  = c.getString(c.getColumnIndex("lat"))

            if(isNull(c.getString(c.getColumnIndex("lon")))) binfo.blon    = ""
            else binfo.blon  = c.getString(c.getColumnIndex("lon"))

            binfos.add(binfo)
        }
    }

    inner class FavRecyclerViewAdapter : RecyclerView.Adapter<FavViewHolder>() {
        init {
            Log.d("FavRecylcerViewAdapter", "FavRecylcerViewAdapter init")
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
            val view: View =layoutInflater.inflate(R.layout.text_row_item, parent,false)
            return FavViewHolder(view)
        }

        override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
            val item = binfos[position]
            holder.setItems(item)
        }

        override fun getItemCount(): Int {
            return binfos.size
        }
    }

    inner class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setItems(item: BInfo) {

            itemView.tTitle.text = item.staNm + " 해변"
            itemView.tDetail.text = item.sidoNm + " " + item.staNm
            //BeachDetails call
            itemView.setOnClickListener{
                val context = activity as AppCompatActivity
                // Pass data to fragment
                val fragment = BeachDetails()
                val args = Bundle()
                // Send string data as key value format
                args.putString("pBeach_id", item.beachId)
                args.putString("pSido_nm", item.sidoNm)
                args.putString("pGugun_nm", item.gugunNm)
                args.putString("pSta_nm", item.staNm)
                args.putString("pBeach_wid", item.beachWid)
                args.putString("pBeach_len", item.beachLen)
                args.putString("pBeach_knd", item.beachKnd)
                args.putString("pLink_addr", item.linkAddr)
                args.putString("pLink_nm", item.linkNm)
                args.putString("pLink_tel", item.linkTel)
                args.putString("pLat", item.blat)
                args.putString("pLon", item.blon)
                fragment.arguments = args

                context.replaceFragment(fragment)
                Log.d("FavRecylcerViewAdapter", "클릭클릭2222")
            }
        }
    }

    override fun onStart() {
        Log.d("DashBoardfragment", "onStart")
        super.onStart()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onResume() {
        Log.d("DashBoardfragment", "onResume")
        super.onResume()
        selectFavList()
        recyclerView.adapter = FavRecyclerViewAdapter()
    }
}


