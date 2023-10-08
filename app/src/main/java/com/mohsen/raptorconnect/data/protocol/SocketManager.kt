package com.mohsen.raptorconnect.data.protocol

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

object SocketManager {
    private val SERVER_URL = "آدرس سرور Socket.IO"

    private var mSocket: Socket

    init {
        val options = IO.Options()
        options.forceNew = true
        options.reconnection = true
        options.reconnectionAttempts = 10
        options.reconnectionDelay = 5000
        options.timeout = 5000
        mSocket = IO.socket(SERVER_URL, options)
    }

    fun connect() = mSocket.connect()


    fun disconnect() = mSocket.disconnect()


    fun isActive() = mSocket.isActive


    fun sendMessage(message: String) = mSocket.emit("message", message)


    fun setOnMessageListener(listener: (String) -> Unit) {
        mSocket.on("message") { args ->
            listener(args[0].toString())
        }
    }


}