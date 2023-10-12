package com.mohsen.raptorconnect.data.connection

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import com.mohsen.raptorconnect.isLocationPermissionsGranted
import com.mohsen.raptorconnect.requestLocationPermissions

class WifiDirectReceiver(
    private val wifiP2pManager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel,
    private val manager: WifiDirectManager
) : BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    Log.d("TAG", "Wi-Fi P2P is enabled")
                } else {
                    Log.d("TAG", "Wi-Fi P2P is disabled")
                }
            }

            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
//                if (isLocationPermissionsGranted(context!!)) {
                    wifiP2pManager.requestPeers(channel) { peers: WifiP2pDeviceList ->
                        val deviceList = peers.deviceList.toList()
                        Log.e("TAG", "onReceive: ${deviceList.size}" )
                        deviceList.forEach {
                            Log.e("TAG", "onReceive: ${it.deviceName}" )
                        }
                    }
//                } else {
//                    requestLocationPermissions(context)
//                }
            }
        }
    }
}