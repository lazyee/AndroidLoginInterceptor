package com.lazyee.login.interceptor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.io.Serializable

/**
 * @Author leeorz
 * @Date 2020/11/3-11:28 AM
 * @Description:登录拦截
 */
private const val TAG = "[LoginInterceptor]"
typealias TodoBlock = ()->Unit
class LoginInterceptor private constructor(private val activity: FragmentActivity) {

    private var mLoginTodoBlock: TodoBlock? = null
    private var mLoginBeforeTodoBlock: TodoBlock? = null
    private var mLoginCancelTodoBlock: TodoBlock? = null
    private var mLoginInterceptorUI: LoginInterceptorUI? = null
    private var mIntent: Intent = Intent(activity, defaultLoginInterceptorConfig.getLoginPageActivity())
    private var isExecuteBusinessCodeAfterLogin: Boolean = defaultLoginInterceptorConfig.isExecuteBusinessCodeAfterLogin()
    private var mRequestCode: Int = defaultLoginInterceptorConfig.getLoginInterceptorRequestCode()

    /**
     * 未登录的情况下才会执行
     *
     * @param block
     * @return
     */
    fun onLoginBefore(block: TodoBlock): LoginInterceptor {
        mLoginBeforeTodoBlock = block
        return this
    }

    fun getLoginBeforeTodoBlock() = mLoginBeforeTodoBlock

    /**
     * 取消登录的时候才会执行
     *
     * @param block
     * @return
     */
    fun onLoginCancel(block: TodoBlock): LoginInterceptor {
        mLoginCancelTodoBlock = block
        return this
    }

    fun getLoginCancelTodoBlock() = mLoginCancelTodoBlock

    /**
     * 处理具体的业务，如果登录的话
     * @param block Function0<Unit>
     */
    fun execute(block:TodoBlock){
        this.mLoginTodoBlock = block

        val isLogin = defaultLoginInterceptorConfig.isLogin()
        /**
         * 如果未登录，那么执行before函数
         */
        if(!isLogin){
            mLoginBeforeTodoBlock?.invoke()
        }

        /**
         * 用户已经登录，直接执行业务代码
         */
        if (isLogin){
            mLoginTodoBlock?.invoke()
            return
        }

        /**
         * 是否有自定义的拦截UI
         */
        if(mLoginInterceptorUI == null){
            if(defaultLoginInterceptorConfig.defaultLoginInterceptorUI(this))return
        }else{
            if(mLoginInterceptorUI!!.show(this))return
        }

        addLoginInterceptorFragment(activity)
    }

    fun getLoginTodoBlock() = mLoginTodoBlock

    /**
     * 前往登录界面，登录成功会执行block方法
     */
    fun doLogin(){
        addLoginInterceptorFragment(activity)
    }

    /**
     * 继续登录
     */
    fun continueLogin(){
        addLoginInterceptorFragment(activity,true)
    }

    fun getActivity(): Activity {
        return activity
    }

    /**
     * 设置登录拦截UI
     *
     * @param ui
     */
    fun setInterceptorUI(ui:LoginInterceptorUI): LoginInterceptor {
        this.mLoginInterceptorUI = ui
        return this
    }

    /**
     * 设置是否在登录完成之后继续执行业务代码
     * true:继续执行
     * false:登录完成之后中断操作，等待用户下一步操作
     *
     * @param bool
     * @return
     */
    fun setExecuteBusinessCodeAfterLogin(bool:Boolean): LoginInterceptor {
        isExecuteBusinessCodeAfterLogin = bool
        return this
    }

    fun isExecuteBusinessCodeAfterLogin()=isExecuteBusinessCodeAfterLogin

    /**
     * 设置RequestCode
     *
     * @param code
     * @return
     */
    fun setRequestCode(code:Int):LoginInterceptor{
        mRequestCode = code;
        return this
    }

    fun getRequestCode() = mRequestCode


    fun putExtra(key:String,value:Any?): LoginInterceptor {
        when (value) {
            is String -> mIntent.putExtra(key, value)
            is Int -> mIntent.putExtra(key, value)
            is Double -> mIntent.putExtra(key, value)
            is Float -> mIntent.putExtra(key, value)
            is Long -> mIntent.putExtra(key, value)
            is Boolean -> mIntent.putExtra(key, value)
            is Short -> mIntent.putExtra(key, value)
            is Char -> mIntent.putExtra(key, value)
            is IntArray? -> mIntent.putExtra(key, value)
            is ByteArray? -> mIntent.putExtra(key, value)
            is CharArray? -> mIntent.putExtra(key, value)
            is LongArray? -> mIntent.putExtra(key, value)
            is FloatArray? -> mIntent.putExtra(key, value)
            is DoubleArray? -> mIntent.putExtra(key, value)
            is ShortArray? -> mIntent.putExtra(key, value)
            is BooleanArray? -> mIntent.putExtra(key, value)
            is Parcelable? -> mIntent.putExtra(key, value)
            is Serializable? -> mIntent.putExtra(key, value)
            is CharSequence? -> mIntent.putExtra(key, value)
            is Bundle? -> mIntent.putExtra(key, value)
        }

        return this
    }

