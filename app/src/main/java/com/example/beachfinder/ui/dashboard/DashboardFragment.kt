package com.example.beachfinder.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.beachfinder.Favorites
import com.example.beachfinder.MainListAdapter
import com.example.beachfinder.R
import com.google.android.gms.maps.MapView
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : Fragment() {

    var favList = arrayListOf<Favorites>(
        Favorites("만리포", "서해"),
        Favorites("양양", "강원도"),
        Favorites("고성", "강원도"),
        Favorites("삼포", "강원도"),
        Favorites("만리포", "서해"),
        Favorites("양양", "강원도"),
        Favorites("고성", "강원도"),
        Favorites("만리포", "서해"),
        Favorites("양양", "강원도"),
        Favorites("고성", "강원도"),
        Favorites("만리포", "서해"),
        Favorites("양양", "강원도"),
        Favorites("고성", "강원도"),
        Favorites("만리포", "서해"),
        Favorites("양양", "강원도"),
        Favorites("고성", "강원도")
    )

    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        Log.d("favList is >> ","${favList[0].beachAdd},${favList[0].beachName}")
//      Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_dashboard, container, false)

        listView = rootView.findViewById(R.id.mainListView) as ListView
        val beachAdapter = activity?.let { MainListAdapter(it, favList) }
        listView.adapter = beachAdapter

        return rootView
    }


//    private lateinit var listview: ListView
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View?
//    {
//        // Inflate the layout for this fragment
//        var rootView = inflater.inflate(R.layout.fragment_dashboard, container, false)
//
//        //리스트에 들어갈 아이템 생성
//        val item = Array(20,{ i -> "$i + list" })
//
//        listview = rootView.findViewById(R.id.listView) as ListView
//
//        // 자동완성 텍스트뷰에서 사용하는 어댑터 추가
//        val listViewAdapter = activity?.let {
//            ArrayAdapter<String>(
//                it, android.R.layout.simple_list_item_1, item
//            )
//        }
//
//        listview.setAdapter(listViewAdapter)
//
//        Log.d("rootView", "${rootView}")
//
//        return rootView
//    }

    override fun onStart() {
        Log.d("DashBoardfragment", "onStart")
        super.onStart()
    }
}