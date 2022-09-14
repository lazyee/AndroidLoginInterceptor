package com.lazyee.login.interceptor

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

/**
 * fragment 登录拦截Fragment
 *
 * @property intent
 * @property loginInterceptor
 * @constructor Create empty Login interceptor fragment
 */
internal class LoginInterceptorFragment(private val intent :Intent,
                                        private val loginInterceptor: LoginInterceptor):Fragment() {
    private var mReleaseFlag = true
    override fun onAttach(context: Context) {
        super.onAttach(context)
        startActivityForResult(intent,loginInterceptor.getRequestCode())
    }

    fun setReleaseFlag(flag:Boolean){
        mReleaseFlag = flag
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()

        if(requestCode != loginInterceptor.getRequestCode())return
        if(resultCode == Activity.RESULT_CANCELED){
            if (loginInterceptor.getLoginCancelTodoBlock() == null){
                LoginInterceptor.getDefaultLoginInterceptorConfig().defaultLoginCancelCallback(loginInterceptor)
            }else{
                loginInterceptor.getLoginCancelTodoBlock()!!.invoke()
            }

            return
        }

        if(resultCode == Activity.RESULT_OK) {
            if (!loginInterceptor.isExecuteBusinessCodeAfterLogin()) return

            loginInterceptor.getLoginTodoBlock()?.invoke()
        }

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