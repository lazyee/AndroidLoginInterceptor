package com.lazyee.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lazyee.login.interceptor.LoginInterceptor
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * @Author leeorz
 * @Date 3/15/21-5:37 PM
 * @Description:
 */
class MainActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnClear.setOnClickListener {
            isLogin = false
            tvLoginState.text = "是否登录:$isLogin"
        }
        btnSetter.setOnClickListener {
            LoginInterceptor.with(this)?.doSomeThing {
                tvResult.text = "result:${Random().nextInt()}"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        tvLoginState.text = "是否登录:$isLogin"
    }


    companion object{
        var isLogin = false
    }
}