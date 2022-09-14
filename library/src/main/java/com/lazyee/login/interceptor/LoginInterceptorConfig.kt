package com.lazyee.login.interceptor

import android.app.Activity
import android.content.Context

/**
 * @Author leeorz
 * @Date 3/15/21-12:18 PM
 * @Description:登录拦截配置
 */
abstract class LoginInterceptorConfig {
    /**
     * 判断是否登录
     *
     * @return
     */
    abstract fun isLogin(): Boolean

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
     * @return true:中断登录流程，false:在显示ui之后继续登录流程
     */
    open fun defaultLoginInterceptorUI(interceptor: LoginInterceptor): Boolean = false

    /**
     * 是否在登录之后继续业务代码执行
     *
     * @return
     */
    open fun isExecuteBusinessCodeAfterLogin(): Boolean = true

    /**
     * 获取登录拦截的RequestCode
     *
     * @return
     */
    open fun getLoginInterceptorRequestCode(): Int = 8001

    /**
     * 取消登录
     *
     * @param interceptor
     */
    open fun defaultLoginCancelCallback(interceptor: LoginInterceptor){

    }


}