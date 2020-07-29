## Android
### 1: app 中集成最新的[友盟分享库](// https://developer.umeng.com/docs/128606/detail/129620)
* android 依赖
```groovy
maven { url 'https://dl.bintray.com/umsdk/release' }

compile 'com.android.support:support-v4:25.3.1'
compile 'com.umeng.umsdk:common:2.2.5'
compile 'com.umeng.umsdk:share-core:7.0.2'
compile 'com.umeng.umsdk:share-board:7.0.2'

compile 'com.umeng.umsdk:share-wx:7.0.2'
// compile 'com.umeng.umsdk:share-qq:7.0.2'
// compile 'com.umeng.umsdk:share-sina:7.0.2'
// compile 'com.umeng.umsdk:share-alipay:7.0.2'
// compile 'com.umeng.umsdk:share-dingding:7.0.2'
```

* android 初始化并分享到微信小程序
```java
public class STShareManager {
    public static void init(final Activity activity) {
        if (!UMConfigure.getInitStatus()) {
            UMConfigure.init(activity, getUmengKey(activity), getUmengChannel(activity), UMConfigure.DEVICE_TYPE_PHONE, "");
            PlatformConfig.setWeixin(getWxId(activity), getWxKey(activity));

            // 豆瓣RENREN平台目前只能在服务器端配置
            // PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
            // PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
            // PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
            // PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
            // PlatformConfig.setAlipay("2015111700822536");
            // PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
            // PlatformConfig.setPinterest("1439206");
            // PlatformConfig.setKakao("e4f60e065048eb031e235c806b31c70f");
            // PlatformConfig.setDing("dingoalmlnohc0wggfedpk");
            // PlatformConfig.setVKontakte("5764965","5My6SNliAaLxEm3Lyd9J");
            // PlatformConfig.setDropbox("oz8v5apet3arcdy","h7p2pjbzkkxt02a");
            // PlatformConfig.setYnote("9c82bf470cba7bd2f1819b0ee26f86c6ce670e9b");
        }
    }

    public static void shareToMiniProgram(Activity activity, UMShareListener callback) {
        STShareManager.init(activity);

        UMMin umMin = new UMMin("http://www.smart.com"); //兼容低版本的网页链接
        umMin.setThumb(new UMImage(activity, R.drawable.icon_smart_wechat)); // 小程序消息封面图片
        umMin.setTitle("smart"); // 小程序消息title
        umMin.setDescription("smart description"); // 小程序消息描述
        umMin.setPath("pages/index/index"); //小程序页面路径
        umMin.setUserName(getWXMiNiOriginID(activity)); // 小程序原始id,在微信平台查询, 小程序->设置->基本设置->最下面账号信息里面的的原始ID

        // 预览版
        // com.umeng.socialize.Config.setMiniPreView();
        com.umeng.socialize.Config.setMiniTest(); // 测试版, 发布时注释

        new ShareAction(activity)
                .withMedia(umMin)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(callback).share();
    }
}
```

```kotlin
STShareManager.shareToMiniProgram(activity, object : UMShareListener {
    override fun onResult(p0: SHARE_MEDIA?) {
        MLogUtil.d("share", "onResult :$ { p0?.getName() }")
    }

    override fun onCancel(p0: SHARE_MEDIA?) {
        MLogUtil.d("share", "onCancel:${p0?.getName()}")
    }

    override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
        MLogUtil.d("share", "onError:${p0?.getName()}", p1)
    }

    override fun onStart(p0: SHARE_MEDIA?) {
        MLogUtil.d("share", "onStart:${p0?.getName()}")
    }
})
```

