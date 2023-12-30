package com.example.finalproject.ui.slideshow

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.data.PlaceDao
import com.example.finalproject.data.PlaceDatabase
import com.example.finalproject.databinding.ActivitySavedPlaceDetailBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

public class SavedPlaceDetailActivity : AppCompatActivity() {

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

    private var onMapReadyActions: (() -> Unit)? = null

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

        // 지도 관련
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this, Locale.getDefault())
        getLastLocation()   // 최종위치 확인

        if (information != null) {
            // 지도의 위치를 저장한 장소로 지정
            val location = getLocationFromAddress(information)
            Log.d(TAG, "$location 위치는 여기")
            if (location != null) {
                performActionsWhenMapReady {
                    moveCameraToLocation(location)
                }
            } else {
                Toast.makeText(this, "지도 위치를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        db = PlaceDatabase.getDatabase(this)
        placeDao = db.placeDao()

        showData("Geocoder isEnabled: ${Geocoder.isPresent()}")

        savedPlaceBinding.btnBack.setOnClickListener {
            finish()
        }
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapViewHome) as? SupportMapFragment
        mapFragment?.getMapAsync(mapReadyCallback)
    }

    /* GoogleMap 로딩이 완료될 경우 실행하는 Callback */
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

            onMapReadyActions?.invoke()
            onMapReadyActions = null // 실행 후 초기화
        }
    }

    private fun performActionsWhenMapReady(actions: () -> Unit) {
        if (::googleMap.isInitialized) {
            // 이미 GoogleMap이 준비된 경우
            actions.invoke()
        } else {
            // GoogleMap이 준비되지 않은 경우
            // GoogleMap이 준비된 후에 실행해야 하는 작업을 등록
            onMapReadyActions = actions
        }
    }
    fun removeParentheses(input: String): String {
        // 정규식을 사용하여 괄호와 괄호 안의 내용을 제거합니다.
        val regex = Regex("\\([^)]+\\)")
        return input.replace(regex, "").trim()
    }

    private fun getLocationFromAddress(address: String): LatLng? {
        return try {
            val addr = removeParentheses(address)
            Log.d(TAG, "$addr 를 찾고 있습니다.")
            val locationList = geocoder.getFromLocationName(addr, 10)

            if (locationList.isNullOrEmpty()) {
                Log.d(TAG, "주소에서 위치를 찾을 수 없습니다.")
                return null
            }

            val foundAddress: Address = locationList[0]
            val lat: Double = foundAddress.latitude
            val lon: Double = foundAddress.longitude
            LatLng(lat, lon)
        } catch (e: Exception) {
            Log.e(TAG, "주소에서 위치를 찾는 중 오류 발생: ${e.message}")
            e.printStackTrace()
            null
        }
    }


    private fun moveCameraToLocation(location: LatLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17F))
    }

    /*마커 추가*/
    fun addMarker(targetLoc: LatLng, s: String) {  // LatLng(37.606320, 127.041808)
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
    @SuppressLint("MissingPermission")
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

            // 마지막 위치 확인 후에 호출되는 함수
            callExternalMap()
        }
        fusedLocationClient.lastLocation.addOnFailureListener { e: Exception ->
            Log.d(TAG, e.toString())
        }
    }

    private fun callExternalMap() {
        val locLatLng =
            String.format("geo:%f,%f?z=%d", 37.606320, 127.041808, 17)
        val locName =
            "https://www.google.co.kr/maps/place/" + "Hawolgok-dong"
        val route =
            String.format(
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