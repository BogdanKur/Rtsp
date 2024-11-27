package com.example.rtsp

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.rtsp.databinding.FragmentMainBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService
    private lateinit var getRes: GetDevice

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        val navController = findNavController()
        retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.144.25:3002")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
        binding.btnGet.setOnClickListener{
            apiService.getVideoDevices().enqueue(object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful) {
                        val currentResponse = response.body()!!
                        getRes.vidDeviceSelected = currentResponse.get("vidDeviceSelected") as SelectedRes
                        getRes.retDevices = currentResponse.get("label") as List<SelectedRes>
                        getRes.vidResSelected = currentResponse.get("caps") as Caps
                        val listOfNamesDevices = mutableListOf<String>()
                        for(device in getRes.retDevices) {
                            listOfNamesDevices.add(device.label)
                        }
                        val spinnerDevice = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOfNamesDevices)
                        binding.spinDevice.adapter = spinnerDevice
                        val listOfResolutions = mutableListOf<String>()
                        for(resolution in getRes.vidDeviceSelected.caps) {
                            listOfResolutions.add(resolution.value)
                        }
                        val spinnerResolution = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOfResolutions)
                        binding.spinResolution.adapter = spinnerResolution
                        val listOfRotation = listOf(0, 90, 180, 270)
                        val spinnerRotation = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOfRotation)
                        binding.spinRotation.adapter = spinnerRotation
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(requireContext(), "Ошибка", Toast.LENGTH_SHORT).show()
                }

            })
        }

        binding.spinDevice.setOnItemClickListener { parent, view, position, id ->
            for(device in getRes.retDevices) {
                if(device.label == id.toString()) {
                    getRes.vidDeviceSelected = device
                }
            }
            val listOfCaps = mutableListOf<String>()
            for(cap in getRes.vidDeviceSelected.caps) {
                listOfCaps.add(cap.value)
            }
            val spinnerResolution = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOfCaps)
            binding.spinResolution.adapter = spinnerResolution
        }
        binding.spinResolution.setOnItemClickListener{parent, view, position, id ->

        }

        binding.btnSend.setOnClickListener {
        }

        return view
    }
}