    fun putIntArrayListExtra(key:String,value:ArrayList<Int>): LoginInterceptor {
        mIntent.putIntegerArrayListExtra(key,value)
        return this
    }

    fun putParcelableArrayListExtra(key:String,value:ArrayList<Parcelable>): LoginInterceptor {
        mIntent.putParcelableArrayListExtra(key,value)
        return this
    }

    fun putCharSequenceArrayListExtra(key:String,value:ArrayList<CharSequence>): LoginInterceptor {
        mIntent.putCharSequenceArrayListExtra(key,value)
        return this
    }

    fun putStringArrayListExtra(key:String,value:ArrayList<String>): LoginInterceptor {
        mIntent.putStringArrayListExtra(key,value)
        return this
    }

    fun putExtras(bundle:Bundle): LoginInterceptor {
        mIntent.putExtras(bundle)
        return this
    }

    fun putExtras(src:Intent):LoginInterceptor{
        mIntent.putExtras(src);
        return this
    }

    private fun addLoginInterceptorFragment(activity: FragmentActivity,isContinueLogin:Boolean = false){
        try {
            setCurrentLoginInterceptor(this)
            var fragment = activity.supportFragmentManager.findFragmentByTag(LoginInterceptorFragment.TAG) as LoginInterceptorFragment?

            if(fragment != null && fragment.isAdded){
                fragment.setReleaseFlag(!isContinueLogin)
                activity.supportFragmentManager
                    .beginTransaction()
                    .remove(fragment)
                    .commitAllowingStateLoss()
            }

            fragment = LoginInterceptorFragment(mIntent, this)

            activity.supportFragmentManager
                .beginTransaction()
                .add(fragment,LoginInterceptorFragment.TAG)
                .commitAllowingStateLoss()

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    companion object {
        private var currentLoginInterceptor :LoginInterceptor? = null
        private val activityList:MutableList<Activity> = mutableListOf()

        private var isInitialized = false
        /**
         * 全局登录判断条件,在Application类中进行实例化
         */
        private lateinit var defaultLoginInterceptorConfig:LoginInterceptorConfig

        fun init(config: LoginInterceptorConfig) {
            this.defaultLoginInterceptorConfig = config
            isInitialized = true
        }

        fun getDefaultLoginInterceptorConfig()= defaultLoginInterceptorConfig

        /**
         * 检查是否初始化
         *
         * @return
         */
        private fun checkInitialized(): Boolean {
            if(!isInitialized){
                throw Exception("请先执行init方法进行初始化")
            }
            return true
        }

        /**
         * 设置当前的拦截器
         *
         * @param interceptor
         */
        fun setCurrentLoginInterceptor(interceptor: LoginInterceptor?){
            interceptor?:return
            currentLoginInterceptor = interceptor
        }

        /**
         * 是否在登录之后继续执行业务代码(就是执行todo函数)
         *
         * @return true:继续执行，false:登录之后中断流程，需要用户再次发起
         */
        fun isExecuteBusinessCodeAfterLogin(): Boolean {
            checkInitialized()
            return defaultLoginInterceptorConfig.isExecuteBusinessCodeAfterLogin()
        }

        fun with(activity:Activity): LoginInterceptor {
            checkInitialized()
            if(activity !is FragmentActivity){
                throw Exception("activity must be FragmentActivity!")
            }

            return LoginInterceptor(activity)
        }

        fun continueLogin(activity: Activity, intent: Intent){
            addIntermediateActivity(activity)
            if (currentLoginInterceptor == null)throw Exception("你调用continueLogin之前必须先调用LoginInterceptor实例的doLogin方法")
            currentLoginInterceptor!!.mIntent = intent
            currentLoginInterceptor!!.continueLogin()

        }

        /**
         * 添加中间过程activity
         *
         * @param activity
         */
        private fun addIntermediateActivity(activity: Activity){
            if(activityList.contains(activity))return
            activityList.add(activity)
        }

        /**
         * 关闭所有中间过程界面
         */
        private fun finishAllIntermediateActivity(){
            activityList.forEach { it.finish() }
            activityList.clear()
        }

        private fun finishAll(activity: Activity?,resultCode:Int = Activity.RESULT_CANCELED){
            activity?.setResult(resultCode)
            activity?.finish()
            finishAllIntermediateActivity()
        }

        /**
         * 登录完成
         *
         * @param activity
         */
        fun loginComplete(activity: Activity?){
            finishAll(activity, Activity.RESULT_OK)
        }

        /**
         * 登录完成
         *
         * @param fragment
         */
        fun loginComplete(fragment: Fragment){
            loginComplete(fragment.activity)
        }

        /**
         * 取消登录，并且返回登录发起页面
         *
         * @param activity
         */
        fun loginCancel(activity: Activity?){
            finishAll(activity)
        }

        /**
         * 取消登录，并且返回登录发起页面
         *
         * @param fragment
         */
        fun loginCancel(fragment: Fragment){
            loginCancel(fragment.activity)
        }


        /**
         * 释放引用的对象
         *
         */
        fun release() {
            Log.i(TAG,"release all references!!!")
            activityList.clear()
            currentLoginInterceptor = null
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
    setOnClickListener { LoginInterceptor.with(activity).execute(block) }
}
