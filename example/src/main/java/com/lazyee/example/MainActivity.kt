package com.lazyee.example

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lazyee.login.interceptor.LoginInterceptor
import com.lazyee.login.interceptor.LoginInterceptorUI
import com.lazyee.login.interceptor.LoginInterceptorUITemplates
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * @Author leeorz
 * @Date 3/15/21-5:37 PM
 * @Description:
 */
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        setContentView(R.layout.activity_main)

        btnClear.setOnClickListener {
            isLogin = false
            tvLoginState.text = "是否登录:$isLogin"
        }
        btnSetter.setOnClickListener {
            LoginInterceptor.with(this)
                .putExtra("key",1)
                .onLoginBefore {
                    Log.e("leeorz","这个检查登录之前啦,isLogin:${isLogin}")
                }
                .execute {
                    Log.e("leeorz","TODO")
                    tvResult.text = "result:${Random().nextInt()}"
                    val intent = Intent(this@MainActivity,MainActivity::class.java)
                    startActivity(intent)

                }
        }
        btnCustomInterceptorUI.setOnClickListener {
            LoginInterceptor.with(this)
                .putExtra("key",2)
                .setInterceptorUI(LoginInterceptorUITemplates.NONE())
                .setExecuteBusinessCodeAfterLogin(true)
                .onLoginBefore {
                    Log.e("leeorz","这个检查登录之前啦,isLogin:${isLogin}")
                }
                .onLoginCancel {
                    Log.e("leeorz","取消登录啦")
                }
                .execute {
                    Log.e("leeorz","TODO")
                    tvResult.text = "result:${Random().nextInt()}"
                }
        }
    }

    override fun onResume() {
        super.onResume()
        tvLoginState.text = "是否登录:$isLogin"
    }


    companion object {
        var isLogin = false
        var instance:MainActivity? = null
    }
}

class CustomLoginInterceptorUI : LoginInterceptorUI {
    override fun show(interceptor: LoginInterceptor): Boolean {
        Toast.makeText(interceptor.getActivity(), "gogogo", Toast.LENGTH_SHORT).show()
        return false
    }


}