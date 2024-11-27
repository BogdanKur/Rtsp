package com.example.rtsp

data class GetDevice (
    var vidDeviceSelected: SelectedRes,
    var retDevices: List<SelectedRes>,
    var vidResSelected: Caps
)