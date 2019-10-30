package io.cordova.lexuncompany.application;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.URLConnectionNetworkExecutor;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import io.cordova.lexuncompany.R;
import io.cordova.lexuncompany.bean.base.App;
import io.cordova.lexuncompany.view.CardContentActivity;

/**
 * Created by JasonYao on 2018/2/27.
 */

public class MyApplication extends Application {
    public static MyApplication mInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化NoHttp
        NoHttp.initialize(this, new NoHttp.Config()
                .setReadTimeout(30 * 1000)  //服务器响应超时时间
                .setConnectTimeout(30 * 1000)  //连接超时时间
                .setNetworkExecutor(new URLConnectionNetworkExecutor()) //使用HttpURLConnection做网络层
        );

        Logger.setTag("NoHttpSample");
        Logger.setDebug(true); //开启调试模式

        Stetho.initializeWithDefaults(this);  //初始化Chrome查看Sqlite插件


        Beta.canShowUpgradeActs.add(CardContentActivity.class);
        Bugly.init(getApplicationContext(), App.LexunCard.BUGLY_APPID, false);  //乐巡企业版bugly

        //极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.statusBarDrawable = R.mipmap.logo;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL//
                | Notification.FLAG_SHOW_LIGHTS; // 设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE |  // 设置为、震动
                Notification.DEFAULT_LIGHTS; // 设置为呼吸灯闪烁
        JPushInterface.setPushNotificationBuilder(1, builder);

        this.mInstance = this;
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
