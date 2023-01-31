package com.elvinliang

import android.content.Context
import android.net.Network
import com.elvinliang.aviation.BuildConfig
import com.elvinliang.aviation.Config
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader
import javax.inject.Inject

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