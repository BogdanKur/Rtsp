package com.example.rtsp

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/get_videodevices")
    fun getVideoDevices(): Call<JsonObject>
    @POST("/startstopvideo")
    fun sendVideoData(currentDevice: Device)
}