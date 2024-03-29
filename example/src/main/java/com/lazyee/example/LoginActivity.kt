package com.lazyee.example

import android.content.Intent
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
        tvExtra.text = "上个界面传入的值:${intent.getIntExtra("key",0)}"

        btnLoginSuccess.setOnClickListener {
            MainActivity.isLogin = true
            LoginInterceptor.loginComplete(this)
        }

        btnGotoNextLoginPage.setOnClickListener {
            val intent = Intent(this@LoginActivity,LoginActivity2::class.java)
            LoginInterceptor.continueLogin(this,intent)
//            LoginInterceptor.with(this)
//                .setLoginPage(LoginActivity2::class.java)
//                .doLogin()
        }
    }
}