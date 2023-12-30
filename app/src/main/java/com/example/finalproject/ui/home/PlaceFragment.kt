package com.example.finalproject.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.R
import com.example.finalproject.data.Root
import com.example.finalproject.databinding.FragmentHomeBinding
import com.example.finalproject.network.IParkAPIService
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlaceFragment : Fragment() {
    private val TAG = "PlaceFragment"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapter: PlaceAdapter = PlaceAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val placeViewModel =
            ViewModelProvider(this).get(PlaceViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        placeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.rvPlaces.adapter = adapter
        binding.rvPlaces.layoutManager = LinearLayoutManager(requireContext())

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.api_url))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val service = retrofit.create(IParkAPIService::class.java)

        binding.btnSearch.setOnClickListener {
            val keyword = binding.etKeyword.text.toString()

            val apiCallback = object : Callback<Root> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<Root>, response: Response<Root>) {
                    Log.d(TAG, "Raw Response: ${response.raw().toString()}")
                    Log.d(TAG, "JSON String: ${response.body()}")

                    try {
                        if (response.isSuccessful) {
                            val root: Root? = response.body()
                            adapter.places = root?.response?.body?.items?.item
                            adapter.notifyDataSetChanged()

                        } else {
                            Log.d(TAG, "Unsuccessful Response")
                        }

                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing response", e)
                    }
                }

                override fun onFailure(call: Call<Root>, t: Throwable) {
                    Log.e(TAG, "OpenAPI Call Failure", t)
                    Log.e(TAG, "Error message: ${t.message}")
                    Log.e(TAG, "Error stack trace: ${Log.getStackTraceString(t)}")
                }
            }

            val apiCall: Call<Root> = service.getPlaceResult(
                resources.getString(R.string.api_key),
                keyword
            )

            apiCall.enqueue(apiCallback)
        }


        adapter.setOnItemClickListener(object : PlaceAdapter.OnItemClickListner {
            override fun onItemClick(view: View, position: Int) {
                val title = adapter.places?.get(position)?.title
                val address = adapter.places?.get(position)?.address
                val information = adapter.places?.get(position)?.information

                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("title", title)
                intent.putExtra("address", address)
                intent.putExtra("information", information)

                startActivity(intent)
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
