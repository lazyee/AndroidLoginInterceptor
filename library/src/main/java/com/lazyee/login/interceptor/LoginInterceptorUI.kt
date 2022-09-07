package com.lazyee.login.interceptor

import android.app.Activity

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:登录拦截的UI
 * Date: 2022/9/6 09:30
 */
interface LoginInterceptorUI {
    /**
     * 显示拦截UI,这里由实现该接口的类来实现
     *
     * @return true:打断登录流程，需要由实现类来继续实现登录流程,
     *         false:不打断登录流程，将跳转登录界面
     */
    fun show(interceptor: LoginInterceptor):Boolean
}

object LoginInterceptorUITemplates{

    fun NONE() = NoneUI()

    class NoneUI : LoginInterceptorUI{
        override fun show(interceptor: LoginInterceptor): Boolean {
            return false
        }
    }
}