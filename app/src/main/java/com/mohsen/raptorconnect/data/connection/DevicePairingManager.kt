package com.mohsen.raptorconnect.data.connection

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.os.Looper
import android.util.Log

class DevicePairingManager(context: Context) {

    private val wifiP2pManager: WifiP2pManager = context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    private val channel: WifiP2pManager.Channel = wifiP2pManager.initialize(context, Looper.getMainLooper(), null)
    private val receiver: BroadcastReceiver

    init {
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                        // بررسی وضعیت Wi-Fi P2P
                        val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                        if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                            Log.e("TAG", "Wi-Fi P2P فعال است" )
                        } else {
                            Log.e("TAG", "Wi-Fi P2P غیرفعال است" )
                        }
                    }

                    WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                        // لیست دستگاه‌های متصل به LAN تغییر کرده است
                        wifiP2pManager.requestPeers(channel) { peers: WifiP2pDeviceList ->
                            // در اینجا لیست دستگاه‌ها در peers قابل دسترسی است
                            val deviceList = peers.deviceList
                            // نمایش دستگاه‌های متصل به LAN
                            val deviceNames = deviceList.map { it.deviceName }.toTypedArray()
                            // نمایش لیست دستگاه‌ها به کاربر
                        }
                    }

                    WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                        // وضعیت اتصال تغییر کرده است
                    }

                    WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                        // وضعیت دستگاه شما تغییر کرده است
                    }
                }
            }
        }

        context.registerReceiver(receiver, intentFilter)
    }

    fun discoverPeers() {
        wifiP2pManager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Log.e("TAG", " جستجوی دستگاهها با موفقیت شروع شد" )
            }

            override fun onFailure(reasonCode: Int) {
                Log.e("TAG", " خطا در شروع جستجو" )
            }
        })
    }

    fun stopPeerDiscovery() {
        wifiP2pManager.stopPeerDiscovery(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Log.e("TAG", "جستجوی دستگاهها متوقف شد" )
            }

            override fun onFailure(reasonCode: Int) {
                Log.e("TAG", " خطا در متوقف کردن جستجو " )
            }
        })
    }

    fun connectToDevice(device: WifiP2pDevice) {
        val config = WifiP2pConfig()
        config.deviceAddress = device.deviceAddress
        config.wps.setup = WpsInfo.PBC  // روش اتصال به دستگاه را تنظیم کنید
        wifiP2pManager.connect(channel, config, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Log.e("TAG", "اتصال با دستگاه با موفقیت برقرار شد" )
            }

            override fun onFailure(reasonCode: Int) {
                Log.e("TAG", " خطا در اتصال به دستگاه" )
            }
        })
    }

}