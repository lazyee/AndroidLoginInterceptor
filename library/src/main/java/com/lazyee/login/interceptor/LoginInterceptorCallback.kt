package com.lazyee.login.interceptor

import android.app.Activity

/**
 * @Author leeorz
 * @Date 3/15/21-12:18 PM
 * @Description:
 */
interface LoginInterceptorCallback {
    fun isLogin():Boolean
    fun getLoginPageActivity(): Class<out Activity>?
    fun onNotLogin(block: DoSomeThingBlock):Boolean = false
}