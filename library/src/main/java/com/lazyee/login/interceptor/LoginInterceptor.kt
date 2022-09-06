package com.lazyee.login.interceptor

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.lang.Exception

/**
 * @Author leeorz
 * @Date 2020/11/3-11:28 AM
 * @Description:登录拦截
 */
private const val TAG = "[LoginInterceptor]"
typealias TodoBlock = ()->Unit
class LoginInterceptor private constructor(private val activity: FragmentActivity) {

    private var fragment: LoginInterceptorFragment? = null
    private var todoBlock:TodoBlock? = null
    private var loginInterceptorUI:LoginInterceptorUI? = null

    /**
     * 处理具体的业务，如果登录的话
     * @param block Function0<Unit>
     */
    fun todo(block:TodoBlock){
        this.todoBlock = block
        /**
         * 判断条件为空，证明无需处理登录情况，此时直接执行业务代码
         */
        if (loginInterceptorCallback == null){
            todoBlock?.invoke()
            return
        }

        /**
         * 用户已经登录，直接执行业务代码
         */
        if (loginInterceptorCallback!!.isLogin()){
            todoBlock?.invoke()
            return
        }

        /**
         * 是否有自定义的拦截UI
         */
        if(loginInterceptorUI == null){
            if(loginInterceptorCallback!!.defaultNotLogin(activity,block))return
        }else{
            if(loginInterceptorUI!!.show(activity))return
        }

        addLoginInterceptorFragment(block)
    }

    /**
     * 前往登录界面，登录成功会执行block方法
     * @param todo Function0<Unit>
     */
    fun login(todo:TodoBlock){
        this.todoBlock = todo
        addLoginInterceptorFragment(todo)
    }

    /**
     * 设置登录拦截UI
     *
     * @param ui
     */
    fun setInterceptorUI(ui:LoginInterceptorUI): LoginInterceptor {
        this.loginInterceptorUI = ui
        return this
    }

    /**
     * 前往登录界面
     */
    fun gotoLoginPage(){
        if (loginInterceptorCallback == null){
            throw Exception("必须先调用init()进行初始化")
        }
        activity.startActivity(Intent(activity,loginInterceptorCallback!!.getLoginPageActivity()))
    }

    private fun addLoginInterceptorFragment(todo:TodoBlock){
        try {
            val transaction = activity.supportFragmentManager.beginTransaction()
            fragment = activity.supportFragmentManager.findFragmentByTag(LoginInterceptorFragment.TAG) as LoginInterceptorFragment?

            if(fragment != null && fragment!!.isAdded){
                transaction.remove(fragment!!).commitAllowingStateLoss()
                fragment = null
            }
            fragment = LoginInterceptorFragment(Intent(activity,loginInterceptorCallback!!.getLoginPageActivity()),todo)
            transaction.add(fragment!!,LoginInterceptorFragment.TAG).commitAllowingStateLoss()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    companion object{

        /**
         * 全局登录判断条件,在Application类中进行实例化
         */
        private var loginInterceptorCallback:LoginInterceptorCallback? = null

        fun init(callback: LoginInterceptorCallback) {
            loginInterceptorCallback = callback
        }

        fun with(activity:Activity): LoginInterceptor {
            if(activity !is FragmentActivity){
                throw Exception("activity must be FragmentActivity!")
            }

            return LoginInterceptor(activity)
        }

        /**
         * 登录完成
         */
        fun loginComplete(activity: Activity?){
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }

        /**
         * 登录完成
         */
        fun loginComplete(fragment: Fragment){
            loginComplete(fragment.activity)
        }
    }

}

/**
 * View的拓展方法，用来处理登录状态拦截
 * @receiver View
 * @param activity FragmentActivity
 * @param block Function0<Unit>
 */
fun View.setOnClickIfLogin(activity: Activity?,block: TodoBlock){
    activity?:return
    if(activity !is FragmentActivity)return
    setOnClickListener { LoginInterceptor.with(activity)?.todo(block) }
}
