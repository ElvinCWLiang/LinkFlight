package com.elvinliang.aviation

import android.content.Context
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader

object FlipperInitializer {
    fun init(context: Context) {
        SoLoader.init(context, false)
        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(context)) {
            val client = AndroidFlipperClient.getInstance(context)
            client.addPlugin(NetworkFlipperPlugin())
            client.addPlugin(InspectorFlipperPlugin(context, DescriptorMapping.withDefaults()))
            client.start()
        }
    }
}