* android 接收微信小程序的唤醒
```java
package com.groupbuy.wxapi;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.app.STBootActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity {
    protected String TAG = getClass().getSimpleName();

    @Override
    public void onReq(com.tencent.mm.opensdk.modelbase.BaseReq req) {
        super.onReq(req);
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                Log.d(TAG, "onReq COMMAND_GETMESSAGE_FROM_WX");
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                // 小程序 跳转app
                ShowMessageFromWX.Req showReq = (ShowMessageFromWX.Req) req;
                WXMediaMessage wxMsg = showReq.message;
                WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
                String extInfo = obj.extInfo; // 对应 小程序 app_paramter 参数

                Log.d(TAG, "onReq COMMAND_SHOWMESSAGE_FROM_WX extInfo=" + extInfo);
                Uri uri = TextUtils.isEmpty(extInfo) ? null : Uri.parse(extInfo);
                STBootActivity.goToBootActivity(this, uri, true);
                // finish();
                break;
            default:
                Log.d(TAG, "onReq default type=" + req.getType());
                break;
        }
    }

    @Override
    public void onResp(final BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX && !TextUtils.isEmpty(resp.transaction)) {
            // 微信分享
            Log.d(TAG, "onResp COMMAND_SENDMESSAGE_TO_WX 微信分享");
        } else if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
            String extraData = launchMiniProResp.extMsg; // 对应JsApi navigateBackApplication中的extraData字段数据
            Log.d(TAG, "onResp COMMAND_LAUNCH_WX_MINIPROGRAM 微信小程序 extraData=" + extraData);
        } else if (resp.getType() == ConstantsAPI.COMMAND_OPEN_BUSINESS_VIEW) {
            Log.d(TAG, "onResp COMMAND_OPEN_BUSINESS_VIEW");
        } else {
            Log.d(TAG, "onResp type=" + resp.getType() + ", errCode=" + resp.errCode + ", errStr=" + resp.errStr);
            // 微信登陆回调逻辑处理
            if (resp instanceof SendAuth.Resp) {
                //
            }
            int code = resp.errCode;
            if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL || code == BaseResp.ErrCode.ERR_AUTH_DENIED) { //取消授权登录

            } else {
                //允许登录授权之后
            }
        }
    }
}

```

* android 配置 schema 的方式唤醒, 统一入口
```kotlin
package com.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import com.groupbuy.CCGoodsDetailFragment
import com.jdhome.base.BaseApplication

@Suppress("unused")
class STBootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val schemaUrl: String? = intent.data?.toString()

        val uri: Uri? = if (TextUtils.isEmpty(schemaUrl)) null else Uri.parse(schemaUrl)

        val moduleName = schemaUrl?.substringBefore("?")?.substringAfterLast("/")
        val isAppInit = BaseApplication.getInstance().isInit
        Log.w("[boot]", "isHomeCreated=$isHomeCreated, isAppInit=$isAppInit, schemaUrl=$schemaUrl, moduleName=$moduleName")


        if (schemaUrl?.startsWith("smart://template") == true) {
            if (isAppInit && isHomeCreated) {
                // App 已经初始化过, 说明首页存在


                when (moduleName) {
                    "goodsdetail" -> {
                        // smart://template/goodsdetail?goodsId=228&grouponId=5465416

                        val goodsId: String? = uri?.getQueryParameter("goodsId")
                        val grouponId: String? = uri?.getQueryParameter("grouponId")
                        CCGoodsDetailFragment.goToNewTask(goodsId?.toInt() ?: 0, grouponId?.toInt() ?: 0)
                    }
                }
            } else {
                // App 尚未启动, 走 Splash -> Home -> GoodsDetail

                AppLaunchActivity.goToLaunchActivity(this, uri)
            }
        }

        finish()
    }


    companion object {
        @JvmField
        var isHomeCreated: Boolean = false

        @JvmStatic
        @JvmOverloads
        fun goToBootActivity(context: Context, schemaUri: Uri?, flagNewTask: Boolean = false) {
            val intent = Intent(context, STBootActivity::class.java)
            if (schemaUri != null) {
                intent.data = schemaUri
            }
            if (flagNewTask) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            }
            context.startActivity(intent)
        }
    }
}
```

* android 配置 schema 的方式唤醒
```xml
<activity
    android:name="com.app.STBootActivity"
    android:configChanges="locale|fontScale|orientation|keyboardHidden|screenSize|smallestScreenSize|screenLayout"
    android:launchMode="standard"
    android:taskAffinity="com.template"
    android:theme="@style/AppStartTheme"
    android:windowSoftInputMode="stateHidden|adjustResize">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />

        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />

        <!-- 隐式Intent启动的activity，需要都要加上DEFAULT才可以匹配，如果category加了LAUNCHER则可选 -->
        <category android:name="android.intent.category.DEFAULT" />

        <!-- 浏览器在特定情况下可以打开这个页面 -->
        <category android:name="android.intent.category.BROWSABLE" />

        <data
            android:host="template"
            android:scheme="smart" />
    </intent-filter>
</activity>
```

