package com.mohsen.raptorconnect

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

tailrec fun Context.activity(): Activity? = when {
    this is Activity -> this
    else -> (this as? ContextWrapper)?.baseContext?.activity()
}