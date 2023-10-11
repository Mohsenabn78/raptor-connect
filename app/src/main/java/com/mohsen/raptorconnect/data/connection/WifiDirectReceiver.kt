package com.mohsen.raptorconnect.data.connection

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log

class WifiDirectReceiver(
    private val wifiP2pManager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel,
    private val manager: WifiDirectManager
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    Log.d("WifiDirect", "Wi-Fi P2P is enabled")
                } else {
                    Log.d("WifiDirect", "Wi-Fi P2P is disabled")
                }
            }

            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                // لیست دستگاه‌های متصل به LAN تغییر کرده است
                wifiP2pManager.requestPeers(channel) { peers: WifiP2pDeviceList ->
                    val deviceList = peers.deviceList.toList()
                    deviceList.forEach {
                        Log.e("TAG", "onReceive: ${it.deviceName}" )
                    }

                    // دستگاه‌ها در لیست `deviceList` در دسترس هستند
                }
            }
        }
    }
}