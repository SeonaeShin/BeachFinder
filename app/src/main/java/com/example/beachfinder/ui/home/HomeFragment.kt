package com.example.beachfinder.ui.home

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.text.Layout
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.beachfinder.*
import com.example.beachfinder.ui.notifications.NotificationsFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.search_bar.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import java.util.*

class HomeFragment : Fragment(), OnMapReadyCallback {

    //공공 데이타 해수욕
    val API_KEY =
        "FmusGj5BSKLjzF%2Fjx7BvG9j6KFKJGJrE8bdVgFT9VDtyzqub2VdSOKkPbv1%2B4rp8R7WZQRGeK1%2BugytqL06HFQ%3D%3D"

    //해 정보 집합을 저장할 array변수. 검색을 위해 저장
    var beaches = JSONArray()

    //앱이 비활성화 될 떄 백그라운드 작업도 취소하기 위한 변수
    var task: HomeFragment.BeachReadTask? = null

    // ClusterManager 변수 선언
    var clusterManager: ClusterManager<MyItem>? = null
    // ClusterRenderer 변수 선언
    var clusterRenderer: ClusterRenderer? = null

    // JsonObject 를 키로 MyItem 객체를 저장할 맵
    val itemMap = mutableMapOf<JSONObject, MyItem>()


    private lateinit var mapView: MapView

//    val context = activity as AppCompatActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_home, container, false)

        mapView = rootView.findViewById(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        Log.d("rootView", "${rootView}")

        return rootView

    }

    // 화장실 데이터를 읽어오는 AsyncTask
    inner class BeachReadTask : AsyncTask<Void, JSONArray, String>() {
        // 데이터를 읽기 전에 기존 데이터 초기화
        override fun onPreExecute() {

            // 화장실 정보 초기화
            beaches = JSONArray()
            // itemMap 변수 초기화
            itemMap.clear()

        }


        override fun doInBackground(vararg params: Void?): String {
            var totalCount = 0
            val provinces:Array<String> = arrayOf("%EA%B0%95%EC%9B%90","%EA%B2%BD%EB%82%A8","%EA%B2%BD%EB%B6%81","%EB%B6%80%EC%82%B0","%EC%9D%B8%EC%B2%9C","%EC%9A%B8%EC%82%B0","%EC%A0%84%EB%82%A8","%EC%A0%9C%EC%A3%BC","%EC%A0%84%EB%B6%81","%EC%B6%A9%EB%B6%81","%EC%B6%A9%EB%82%A8")
            //("강원","경남","경북","부산","울산","인천","전남","전북","제주","충북","충남")
            do {
                // 백그라운드 작업이 취소된 경우 루프를 빠져나간다.
                if (isCancelled) break
                //provinces갯수만큼 포문 돌린다
                for(item in provinces){
                    Log.d("display provinces -----", "${item}")
                    val jsonObject = readData(item)
                    Log.d("display provinces okok", "display provinces okok")
                    // totalCount 를 가져온다.
                    totalCount = jsonObject.getJSONObject("getOceansBeachInfo").getInt("totalCount")
                    // 화장실 정보 데이터 집합을 가져온다.
                    val rows = jsonObject.getJSONObject("getOceansBeachInfo").getJSONArray("item")
                    // 기존에 읽은 데이터와 병합
                    beaches.merge(rows)
                    // UI 업데이트를 위해 progress 발행
                    publishProgress(rows)
                    //totalCount
                    totalCount += totalCount
                }

            } while (false)
            return "complete"
        }

        // 데이터를 읽어올때마다 중간중간 실행
        override fun onProgressUpdate(vararg values: JSONArray?) {
            // vararg 는 JSONArray 파라미터를 가변적으로 전달하도록 하는 키워드
            // 인덱스 0의 데이터를 사용
            val array = values[0]
            array?.let {
                for (i in 0 until array.length()) {
                    // 마커 추가
                    addMarkers(array.getJSONObject(i))
                }
            }
            // clusterManager 의 클러스터링 실행
            clusterManager?.cluster()
        }

        // 백그라운드 작업이 완료된 후 실행
        override fun onPostExecute(result: String?) {
            // 자동완성 텍스트뷰(AutoCompleteTextView) 에서 사용할 텍스트 리스트
            val textList = mutableListOf<String>()
            // 모든 화장실의 이름을 텍스트 리스트에 추가
            for (i in 0 until beaches.length()) {
                val beach = beaches.getJSONObject(i)
                textList.add(beach.getString("sta_nm"))
            }
            // 자동완성 텍스트뷰에서 사용하는 어댑터 추가
            val adapter = activity?.let {
                ArrayAdapter<String>(
                    it, android.R.layout.simple_dropdown_item_1line, textList
                )
            }
            // 자동완성이 시작되는 글자수 지정
            searchBar.autoCompleteTextView.threshold = 1
            // autoCompleteTextView 의 어댑터를 상단에서 만든 어댑터로 지정
            searchBar.autoCompleteTextView.setAdapter(adapter)
        }
    }


