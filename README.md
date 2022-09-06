```groovy
dependencies {
    implementation 'com.github.lazyee:AndroidLoginInterceptor:0.0.6'
}
```
在Application中进行初始化
```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        LoginInterceptor.init(object : LoginInterceptorCallback() {
            override fun isLogin(): Boolean {
                return MainActivity.isLogin
            }

            override fun getLoginPageActivity(): Class<out Activity> {
                return LoginActivity::class.java
            }

            override fun defaultLoginInterceptorUI(activity: Activity, block: TodoBlock): Boolean {
                val builder = AlertDialog.Builder(activity)
                builder.setMessage("您还没有登录")
                    .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
                    .setPositiveButton("登录")
                    { dialog, _ ->
                        LoginInterceptor.with(activity)?.login(block)
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
LoginInterceptor.with(activity).todo{
    //todo
}
```

在登录页完成之后调用
```kotlin
//登录完成，关闭页面
LoginInterceptor.loginComplete()
```
or
```kotlin
setResult(Activity.RESULT_OK)
finish()
```