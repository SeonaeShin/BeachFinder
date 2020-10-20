package com.example.beachfinder.ui.dashboard

import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beachfinder.*
import com.example.beachfinder.ui.dashboard.DashboardFragment
import com.google.android.gms.maps.model.Dash


/**
 * Provide views to RecyclerView with data from binfos.
 */
private var beach_id: String? = null
private var sido_nm: String? = null
private var gugun_nm: String? = null
private var sta_nm: String? = null
private var beach_wid: String? = null
private var beach_len: String? = null
private var beach_knd: String? = null
private var link_addr: String? = null
private var link_nm: String? = null
private var link_tel: String? = null
private var lat: String? = null
private var lon: String? = null

// Pass data to fragment
val args = Bundle()
val context = MainActivity() as AppCompatActivity
val dashboard = DashboardFragment()


class CustomAdapter(private val binfos: ArrayList<BInfo>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)   {
        val vName: TextView
        val vAddr: TextView

        init {
            // Define click listener for the ViewHolder's View.
            vName = v.findViewById(R.id.tTitle)
            vAddr = v.findViewById(R.id.tDetail)
        }
    }



    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder{
        // Create a new view.

        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        v.setOnClickListener {

            //상세페이지
            args.putString("pBeach_id", beach_id)
            args.putString("pSido_nm",sido_nm )
            args.putString("pGugun_nm", gugun_nm)
            args.putString("pSta_nm",  sta_nm)
            args.putString("pBeach_wid", beach_wid)
            args.putString("pBeach_len", beach_len)
            args.putString("pBeach_knd", beach_knd)
            args.putString("pLink_addr", link_addr)
            args.putString("pLink_nm",  link_nm)
            args.putString("pLink_tel", link_tel)
            args.putString("pLat",  lat)
            args.putString("pLon", lon)


/* temp temp
            val fragment = BeachDetails()

            fragment.arguments = args

            context.replaceFragment(fragment)
*/
            dashboard.showReadDialog()
        }

        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "")

        // Get element from your binfos at this position and replace the contents of the view
        // with that element

        beach_id = binfos[position].beachId
        sido_nm  = binfos[position].sidoNm
        gugun_nm = binfos[position].gugunNm
        sta_nm = binfos[position].staNm
        beach_wid = binfos[position].beachWid
        beach_len = binfos[position].beachLen
        beach_knd = binfos[position].beachKnd
        link_addr = binfos[position].linkAddr
        link_nm = binfos[position].linkNm
        link_tel = binfos[position].linkTel
        lat = binfos[position].blat
        lon = binfos[position].blon

        viewHolder.vName.text = sta_nm+" 해변"
        viewHolder.vAddr.text = sido_nm+ " "+sta_nm
    }

    // Return the size of your binfos (invoked by the layout manager)
    override fun getItemCount() = binfos.size

    companion object {
        private val TAG = "CustomAdapter"
    }
}



/*

 */