    // JSONArray 에서 원소의 속성으로 원소를 검색.
    // propertyName: 속성이름
    // value: 값
    fun JSONArray.findByChildProperty(propertyName: String, value: String): JSONObject? {
        // JSONArray 를 순회하면서 각 JSONObject 의 프로퍼티의 값이 같은지 확인
        for (i in 0 until length()) {
            val obj = getJSONObject(i)
            if (value == obj.getString(propertyName)) return obj
        }
        return null
    }

    // 마커를 추가하는 함수
    fun addMarkers(beach: JSONObject) : Boolean {
        try{
            //정보가 하나라도 없으면 해당 정보는 보여주지 않는다
            if(beach.isNull("lat")||beach.isNull("lon")||beach.isNull("gugun_nm")||beach.isNull("sta_nm"))
                return false;

            var lat = beach.getDouble("lat")
            var lon = beach.getDouble("lon")
            var title = beach.getString("gugun_nm")
            var snippet = beach.getString("sta_nm")

            Log.d("----------------------",   "${lat}"+ " ," + "${lon}"+ " ," + "${title}"+" ," + "${snippet}")

            val item = MyItem(
                LatLng(lat, lon), title, snippet, BitmapDescriptorFactory.fromBitmap(bitmap)
            )

            // clusterManager 를 이용해 마커 추가
            clusterManager?.addItem(
                MyItem(
                    LatLng(lat, lon), title, snippet, BitmapDescriptorFactory.fromBitmap(bitmap)
                )
            )

            // 아이템맵에 beach 객체를 키로 MyItem 객체 저장
            itemMap.put(beach, item)

        } catch ( e: JSONException ){
            Log.d("error occurs",   "${beach}}")
            return false
        }

        return true
    }



