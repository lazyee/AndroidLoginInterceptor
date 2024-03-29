```groovy
dependencies {
    implementation 'com.github.lazyee:AndroidLoginInterceptor:0.0.16'
}
```
在Application中进行初始化
```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        LoginInterceptor.init(object : LoginInterceptorConfig() {
            override fun isLogin(): Boolean {
                return MainActivity.isLogin
            }

            override fun getLoginPageActivity(): Class<out Activity> {
                return LoginActivity::class.java
            }

            override fun defaultLoginInterceptorUI(interceptor: LoginInterceptor): Boolean {
                val builder = AlertDialog.Builder(interceptor.getActivity())
                builder.setMessage("您还没有登录")
                    .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton("登录")
                    { dialog, _ ->
                        interceptor.doLogin()
                        dialog.dismiss()
                    }.show()
                return true
            }
        })
    }
}
```


在需要做登录校验的地方加上这个
```kotlin
LoginInterceptor.with(activity).execute{
    //todo
}
```

在登录页完成之后调用
```kotlin
//登录完成，关闭页面
LoginInterceptor.loginComplete(activity)
```
在登录页取消登录之后调用
```kotlin
//登录取消，关闭页面
LoginInterceptor.loginCancel(activity)
```
释放资源
```kotlin
LoginInterceptor.release()
```
