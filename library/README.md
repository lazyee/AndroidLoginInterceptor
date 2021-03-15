在Application中进行初始化
```kotlin
class MyApplication :BaseApplication(){

    override fun onCreate() {
        super.onCreate()

        LoginInterceptor.init(object : LoginInterceptorCallback {
            //全局登录判断
            override fun isLogin(): Boolean {
                return false
            }

            //要跳转的界面
            override fun getLoginPageActivity(): Class<out Activity>? {
                return LoginActivity::class.java
            }

            //如果不需要做登录的拦截UI显示的话，就不需要实现这个这方法，默认返回false
            override fun onNotLogin(block: DoSomeThingBlock): Boolean {
                val builder = AlertDialog.Builder(ActivityStack.current())
                builder.setMessage("您还没有登录")
                    .setNegativeButton("取消") { dialog, which -> dialog.dismiss()}
                    .setPositiveButton("登录")
                    {dialog,which ->
                        LoginInterceptor.with(ActivityStack.current())?.login(block)
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
LoginInterceptor.with(activity)?.doSomeThing{
    //todo
}
```