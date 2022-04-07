package org.techtown.trip_app.Map

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "cffa6411d81c5b6be54ce55bf4bbae1c")
    }
}