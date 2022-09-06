package com.lazyee.login.interceptor

import android.app.Activity
import android.content.Context

/**
 * @Author leeorz
 * @Date 3/15/21-12:18 PM
 * @Description:登录拦截Callback
 */
abstract class LoginInterceptorCallback {
    /**
     * 判断是否登录
     *
     * @return
     */
    abstract fun isLogin():Boolean

    /**
     * 获取登录界面Activity
     *
     * @return
     */
    abstract fun getLoginPageActivity(): Class<out Activity>

    /**
     * 默认拦截登录的UI
     *
     * @param interceptor
     * @return
     */
    open fun defaultLoginInterceptorUI(interceptor: LoginInterceptor):Boolean = false

    /**
     * 是否在登录之后继续业务代码执行
     *
     * @return
     */
    open fun isPerformBusinessCodeAfterLogin():Boolean = true
}