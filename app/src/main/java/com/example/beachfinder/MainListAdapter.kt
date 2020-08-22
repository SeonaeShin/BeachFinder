package com.example.beachfinder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class MainListAdapter(val context: Context, val beachList: ArrayList<Favorites>) : BaseAdapter()  {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */
        val view: View = LayoutInflater.from(context).inflate(R.layout.listview_item, null)

        /* 위에서 생성된 view를 res-layout-listivew_item.xml 파일의 각 View와 연결하는 과정이다. */
        val beachName = view.findViewById<TextView>(R.id.beachName)
        val beachAdd = view.findViewById<TextView>(R.id.beachAdd)

        /* ArrayList<Dog>의 변수 dog의 이미지와 데이터를 ImageView와 TextView에 담는다. */
        val beach = beachList[position]
        beachName.text = beach.beachName
        beachAdd.text = beach.beachAdd

        return view
    }

    override fun getItem(position: Int): Any {
        return beachList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return beachList.size
    }
}