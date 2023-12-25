package com.example.finalproject.ui.home

import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.MainActivity
import com.example.finalproject.R
import com.example.finalproject.data.Place
import com.example.finalproject.databinding.FragmentHomeBinding
import com.example.finalproject.network.IParkAPIService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    val activity: MainActivity
        get() {
            TODO()
        }

    private val adapter: HomeAdapter = HomeAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

//        /** 검색결과 -> 리사이클러뷰에서 보여줌 **/
//        adapter.setOnItemClickListener(object : HomeAdapter.OnItemClickListner {
//            // 검색 결과로 도출된 결과들 중 아이템 하나 클릭 -> detailActivity로 넘어감
//            override fun onItemClick(view: View, position: Int) {
//                // 클릭 -> 책 표지 이미지 -> glider에 보여주고 있음
//                // applicationContext <= mainActivity.this 대신
//                val url = adapter.parks?.get(position)?.image
//                Glide.with(requireContext())
//                    .load(url)
//                    .into(binding.imageView)
//
//                // DetailActivity 를 호출하며 image url 을 intent 로 전달
//                val intent = Intent(requireContext(), DetailActivity::class.java)
//                intent.putExtra("url", url)
//                startActivity(intent)
//                // intent <= put.extra(표지 url) 보내줌 -> detail액티비티에 url 이 전송됨
//                // glider로 화면상에 이미지 보여줌
//            }
//        })
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.api_url))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val service = retrofit.create(IParkAPIService::class.java)

        binding.btnSearch.setOnClickListener {
            val keyword = binding.etKeyword.text.toString()

            val apiCallback = object : Callback<Place> {
                override fun onResponse(call: Call<Place>, response: Response<Place>) {
                    if (response.isSuccessful) {
                        val root: Place? = response.body()
                        adapter.places = root?.body?.items?.item
                        adapter.notifyDataSetChanged()

                    } else {
                        Log.d(TAG, "Unsuccessful Response")
                        Log.d(TAG, response.errorBody()!!.string())     // 응답 오류가 있을 때 상세정보 확인
                    }
                    
                }

                override fun onFailure(call: Call<Place>, t: Throwable) {
                    Log.d(TAG, "OpenAPI Call Failure ${t.message}")
                }
            }

            val apiCall: Call<Place> = service.getPlaceResult(
                resources.getString(R.string.api_key),
                keyword,
                "application/json"
            )

            apiCall.enqueue(apiCallback)

        }
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}