package com.mohsen.raptorconnect.data.connection


import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.mohsen.raptorconnect.isLocationPermissionsGranted
import com.mohsen.raptorconnect.requestLocationPermissions

class WifiDirectManager(private val context: Context) {

    private val wifiP2pManager: WifiP2pManager by lazy {
        context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    }

    private val channel: WifiP2pManager.Channel by lazy {
        wifiP2pManager.initialize(context, Looper.getMainLooper(), null)
    }

    private val intentFilter: IntentFilter by lazy {
        val filter = IntentFilter()
        filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        filter
    }

    private val receiver: WifiDirectReceiver by lazy {
        WifiDirectReceiver(wifiP2pManager, channel, this)
    }

    fun registerReceiver() {
        ContextCompat.registerReceiver(context, receiver, intentFilter, ContextCompat.RECEIVER_EXPORTED)
    }

    fun unregisterReceiver() {
        context.unregisterReceiver(receiver)
    }

    @SuppressLint("MissingPermission")
    fun discoverPeers(context: Context) {
        if (isLocationPermissionsGranted(context)) {
            wifiP2pManager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Log.d("TAG", "Discovery initiated")
                    wifiP2pManager.requestPeers(channel) { peers: WifiP2pDeviceList ->
                        val deviceList = peers.deviceList.toList()
                        deviceList.forEach {
                            Log.e("TAG", "onReceive: ${it.deviceName}" )
                        }
                    }
                }

                override fun onFailure(reason: Int) {
                    Log.e("TAG", "Discovery failed. Reason: $reason")
                }
            })
        } else {
            requestLocationPermissions(context)
        }
    }

    @SuppressLint("MissingPermission")
    fun connect(device: WifiP2pDevice, context: Context) {
        if (isLocationPermissionsGranted(context)) {
            val config = WifiP2pConfig()
            config.deviceAddress = device.deviceAddress
            wifiP2pManager.connect(channel, config, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Log.d("TAG", "Connection initiated to ${device.deviceName}")
                }

                override fun onFailure(reason: Int) {
                    Log.e("TAG", "Connection failed. Reason: $reason")
                }
            })
        } else {
            requestLocationPermissions(context)
        }
    }
}