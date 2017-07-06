* Dagger 


依赖注入
比ButterKnife更强大，ButterKnife只是对view的注入。
使用Dagger依赖注入，可以在应用程序的各个点儿访问相同的对象，避免了耦合现象，提高性能。
例如网络客户端，图像加载程序或本地存储的SharedPreference。你可以将这些对象注入你的活动和碎片中，并立即访问它们。

以下例子，是对Shared Preference的依赖注入的使用，非常方便。
    
SharedPreference
    
 ```Java
   @Component(modules = {AppModule.class})
    public interface AppComponent {

       SharedPreferences getsharedPreferences();
    }
    
    
    //----
    @Module
    public class AppModule {
        Context mcontext;

        public AppModule(Context context) {
            this.mcontext = context;
        }

        @Provides
        Context providesContext(){
            return mcontext;
        }

        @Provides
        public SharedPreferences provideSharedPreferences(Context context){
            return PreferenceManager.getDefaultSharedPreferences(context);
        }
    }
```
    
------------------------------------------------------------
    
其它类使用到sharedPreferences的方法
 
```Java
   @Inject 
    SharedPreferences sharedPreferences;
     
    sharedPreferences = DaggerAppComponent.builder().appModule(new AppModule(mContext))
                .build().getsharedPreferences();
                
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(cities.get(0).getD2(), cities.get(0).getD1());
    editor.apply();
                                   
       //when sharedPreferences is null, locate current city                           
    if(sharedPreferences.getAll().size()==0) {
        //do something
    
    }
 ```
    
    



* 定位当前城市，获取城市代码
百度地图
定位开发指南
http://lbsyun.baidu.com/index.php?title=android-locsdk/guide/buildprojec
相当好用，方便。。。。


