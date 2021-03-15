package com.lazyee.login.interceptor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment

/**
 * fragment 登录拦截Fragment
 * @property intent Intent
 * @property TAG String
 * @property REQUEST_CODE_LOGIN_INTERCEPT Int
 * @constructor
 */
internal class LoginInterceptorFragment(private val intent :Intent,private val block:DoSomeThingBlock):Fragment() {

    private val REQUEST_CODE_LOGIN_INTERCEPT = 8001

    override fun onAttach(context: Context) {
        super.onAttach(context)
        startActivityForResult(intent,REQUEST_CODE_LOGIN_INTERCEPT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)
            ?.commitAllowingStateLoss()
        if(resultCode != Activity.RESULT_OK)return
        if(requestCode == REQUEST_CODE_LOGIN_INTERCEPT){
            block()
        }
    }

    companion object{
        val TAG :String by lazy { this::class.java.simpleName }
    }
}