package com.lazyee.login.interceptor

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

/**
 * fragment 登录拦截Fragment
 *
 * @property intent
 * @property loginInterceptorRequestCode
 * @property todo
 * @constructor Create empty Login interceptor fragment
 */
internal class LoginInterceptorFragment(private val intent :Intent,
                                        private val loginInterceptorRequestCode: Int,
                                        private val isExecuteBusinessCodeAfterLogin:Boolean,
                                        private val todo:TodoBlock?):Fragment() {
    private var mReleaseFlag = true
    override fun onAttach(context: Context) {
        super.onAttach(context)
        startActivityForResult(intent,loginInterceptorRequestCode)
    }

    fun setReleaseFlag(flag:Boolean){
        mReleaseFlag = flag
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()

        if(resultCode != Activity.RESULT_OK)return
        if(requestCode != loginInterceptorRequestCode)return
        if(!isExecuteBusinessCodeAfterLogin)return
        todo?.invoke()

    }



    override fun onDestroy() {
        super.onDestroy()
        if(!mReleaseFlag)return
        LoginInterceptor.release()
    }


    companion object{
        val TAG :String by lazy { this::class.java.simpleName }
    }
}