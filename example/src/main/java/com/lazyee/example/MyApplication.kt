package com.lazyee.example

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import com.lazyee.login.interceptor.LoginInterceptor
import com.lazyee.login.interceptor.LoginInterceptorCallback
import com.lazyee.login.interceptor.TodoBlock

/**
 * @Author leeorz
 * @Date 3/15/21-5:35 PM
 * @Description:
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        LoginInterceptor.init(object : LoginInterceptorCallback() {
            override fun isLogin(): Boolean {
                return MainActivity.isLogin
            }

            override fun getLoginPageActivity(): Class<out Activity> {
                return LoginActivity::class.java
            }

            override fun defaultLoginInterceptorUI(activity: Activity, block: TodoBlock): Boolean {
                val builder = AlertDialog.Builder(activity)
                builder.setMessage("您还没有登录")
                    .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
                    .setPositiveButton("登录")
                    { dialog, _ ->
                        LoginInterceptor.with(activity)?.login(block)
                        dialog.dismiss()
                    }.show()
                return true
            }
        })
    }
}