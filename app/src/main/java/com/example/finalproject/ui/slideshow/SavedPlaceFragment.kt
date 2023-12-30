package com.example.finalproject.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.data.PlaceDao
import com.example.finalproject.data.PlaceDatabase
import com.example.finalproject.databinding.ActivitySavedPlaceBinding
import com.example.finalproject.databinding.ActivitySavedPlaceDetailBinding
import com.example.finalproject.ui.home.PlaceAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedPlaceFragment : Fragment() {

    private val TAG = "SavedPlaceFragment"
    private var _binding: ActivitySavedPlaceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val adapter: PlaceAdapter = PlaceAdapter()

    private lateinit var db: PlaceDatabase
    private lateinit var placeDao: PlaceDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        db = PlaceDatabase.getDatabase(requireContext())
        placeDao = db.placeDao()

        _binding = ActivitySavedPlaceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvPlaces.adapter = adapter
        binding.rvPlaces.layoutManager = LinearLayoutManager(requireContext())

        getPlaces()

        adapter.setOnItemClickListener(object : PlaceAdapter.OnItemClickListner {
            override fun onItemClick(view: View, position: Int) {
                val title = adapter.places?.get(position)?.title
                val address = adapter.places?.get(position)?.address
                val information = adapter.places?.get(position)?.information

                val intent = Intent(requireContext(), SavedPlaceDetailActivity::class.java)
                intent.putExtra("title", title)
                intent.putExtra("address", address)
                intent.putExtra("information", information)

                startActivity(intent)
            }
        })

        return root
    }

    private fun getPlaces() {
        CoroutineScope(Dispatchers.IO).launch {
            val places = placeDao.getAllPlaces()
            // UI 업데이트나 다른 작업을 수행하려면 withContext(Dispatchers.Main)을 사용합니다.
            withContext(Dispatchers.Main) {
                // 결과 처리 또는 UI 업데이트 작업 수행
                adapter.setData(places)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}