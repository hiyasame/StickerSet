package team.redrock.rain.stickerset

import team.redrock.rain.lib.common.BaseApp

class App : BaseApp() {
    override fun onCreate() {
        super.onCreate()
        version = BuildConfig.VERSION_NAME
    }
}