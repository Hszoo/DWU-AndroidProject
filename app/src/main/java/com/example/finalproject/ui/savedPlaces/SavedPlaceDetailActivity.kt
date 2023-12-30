package com.example.finalproject.ui.slideshow

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.data.PlaceDao
import com.example.finalproject.data.PlaceDatabase
import com.example.finalproject.ui.savedPlaces.SlideshowViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.databinding.ActivitySavedPlaceDetailBinding

public class SavedPlaceDetailActivity : AppCompatActivity()  {

    private val TAG = "SavedPlacesDetailActivity"
    private val savedPlaceBinding by lazy {
        ActivitySavedPlaceDetailBinding.inflate(layoutInflater)
    }

    private lateinit var db: PlaceDatabase
    private lateinit var placeDao: PlaceDao

    /* 지도 관련 */
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private lateinit var currentLoc: Location

    /* 구글 맵 타입의 멤버 변수로 제도를 저장할 멤버 변수 선언 */
    private lateinit var googleMap: GoogleMap
    var centerMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(savedPlaceBinding.root)

        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        val title = intent.getStringExtra("title")
        val address = intent.getStringExtra("address")
        val information = intent.getStringExtra("information")

        savedPlaceBinding.tvTitle.text = title
        savedPlaceBinding.tvInfo.text = address
        savedPlaceBinding.tvAddr.text = information

        db = PlaceDatabase.getDatabase(this)
        placeDao = db.placeDao()

        // 지도 관련
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this, Locale.getDefault())
        getLastLocation()   // 최종위치 확인

        showData("Geocoder isEnabled: ${Geocoder.isPresent()}")

//        val mapFragment =
//            childFragmentManager.findFragmentById(R.id.mapViewHome) as? SupportMapFragment
//        mapFragment?.getMapAsync(mapReadyCallback)
    }

    /*GoogleMap 로딩이 완료될 경우 실행하는 Callback*/
    // 콜백 : 외부 호출 함수 (on 시작 함수)
    // 인터페이스 OnMapreadyCallbak 인터페이스 타입
    val mapReadyCallback = object : OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap) {
            googleMap = map
            Log.d(TAG, "GoogleMap is ready")
            // 마커 클릭시 false 반환 : 여기에서 이벤트처리가 끝나지 않았음
            // infoWindow까지 띄우려면 false 지정해야됨
            // true : 클릭 이벤트 처리가 여기까지
            googleMap.setOnMarkerClickListener {
                Toast.makeText(applicationContext, it.tag.toString(), Toast.LENGTH_SHORT).show()
                false
            }
            googleMap.setOnInfoWindowClickListener {
                Toast.makeText(applicationContext, it.title, Toast.LENGTH_SHORT).show()
            }
            googleMap.setOnMapClickListener {
                Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_SHORT).show()
            }
            googleMap.setOnMapLongClickListener {
                Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*마커 추가*/
    fun addMarker(targetLoc: LatLng) {  // LatLng(37.606320, 127.041808)
        val markerOptions: MarkerOptions = MarkerOptions()
        markerOptions.position(targetLoc)
            .title("마커 제목")
            .snippet("마커 말풍선")
            .icon(BitmapDescriptorFactory.defaultMarker(R.mipmap.android.toFloat()))

        centerMarker = googleMap.addMarker(markerOptions)
        centerMarker?.showInfoWindow()
        centerMarker?.tag = "database_id"

        // centerMarker?.remove() -> 삭제 가능
    }

    lateinit var polyLineOptions: PolylineOptions

    /*선 추가*/
    fun drawLine() {
        val polylineOptions = PolylineOptions()
        LatLng(37.604151, 127.042453)
        LatLng(37.605347, 127.041207)
        LatLng(37.606038, 127.041344)
        LatLng(37.606220, 127.041674)
        LatLng(37.606631, 127.041595)
        LatLng(37.606823, 127.042380)

        val line = googleMap.addPolyline(polylineOptions)
    }

    /* 지도 관련 메서드 정의 */
    private fun showData(data: String) {
//        binding.textView3.setText(binding.textView3.text.toString() + "\n${data}")
    }

    /*위치 정보 수신 시 수행할 동작을 정의하는 Callback (비동기 방식) */
    val locCallback: LocationCallback = object : LocationCallback() {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onLocationResult(locResult: LocationResult) {
            currentLoc = locResult.locations.get(0)
            geocoder.getFromLocation(currentLoc.latitude, currentLoc.longitude, 5) { addresses ->
                CoroutineScope(Dispatchers.Main).launch {
                    showData("위도: ${currentLoc.latitude}, 경도: ${currentLoc.longitude}")
                    showData(addresses?.get(0)?.getAddressLine(0).toString())
                }
            }
            val targetLoc: LatLng = LatLng(currentLoc.latitude, currentLoc.longitude)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLoc, 17F))

            // polylineoption : 멤버 변수로 선언해서 add 해서 사용
            // polyolineOption.add 위도 경도
            // googleMap -> polylinoption 추가
        }
    }

    /*위치 정보 수신 설정*/
    val locRequest = LocationRequest.Builder(5000)
        .setMinUpdateIntervalMillis(3000)
        .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        .build()

    private fun startLocUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locRequest,     // LocationRequest 객체
            locCallback,    // LocationCallback 객체
            Looper.getMainLooper()  // System 메시지 수신 Looper
        )
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locCallback)
    }

    /*LBSTest 관련*/
    //    최종위치 확인
    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                showData(location.toString())
                currentLoc = location   // 현재 위치가 있는 경우에 반환
            } else {
                currentLoc = Location("기본 위치")      // Last Location 이 null 경우 기본으로 설정
                currentLoc.latitude = 37.606816
                currentLoc.longitude = 127.042383
            }
        }
        fusedLocationClient.lastLocation.addOnFailureListener { e: Exception ->
            Log.d(TAG, e.toString())
        }

        fun callExternalMap() {
            val locLatLng   // 위도/경도 정보로 지도 요청 시
                    = String.format("geo:%f,%f?z=%d", 37.606320, 127.041808, 17)
            val locName     // 위치명으로 지도 요청 시
                    = "https://www.google.co.kr/maps/place/" + "Hawolgok-dong"
            val route       // 출발-도착 정보 요청 시
                    = String.format(
                "https://www.google.co.kr/maps?saddr=%f,%f&daddr=%f,%f",
                37.606320, 127.041808, 37.601925, 127.041530
            )
            val uri = Uri.parse(locLatLng)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }



        /*registerForActivityResult 는 startActivityForResult() 대체*/
        val locationPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        showData("FINE_LOCATION is granted")
                    }

                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                        showData("COARSE_LOCATION is granted")
                    }

                    else -> {
                        showData("Location permissions are required")
                    }
                }
            }

        fun checkPermissions() {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            )  {
                showData("Permissions are already granted")  // textView에 출력
            } else {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }

    }
}