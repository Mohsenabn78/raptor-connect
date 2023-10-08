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
        mSocket = IO.socket(SERVER_URL, options)
    }

    fun connect() {
        mSocket.connect()
    }

    fun disconnect() {
        mSocket.disconnect()
    }

    fun sendMessage(message: String) {
        mSocket.emit("message", message)
    }

    fun setOnMessageListener(listener: (String) -> Unit) {
        mSocket.on("message") { args ->
            listener(args[0].toString())
        }
    }


}