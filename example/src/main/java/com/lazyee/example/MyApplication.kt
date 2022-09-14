package com.lazyee.example

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.widget.Toast
import com.lazyee.login.interceptor.LoginInterceptor
import com.lazyee.login.interceptor.LoginInterceptorConfig

/**
 * @Author leeorz
 * @Date 3/15/21-5:35 PM
 * @Description:
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        LoginInterceptor.init(object : LoginInterceptorConfig() {
            override fun isLogin(): Boolean {
                return MainActivity.isLogin
            }

            override fun getLoginPageActivity(): Class<out Activity> {
                return LoginActivity::class.java
            }

            override fun defaultLoginCancelCallback(interceptor: LoginInterceptor) {
                super.defaultLoginCancelCallback(interceptor)
                Toast.makeText(interceptor.getActivity(),"取消登录",Toast.LENGTH_SHORT).show()
            }

            override fun defaultLoginInterceptorUI(interceptor: LoginInterceptor): Boolean {
                val builder = AlertDialog.Builder(interceptor.getActivity())
                builder.setMessage("您还没有登录")
                    .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton("登录")
                    { dialog, _ ->
                        interceptor.doLogin()
                        dialog.dismiss()
                    }.show()
                return true
            }
        })
    }
}