### 2: 在小程序中打开 app
```xml
<button open-type="launchApp" app-parameter="smart://template/goodsdetail?goodsId=228&grouponId=5465416" binderror="launchAppError">打开APP</button>
```

## Ios
### 1: app 中集成最新的[友盟分享库](// https://developer.umeng.com/docs/128606/detail/129620)
* ios 依赖
```ruby
def pods_umeng
      pod 'UMCCommon'
      pod 'UMCCommonLog'
          
      # U-Share SDK UI模块（分享面板，建议添加）
      pod 'UMCShare/UI'
      
      # https://developers.weixin.qq.com/community/develop/doc/000a0af68c40d049b2f71aad756000
      # 一. 如果只有微信支付和分享一种功能，首先检查微信需要的的库和WechatSDK.a是否没问题，然后检查库的路径;
      # 二.微信支付和分享功能都有，去检查两个功能在项目目录下是否重复，我这次遇到的问题就是自己导入集成的微信支付和pod导入友盟的微信分享库冲突，最简单的解决办法是：
      # 1.  CMD+C项目中pod下Wechat文件夹下的libSocialOfficialWeChat.a；
      # 2.  然后把pod下微信的文件夹move to trash;
      # 3.最后一步，show in finder 你手动导入的wechat文件夹将刚copy的libSocialOfficialWeChat.a放在该文件夹下，然后引入项目下WeChat 目录下。
      # 编译，解决。
      # 集成微信(精简版0.2M)
      pod 'UMCShare/Social/ReducedWeChat'
      # 集成微信(完整版14.4M)
      # pod 'UMCShare/Social/WeChat'
      # pod 'WechatOpenSDK' # 微信官方 https://developers.weixin.qq.com/doc/oplatform/Downloads/iOS_Resource.html
      
      # 集成QQ/QZone/TIM(精简版0.5M)
      # pod 'UMCShare/Social/ReducedQQ'
      # 集成QQ/QZone/TIM(完整版7.6M)
      # pod 'UMCShare/Social/QQ'
      # 集成新浪微博(精简版1M)
      # pod 'UMCShare/Social/ReducedSina'
      # 集成新浪微博(完整版25.3M)
      # pod 'UMCShare/Social/Sina'
      # 集成Facebook/Messenger
      # pod 'UMCShare/Social/Facebook'
      # 集成Twitter
      # pod 'UMCShare/Social/Twitter'
      # 集成支付宝
      # pod 'UMCShare/Social/AlipayShare'
      # 集成钉钉
      # pod 'UMCShare/Social/DingDing'
      # 集成豆瓣
      # pod 'UMCShare/Social/Douban'
      # 集成人人
      # pod 'UMCShare/Social/Renren'
      # 集成腾讯微博
      # pod 'UMCShare/Social/TencentWeibo'
      # 集成易信
      # pod 'UMCShare/Social/YiXin'
      # 集成Flickr
      # pod 'UMCShare/Social/Flickr'
      # 集成Kakao
      # pod 'UMCShare/Social/Kakao'
      # 集成Tumblr
      # pod 'UMCShare/Social/Tumblr'
      # 集成Pinterest
      # pod 'UMCShare/Social/Pinterest'
      # 集成Instagram
      # pod 'UMCShare/Social/Instagram'
      # 集成Line
      # pod 'UMCShare/Social/Line'
      # 集成WhatsApp
      # pod 'UMCShare/Social/WhatsApp'
      # 集成有道云笔记
      # pod 'UMCShare/Social/YouDao'
      # 集成印象笔记
      # pod 'UMCShare/Social/EverNote'
      # 集成Google+
      # pod 'UMCShare/Social/GooglePlus'
      # 集成Pocket
      # pod 'UMCShare/Social/Pocket'
      # 集成DropBox
      # pod 'UMCShare/Social/DropBox'
      # 集成VKontakte
      # pod 'UMCShare/Social/VKontakte'
      # 集成邮件
      # pod 'UMCShare/Social/Email'
      # 集成短信
      # pod 'UMCShare/Social/SMS'
end
```

