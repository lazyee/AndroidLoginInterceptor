package com.lazyee.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lazyee.login.interceptor.LoginInterceptor
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @Author leeorz
 * @Date 3/15/21-5:38 PM
 * @Description:
 */
class LoginActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLoginSuccess.setOnClickListener {
            MainActivity.isLogin = true
            LoginInterceptor.loginComplete(this)
        }
    }
}