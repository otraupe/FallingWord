package com.otraupe.fallingwords

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class FallingWordsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(object: Timber.DebugTree() {
                // add prefix to log tags
                override fun log(
                    priority: Int, tag: String?, message: String, t: Throwable?
                ) {
                    super.log(priority, "t-$tag", message, t)
                }
                // add method name to log entries
                override fun createStackElementTag(element: StackTraceElement): String {
                    return String.format(
                        "%s:%s",
                        super.createStackElementTag(element),
                        element.methodName
                    )
                }
            })
        }
    }
}