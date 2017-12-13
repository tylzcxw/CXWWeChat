package com.tylz.wechat.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lqr.emoji.IImageLoader;
import com.lqr.emoji.LQREmotionKit;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.loader.ImageLoader;
import com.lqr.imagepicker.view.CropImageView;
import com.tylz.wechat.api.redpacket.SignService;
import com.tylz.wechat.app.base.BaseApp;
import com.tylz.wechat.db.DBManager;
import com.tylz.wechat.db.model.Friend;
import com.tylz.wechat.model.cache.UserCache;
import com.tylz.wechat.model.message.DeleteContactMessage;
import com.tylz.wechat.model.message.RedPacketMessage;
import com.tylz.wechat.util.UIUtils;


import org.json.JSONException;
import org.litepal.LitePal;

import io.rong.imlib.AnnotationNotFoundException;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ContactNotificationMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author cxw
 * @date 2017/12/5
 * @des TODO
 */

public class MyApp extends BaseApp implements RongIMClient.OnReceiveMessageListener {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);

        initRongClound();
        initImagePicker();
        LQREmotionKit.init(this,(context,path,imageView)->Glide.with(context).load(path).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView));
    }

    /**
     * 初始化仿微信控件ImagePicker
     */
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
                Glide.with(getContext()).load(Uri.parse("file://" + path).toString()).centerCrop().into(imageView);
            }

            @Override
            public void clearMemoryCache() {

            }
        });//设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }


    /**
     * 初始化融云
     */

    private void initRongClound() {
        /*
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIMClient 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            RongIMClient.init(this);
        }
        //监听接收到的消息
        RongIMClient.setOnReceiveMessageListener(this);
        try {
            RongIMClient.registerMessageType(RedPacketMessage.class);
            RongIMClient.registerMessageType(DeleteContactMessage.class);
        } catch (AnnotationNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    @Override
    public boolean onReceived(Message message, int i) {
        MessageContent messageContent = message.getContent();
        //todo  暂时放在这里
        return false;
    }
}
