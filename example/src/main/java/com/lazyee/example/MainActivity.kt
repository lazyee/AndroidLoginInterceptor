package com.lazyee.example

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lazyee.login.interceptor.LoginInterceptor
import com.lazyee.login.interceptor.LoginInterceptorUI
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
        setContentView(R.layout.activity_main)

        btnClear.setOnClickListener {
            isLogin = false
            tvLoginState.text = "是否登录:$isLogin"
        }
        btnSetter.setOnClickListener {
            LoginInterceptor.with(this)
                .putExtra("key",1)
                .todo {
                    tvResult.text = "result:${Random().nextInt()}"
            }
        }
        btnCustomInterceptorUI.setOnClickListener {
            LoginInterceptor.with(this)
                .putExtra("key",2)
                .setInterceptorUI(CustomLoginInterceptorUI())
                .todo {
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
    }
}

class CustomLoginInterceptorUI : LoginInterceptorUI {
    override fun show(activity: Activity): Boolean {
        Toast.makeText(activity, "gogogo", Toast.LENGTH_SHORT).show()
        return false
    }

}