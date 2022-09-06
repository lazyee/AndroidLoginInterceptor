package com.lazyee.login.interceptor

import android.app.Activity
import android.content.Context

/**
 * @Author leeorz
 * @Date 3/15/21-12:18 PM
 * @Description:登录拦截Callback
 */
abstract class LoginInterceptorCallback {
    abstract fun isLogin():Boolean
    abstract fun getLoginPageActivity(): Class<out Activity>
    open fun defaultNotLogin(activity: Activity, block: TodoBlock):Boolean = false
}