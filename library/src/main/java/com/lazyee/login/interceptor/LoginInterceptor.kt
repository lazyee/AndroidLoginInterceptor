package com.lazyee.login.interceptor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
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
typealias BeforeTodoBlock = (Boolean)->Unit
class LoginInterceptor private constructor(private val activity: FragmentActivity) {

    private var mFragment: LoginInterceptorFragment? = null
    private lateinit var mTodoBlock:TodoBlock
    private var mBeforeTodoBlock: BeforeTodoBlock? = null
    private var mLoginInterceptorUI:LoginInterceptorUI? = null
    private var mIntent:Intent = Intent(activity, defaultLoginInterceptorConfig.getLoginPageActivity())
    private var isPerformBusinessCodeAfterLogin: Boolean = defaultLoginInterceptorConfig.isPerformBusinessCodeAfterLogin()
    private var mRequestCode:Int = defaultLoginInterceptorConfig.getLoginInterceptorRequestCode()


    /**
     * 在确认登录之前做一些操作
     *
     * @param block
     * @return
     */
    fun before(block: BeforeTodoBlock): LoginInterceptor {
        mBeforeTodoBlock = block
        return this
    }

    /**
     * 处理具体的业务，如果登录的话
     * @param block Function0<Unit>
     */
    fun todo(block:TodoBlock){
        this.mTodoBlock = block

        val isLogin = defaultLoginInterceptorConfig.isLogin()
        /**
         * 无论如何，这个登录之前的方法都会执行
         */
        mBeforeTodoBlock?.invoke(isLogin)

        /**
         * 用户已经登录，直接执行业务代码
         */
        if (isLogin){
            mTodoBlock.invoke()
            return
        }

        /**
         * 是否有自定义的拦截UI
         */
        if(mLoginInterceptorUI == null){
            if(defaultLoginInterceptorConfig.defaultLoginInterceptorUI(this))return
        }else{
            if(mLoginInterceptorUI!!.show(activity))return
        }

        addLoginInterceptorFragment(block)
    }

    /**
     * 前往登录界面，登录成功会执行block方法
     */
    fun doLogin(){
        addLoginInterceptorFragment(this.mTodoBlock)
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
    fun setPerformBusinessCodeAfterLogin(bool:Boolean): LoginInterceptor {
        isPerformBusinessCodeAfterLogin = bool
        return this
    }

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

    private fun addLoginInterceptorFragment(todo:TodoBlock){
        try {
            val transaction = activity.supportFragmentManager.beginTransaction()
            mFragment = activity.supportFragmentManager.findFragmentByTag(LoginInterceptorFragment.TAG) as LoginInterceptorFragment?

            if(mFragment != null && mFragment!!.isAdded){
                transaction.remove(mFragment!!).commitAllowingStateLoss()
                mFragment = null
            }

            mFragment = LoginInterceptorFragment(mIntent,
                mRequestCode,
                isPerformBusinessCodeAfterLogin,
                todo)
            transaction.add(mFragment!!,LoginInterceptorFragment.TAG).commitAllowingStateLoss()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    companion object{
        private var isInitialized = false
        /**
         * 全局登录判断条件,在Application类中进行实例化
         */
        private lateinit var defaultLoginInterceptorConfig:LoginInterceptorConfig

        fun init(config: LoginInterceptorConfig) {
            this.defaultLoginInterceptorConfig = config
            isInitialized = true
        }

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
         * 是否在登录之后继续执行业务代码(就是执行todo函数)
         *
         * @return true:继续执行，false:登录之后中断流程，需要用户再次发起
         */
        fun isPerformBusinessCodeAfterLogin(): Boolean {
            checkInitialized()
            return defaultLoginInterceptorConfig.isPerformBusinessCodeAfterLogin()
        }

        fun with(activity:Activity): LoginInterceptor {
            checkInitialized()
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
    setOnClickListener { LoginInterceptor.with(activity).todo(block) }
}