    override fun onMapReady(googleMap: GoogleMap) {

        // 구글맵 마커 초기화
        googleMap?.clear()

        val context = activity as AppCompatActivity

        // 맵뷰에서 구글 맵을 불러오는 함수. 컬백함수에서 구글 맵 객체가 전달됨
        mapView.getMapAsync {
            // ClusterManager 객체 초기화
            clusterManager = ClusterManager(getActivity(), it)
            clusterRenderer = ClusterRenderer(getActivity(), it, clusterManager)

            // OnCameraIdleListener 와 OnMarkerClickListener 를 clusterManager 로 지정
            it.setOnCameraIdleListener(clusterManager)
            it.setOnMarkerClickListener(clusterManager)
            it.setOnInfoWindowClickListener {

                // Pass data to fragment
                val args = Bundle()
                for (i in 0 until beaches.length()) {
                    var sta_nm = beaches.getJSONObject(i).getString("sta_nm")
//                    val item = beaches.getString(i)
//                    val jObject: JSONObject = JSONObject(item)
                    if(sta_nm == it.snippet)
                    {
                        Log.d("success!","${sta_nm}, ${it.title}, ${it.snippet}")
                        // Send string data as key value format
                        args.putString("passBName", beaches.getJSONObject(i).getString("sta_nm"))
                        args.putString("passBAdd", beaches.getJSONObject(i).getString("gugun_nm"))
                        break
                    }
                }

                val fragment = BeachDetails()

                fragment.arguments = args

                context.replaceFragment(fragment)

//                searchBar.visibility = View.GONE

                Log.d("HomeFragment", "searchBar22 status> ${searchBar.visibility}")

            }
            // 현재위치로 카메라 이동
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(34.315187, 126.518598), 17f))

        }

        task?.cancel(true)
        task = BeachReadTask()
        task?.execute()
        // searchBar 의 검색 아이콘의 이벤트 리스너 설정
        searchBar.imageView.setOnClickListener {
            Log.d("---------------------- ","listener logic in inlistener logic in in")
            // autoCompleteTextView 의 텍스트를 읽어 키워드로 가져옴
            val keyword = searchBar.autoCompleteTextView.text.toString()
            Log.d("---------------------- ","${keyword}")
            // 키워드 값이 없으면 그대로 리턴
            if (TextUtils.isEmpty(keyword)) {
                Log.d("nothing!!!", "${keyword}")
                return@setOnClickListener
            }
            // 검색 키워드에 해당하는 JSONObject 를 찾는다.
            beaches.findByChildProperty("sta_nm", keyword)?.let {
                // itemMap 에서 JSONObject 를 키로 가진 MyItem 객체를 가져온다.
                val myItem = itemMap[it]
                // ClusterRenderer 에서 myItem 을 기반으로 마커를 검색한다.
                // myItem 은 위도,경도,제목,설명 속성이 같으면 같은 객체로 취급됨
                val marker = clusterRenderer?.getMarker(myItem)
                // 마커에 인포 윈도우를 보여준다
                marker?.showInfoWindow()
                // 마커의 위치로 맵의 카메라를 이동한다.
                googleMap?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(it.getDouble("lat"), it.getDouble("lon")), 17f
                    )
                )
                clusterManager?.cluster()
            }

            // 검색 텍스트뷰의 텍스트를 지운다.
            searchBar.autoCompleteTextView.setText("")
        }
    }

    override fun onStart() {
        Log.d("====Map===", "onStart")
//        searchBar.visibility = View.VISIBLE
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        Log.d("====Map===", "onStop")

        super.onStop()
        mapView.onStop()
        task?.cancel(true)
        task = null

    }

    override fun onResume() {

        Log.d("====Map===", "onResume")
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        Log.d("====Map===", "onPause")

//
//        val fragmentManager = supportFragmentManager
//        val transaction = fragmentManager.beginTransaction()
//
//        transaction.remove(fragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
        super.onPause()

        mapView.onPause()

    }

    override fun onLowMemory() {
        Log.d("====Map===", "onLowMemory")
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {

        Log.d("=onSaveInstanceState=", "onSaveInstanceState")
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        Log.d("=====onDestroy===", "onDestroy")
        mapView.onDestroy()
        super.onDestroy()
    }


    //맵 위에 핀으로 사용할 비트맵
    val bitmap by lazy {
        val drawable = resources.getDrawable(R.drawable.pin_sign) as BitmapDrawable
        Bitmap.createScaledBitmap(drawable.bitmap, 40, 70, false)
    }

    fun JSONArray.merge(anotherArray: JSONArray) {
        for (i in 0 until anotherArray.length()) {
            this.put(anotherArray.get(i))
        }
    }

    // 화장실 정보를 읽어와 JSONObject 로 반환하는 함수
    fun readData(province: String): JSONObject {

        Log.d("readData start", "readData start")
        val url =
            URL("http://apis.data.go.kr/1192000/OceansBeachInfoService/getOceansBeachInfo?SIDO_NM=" + "${province}" + "&numOfRows=100&resultType=JSON&ServiceKey=" + "${API_KEY}")
        Log.d(" before readData ", "before readData ")
        val connection = url.openConnection()
        Log.d("readData :conn", "${connection}")
        val data = connection.getInputStream().readBytes().toString(charset("UTF-8"))

        Log.d("readData finish:data", "${data}")
        return JSONObject(data)
    }


}