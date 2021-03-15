package com.lazyee.example

import android.app.Activity
import android.app.Application
import com.lazyee.login.interceptor.LoginInterceptor
import com.lazyee.login.interceptor.LoginInterceptorCallback

/**
 * @Author leeorz
 * @Date 3/15/21-5:35 PM
 * @Description:
 */
class MyApplication :Application(){
    override fun onCreate() {
        super.onCreate()

        LoginInterceptor.init(object :LoginInterceptorCallback{
            override fun isLogin(): Boolean {
                return MainActivity.isLogin
            }

            override fun getLoginPageActivity(): Class<out Activity> {
                return LoginActivity::class.java
            }
        })
    }
}