* ios 初始化并分享到微信小程序
```objectivec
#import <UMCommon/UMCommon.h>
#import <UMShare/UMShare.h>

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {

    [UMConfigure setLogEnabled:YES];//设置打开日志
    [UMConfigure initWithAppkey:@"5f1ea7c1d62dd10bc71c656e" channel:@"normal"];
    //配置微信平台的Universal Links
    //微信和QQ完整版会校验合法的universalLink，不设置会在初始化平台失败
    [UMSocialGlobal shareInstance].universalLinkDic = @{@(UMSocialPlatformType_WechatSession):@"https://umplus-sdk-download.oss-cn-shanghai.aliyuncs.com/",
                                                        @(UMSocialPlatformType_QQ):@"https://umplus-sdk-download.oss-cn-shanghai.aliyuncs.com/qq_conn/101830139"
                                                        };
    /* 设置微信的appKey和appSecret */
    [[UMSocialManager defaultManager] setPlaform:UMSocialPlatformType_WechatSession
                                          appKey:@"wxac68ef14f0c751f5"
                                       appSecret:@"2946681cfdd3a3ebfce980225a791627"
                                     redirectURL:@"http://mobile.umeng.com/social"];
    /*设置小程序回调app的回调*/
    [[UMSocialManager defaultManager] setLauchFromPlatform:(UMSocialPlatformType_WechatSession) completion:^(id userInfoResponse, NSError *error) {
        NSLog(@"setLauchFromPlatform:userInfoResponse:%@",userInfoResponse);
    }];

    return YES;
}
```

```objectivec
- (void)shareMiniProgramToPlatformType:(UMSocialPlatformType)platformType
{
    //创建分享消息对象
    UMSocialMessageObject *messageObject = [UMSocialMessageObject messageObject];

    UMShareMiniProgramObject *shareObject = [UMShareMiniProgramObject shareObjectWithTitle:@"菜仓生鲜" descr:@"菜仓生鲜小程序详情" thumImage:[UIImage imageNamed:@"logo_caicang"]];
    shareObject.webpageUrl = @"http://www.jiduojia.com";
    shareObject.userName = @"gh_07cca6d0b50f";
    shareObject.path = @"pages/index/index";
    messageObject.shareObject = shareObject;
    shareObject.hdImageData = [NSData dataWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"logo" ofType:@"png"]];
    shareObject.miniProgramType = UShareWXMiniProgramTypeRelease; // 可选体验版和开发板
    shareObject.miniProgramType = UShareWXMiniProgramTypeTest; // 可选体验版和开发板

    //调用分享接口
    [[UMSocialManager defaultManager] shareToPlatform:platformType messageObject:messageObject currentViewController:self completion:^(id data, NSError *error) {
        if (error) {
            UMSocialLogInfo(@"************Share fail with error %@*********",error);
        }else{
            if ([data isKindOfClass:[UMSocialShareResponse class]]) {
                UMSocialShareResponse *resp = data;
                //分享结果消息
                UMSocialLogInfo(@"response message is %@",resp.message);
                //第三方原始返回的数据
                UMSocialLogInfo(@"response originalResponse data is %@",resp.originalResponse);

            }else{
                UMSocialLogInfo(@"response data is %@",data);
            }
        }
        NSLog(@"share error:%@", error);
    }];
}
```

* ios 接收微信小程序的唤醒
```objectivec
- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation {
    if ([[UMSocialManager defaultManager] handleOpenURL:url]) {
        return [[UMSocialManager defaultManager] handleOpenURL:url];
    }
    return YES;
}

```

* ios 配置 schema 的方式唤醒, 统一入口
```objectivec

```

* android 配置 schema 的方式唤醒
```xml

```

### 2: 在小程序中打开 app
```xml
<button open-type="launchApp" app-parameter="smart://template/goodsdetail?goodsId=228&grouponId=5465416" binderror="launchAppError">打开APP</button>
```

### 其它

* 微信分享后台 appId 以及 appKey, 不用在友盟后台绑定, 直接在代码中设置, 分享的时候记住是小程序的原始 ID, 在微信后台设置里面
> https://open.weixin.qq.com/

* 友盟分享后台
> https://mobile.umeng.com/platform/apps/list

* 微信小程序无法跳转 h5 网页或者打开 应用宝下载页面
> https://developers.weixin.qq.com/community/develop/doc/0002864b144458f5389a898b956400?_at=1595991654416

### 参考文档
* 微信开发文档 https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/launchApp.html
* 友盟开发文档 https://developer.umeng.com/docs/128606/detail/129620