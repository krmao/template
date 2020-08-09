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

    public static void shareToMiniProgram(Activity activity, String title, String subTitle, String imageUrl, int goodsId, int grouponId, UMShareListener callback) {
        STShareManager.init(activity);

        UMMin umMin = new UMMin("http://www.jiduojia.com"); //兼容低版本的网页链接
        umMin.setThumb(new UMImage(activity, imageUrl)); // 小程序消息封面图片
        umMin.setTitle(title); // 小程序消息title
        umMin.setDescription(subTitle); // 小程序消息描述
        umMin.setPath("pages/index/index?goodsId=" + goodsId + "&grouponId=" + grouponId); //小程序页面路径, 并携带参数传给小程序, 在小程序的 onLoad: function (options) 获取
        umMin.setUserName(getWXMiNiOriginID(activity)); // 小程序原始id,在微信平台查询, 小程序->设置->基本设置->最下面账号信息里面的的原始ID

        // 预览版
        // com.umeng.socialize.Config.setMiniPreView();
        com.umeng.socialize.Config.setMiniTest();

        new ShareAction(activity)
                .withMedia(umMin)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(callback).share();
    }
}
```

```java
STShareManager.shareToMiniProgram(getActivity(), responseModel.data.goodsName, responseModel.data.goodsSubName, finalImageUrl, goodsId, grouponId, new UMShareListener() {

    @Override
    public void onStart(SHARE_MEDIA share_media) {
        MLogUtil.d("share", "onStart");
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        MLogUtil.d("share", "onResult");
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        MLogUtil.d("share", "onError");
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        MLogUtil.d("share", "onCancel");
    }
});
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
class STBootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val schemaUrl: String? = intent.data?.toString()

        val uri: Uri? = if (TextUtils.isEmpty(schemaUrl)) null else Uri.parse(schemaUrl)

        val moduleName = schemaUrl?.substringBefore("?")?.substringAfterLast("/")
        val isAppInit = BaseApplication.getInstance().isInit
        Log.w("[boot]", "isHomeCreated=$isHomeCreated, isAppInit=$isAppInit, schemaUrl=$schemaUrl, moduleName=$moduleName")


        if (schemaUrl?.startsWith("smart://caicang") == true) {
            if (isAppInit && isHomeCreated) {
                // App 已经初始化过, 说明首页存在


                when (moduleName) {
                    "goodsdetail" -> {
                        // smart://caicang/goodsdetail?goodsId=228&grouponId=5465416

                        val goodsId: String? = uri?.getQueryParameter("goodsId")
                        val grouponId: String? = uri?.getQueryParameter("grouponId")
                        CCGoodsDetailFragment.goToNewTask(goodsId?.toIntOrNull() ?: -1, grouponId?.toIntOrNull() ?: -1)
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
        fun goToBootActivity(context: Context, schemaUri: Uri?) {
            Log.d("goToBootActivity", "schemaUri=$schemaUri")
            val intent = Intent(context, STBootActivity::class.java)
            if (schemaUri != null) {
                intent.data = schemaUri
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
```js
import ApiManager from '../../repository/ApiManager';

Page({
  data: {
    options: {},
    schemaUrl: undefined
  },
  launchAppError(e) {
    // invalid scene	调用场景不正确，即此时的小程序不具备打开 APP 的能力
    console.log(e.detail.errMsg)
  },
  onLoad: function (options) {
    // 在 options 中接收 app 分享到小程序 url 中的参数
    if (options && options.goodsId && options.grouponId) {
      this.setData({
        options: options,
        schemaUrl: "smart://template/goodsdetail?goodsId=" + options.goodsId + "&grouponId=" + options.grouponId
      })
      console.log("index onLoad", options, this.data.schemaUrl)
    } else {
      wx.showToast({
        title: '参数错误:' + options.goodsId + "," + options.grouponId,
        icon: 'none',
        duration: 2000
      })
    }
  }
})
```
```xml
<button class='.bottom_button' open-type="launchApp" app-parameter="{{schemaUrl}}" binderror="launchAppError">OPEN APP</button>
```

## Ios
### 1: app 中集成最新的[友盟分享库](// https://developer.umeng.com/docs/128606/detail/129620)
#### 首先设置 app universal links
* xcode -> target -> signing & capabilities -> associated domains -> applinks:www.smart.com
* 新建 apple-app-site-association 文件, 没有后缀, 上传到 https://www.smart.com 的根目录或者 https://www.smart.com/.well-known/apple-app-site-association , 确保链接可以下载该文件
```json
{
    "applinks": {
        "details": [
            {
                "appIDs": [ "teamId.com.xx.xx" ],
                "components": [
                    {
                        "/": "/app/*",
                        "comment": "Matches any URL whose path starts with /app/"
                    }
                ]
            }
        ]
    },
    "webcredentials": {
      "apps": [ "teamId.com.xx.xx" ]
    }
}
```
* 在浏览器 输入 https://www.smart.com/app 即可打开 app
```objectivec
#pragma mark Universal Link

- (BOOL)application:(UIApplication *)application continueUserActivity:(NSUserActivity *)userActivity restorationHandler:(void (^)(NSArray<id <UIUserActivityRestoring>> *__nullable restorableObjects))restorationHandler {
    NSLog(@"Universal Link continueUserActivity url=%@", userActivity.webpageURL);
    /*if ([userActivity.activityType isEqualToString:NSUserActivityTypeBrowsingWeb]) {
        NSURL *url = userActivity.webpageURL;
        return YES;
    }*/
    return [WXApi handleOpenUniversalLink:userActivity delegate:self];
}
```

* ios 依赖
```ruby
  # https://developers.weixin.qq.com/doc/oplatform/Downloads/iOS_Resource.html
  # https://developers.weixin.qq.com/doc/oplatform/Mobile_App/Access_Guide/iOS.html
  pod 'WechatOpenSDK', '~> 1.8.7.1'
```

* ios 初始化并分享到微信小程序
```objectivec
#import "WXApi.h"

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {

    // 在register之前打开log, 后续可以根据log排查问题，正式环境注释
    // [WXApi startLogByLevel:WXLogLevelDetail logBlock:^(NSString *log) {
    //     NSLog(@"[wechat]:startLogByLevel %@", log);
    // }];
    // 向微信注册 https://developers.weixin.qq.com/community/develop/doc/000200d2d106301d11a94189451400?jumpto=reply&commentid=000806ba8ac2d83811b9891e8594&parent_commentid=000442fa884538ed16a9529e35b4
    [WXApi registerApp:@"wxaxxxxxx1f5" universalLink:@"https://www.smart.com/app/"]; // appKey:@"wxaxxxxxx1f5"
    // 调用自检函数，正式环境注释
    // [WXApi checkUniversalLinkReady:^(WXULCheckStep step, WXCheckULStepResult *result) {
    //     NSLog(@"[wechat]:checkUniversalLinkReady %@, %u, %@, %@", @(step), result.success, result.errorInfo, result.suggestion);
    // }];
    return YES;
}
```

```objectivec
- (BOOL)application:(UIApplication *)application continueUserActivity:(NSUserActivity *)userActivity restorationHandler:(void (^)(NSArray<id <UIUserActivityRestoring>> *__nullable restorableObjects))restorationHandler {
    NSLog(@"Universal Link continueUserActivity url=%@", userActivity.webpageURL);
    return [WXApi handleOpenUniversalLink:userActivity delegate:self];
}

- (void)onReq:(BaseReq *)req {
    NSLog(@"onReq");

    if ([req isKindOfClass:[LaunchFromWXReq class]]) {
        LaunchFromWXReq *wxReq = (LaunchFromWXReq *)req;
        NSString *schemaUrlString = wxReq.message.messageExt;
        NSLog(@"[wechat]:onReq LaunchFromWXReq schemaUrlString=%@", schemaUrlString);
        [self handlerSchema:schemaUrlString];
    } else if ([req isKindOfClass:[WXLaunchMiniProgramReq class]]) {
        WXLaunchMiniProgramReq *wxReq = (WXLaunchMiniProgramReq *)req;
        NSString *message = wxReq.extMsg;
        NSLog(@"[wechat]:onReq WXLaunchMiniProgramReq message=%@", message);
    }
}

- (void)onResp:(BaseResp *)resp {
    NSLog(@"onResp");
}

- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url {
    NSLog(@"handleOpenURL");
    return [WXApi handleOpenURL:url delegate:self];
}

- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation {
    NSLog(@"openURL before WXApi handleOpenURL, sourceApplication=%@, url=%@", sourceApplication, url);
    BOOL result = [WXApi handleOpenURL:url delegate:self];
    NSLog(@"openURL after WXApi handleOpenURL, result=%@", result ? @"YES" : @"NO");

    if (!result) {
        [self handlerSchema:[url absoluteString]];
    }
    return result;
}

- (void)handlerSchema:(NSString *)schemaUrlString {
    if ([schemaUrlString hasPrefix:@"smart://template"]) {
        NSURL *schemaUrl = [NSURL URLWithString:schemaUrlString];

        NSUInteger lastPathIndex = [schemaUrlString rangeOfString:@"/" options:NSBackwardsSearch].location;
        NSUInteger questionMarkIndex = [schemaUrlString rangeOfString:@"?"].location;
        NSString *moduleName = [schemaUrlString substringWithRange:NSMakeRange(lastPathIndex + 1, questionMarkIndex - lastPathIndex)];
        NSDictionary *params = [self parameterWithURL:schemaUrl];
        NSString *goodsId = params[@"goodsId"];
        NSString *grouponId = params[@"grouponId"];

        NSLog(@"schemaUrl=%@, moduleName=%@, goodsId=%@, grouponId=%@", schemaUrl, moduleName, goodsId, grouponId);

        STViewController *vc = [STViewController new];
        __WeakObject(self);
        vc.block = ^{
            __WeakStrongObject();
        };
        [[AppUtil topViewController].navigationController pushViewController:vc animated:YES];
    }
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    [[UIApplication sharedApplication] setApplicationIconBadgeNumber:0];
}

- (NSDictionary *)parameterWithURL:(NSURL *)url {
    NSMutableDictionary *params = [[NSMutableDictionary alloc] initWithCapacity:2];
    //传入url创建url组件类
    NSURLComponents *urlComponents = [[NSURLComponents alloc] initWithString:url.absoluteString];
    //回调遍历所有参数，添加入字典
    [urlComponents.queryItems enumerateObjectsUsingBlock:^(NSURLQueryItem *_Nonnull obj, NSUInteger idx, BOOL *_Nonnull stop) {
        params[obj.name] = obj.value;
    }];
    return params;
}
```
* 分享微信小程序到微信
```objectivec
- (void)shareMiniProgram {
    NSString * title = self.mJDGetGoodsDetailHttp.mBase.goodsName;
    NSString * subTitle = self.mJDGetGoodsDetailHttp.mBase.goodsSubName;
    NSString * imageUrl = self.mJDGetGoodsDetailHttp.mBase.goodsPicture;

    int goodsId = self.mJDCategoryGoodsData.id;
    int grouponId = self.mJDCategoryGoodsData.grouponId;

    NSData *imageData = [NSData dataWithContentsOfURL: [NSURL URLWithString:imageUrl]];

    WXMiniProgramObject *object = [WXMiniProgramObject object];
    object.webpageUrl = @"http://www.smart.com";
    object.userName =  @"gh_xxxxxxx2e";
    object.path = [NSString stringWithFormat:@"pages/index/index?goodsId=%d&grouponId=%d",goodsId ,grouponId];
    object.hdImageData = imageData; // 限制大小不超过128KB，自定义图片建议长宽比是 5:4。
    object.withShareTicket = true; // /** 是否使用带 shareTicket 的转发 */
    object.miniProgramType = WXMiniProgramTypeRelease;
    WXMediaMessage *message = [WXMediaMessage message];
    message.title = title;
    message.description = subTitle;
    message.thumbData = nil;  //兼容旧版本节点的图片，小于32KB，新版本优先
    //使用WXMiniProgramObject的hdImageData属性
    message.mediaObject = object;
    SendMessageToWXReq *req = [[SendMessageToWXReq alloc] init];
    req.bText = NO;
    req.message = message;
    req.scene = WXSceneSession;  //目前只支持会话
    [WXApi sendReq:req completion:^(BOOL success) {
        if(success){
            NSLog(@"分享到微信小程序成功");
        } else {
            NSLog(@"分享到微信小程序失败");
        }
    }];
}
```

* 代码注册微信 id 的时候, universal link 包含 path *[WXApi registerApp:@"wxaxxxxxxx1f5" universalLink:@"https://www.smart.com/app/"]*; 
* 微信后台绑定的 universal link 也包含 path *https://www.smart.com/app/* , 同时记得设置正确的 bundle ID 例如 com.smart.template
> https://open.weixin.qq.com/cgi-bin/appdetail?t=manage/detail&type=app&lang=zh_CN&token=f6e3eb55c1fee2d2f499d6cd03dbfbd475c42afc&appid=wxac68ef14f0c751f5


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