#import "STWKWebViewController.h"
#import "STCommunication.h"

#define kURLPrefix          @"smart://hybird/bridge/"
#define kURLString          @"http://10.32.33.2:5389/#/"

@interface STWKWebViewController () <WKNavigationDelegate, WKUIDelegate, WKScriptMessageHandler>

@property(nonatomic, strong) WKWebView *webView;
@end

@implementation STWKWebViewController

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    [self.webView.configuration.userContentController removeScriptMessageHandlerForName:@"bridge"];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];

    WKWebViewConfiguration *configuration = [[WKWebViewConfiguration alloc] init];
    WKUserContentController *userController = [[WKUserContentController alloc] init];
    configuration.userContentController = userController;
    self.webView = [[WKWebView alloc] initWithFrame:CGRectMake(0, 100, self.view.frame.size.width, self.view.frame.size.height - 200) configuration:configuration];
    [userController addScriptMessageHandler:self name:@"bridge"];

    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:kURLString]]];
    self.webView.navigationDelegate = self;
    self.webView.UIDelegate = self;
    //开了支持滑动返回
    self.webView.allowsBackForwardNavigationGestures = YES;
    [self.view addSubview:self.webView];
}

#pragma mark WKUIDelegate

//显示一个JS的Alert（与JS交互）
- (void)webView:(WKWebView *)webView runJavaScriptAlertPanelWithMessage:(NSString *)message initiatedByFrame:(WKFrameInfo *)frame completionHandler:(void (^)(void))completionHandler {

    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"提示" message:message preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *action = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleCancel handler:^(UIAlertAction *_Nonnull action) {
        //此处的completionHandler()就是调用JS方法时，`evaluateJavaScript`方法中的completionHandler
        completionHandler();
    }];
    [alert addAction:action];
    [self presentViewController:alert animated:YES completion:nil];

}

// 显示一个确认框（JS的）
- (void)webView:(WKWebView *)webView runJavaScriptConfirmPanelWithMessage:(NSString *)message initiatedByFrame:(WKFrameInfo *)frame completionHandler:(void (^)(BOOL result))completionHandler {

}

// 弹出一个输入框（与JS交互的）
- (void)webView:(WKWebView *)webView runJavaScriptTextInputPanelWithPrompt:(NSString *)prompt defaultText:(nullable NSString *)defaultText initiatedByFrame:(WKFrameInfo *)frame completionHandler:(void (^)(NSString *_Nullable result))completionHandler {

}

#pragma mark WKScriptMessageHandler

//监听JS的回调
- (void)userContentController:(WKUserContentController *)userContentController didReceiveScriptMessage:(WKScriptMessage *)message {
    NSLog(@"message.boby 就是JS 里传过来的参数。： %@", message.body);
    if ([message.body hasPrefix:kURLPrefix]) {

        // 获取JS的方法名
        NSArray *requestParams = [message.body componentsSeparatedByString:@"?"];
        NSArray *urlParmes = [[requestParams firstObject] componentsSeparatedByString:@"/"];
        NSString *functionName = [urlParmes lastObject];

        // 获取JS传过来的参数
        NSArray *params = [[requestParams lastObject] componentsSeparatedByString:@"&"];
        NSMutableDictionary *paramsDic = [NSMutableDictionary dictionary];
        for (NSString *str in params) {
            NSArray *dicArray = [str componentsSeparatedByString:@"="];
            if (dicArray.count > 1) {
                NSString *decodeValue = [dicArray[1] stringByRemovingPercentEncoding];
                paramsDic[dicArray[0]] = decodeValue;
            }
        }

        NSString *jsonStr = paramsDic[@"params"];
        NSString *callbackId = paramsDic[@"callbackId"];
        [STCommunication callFunction:functionName jsonStr:jsonStr callBackId:callbackId callback:^(id result, NSString *callBackId) {
            NSString *jsStr = [NSString stringWithFormat:@"window.bridge.onCallback(%@,'%@')", callBackId, result];
            [self.webView evaluateJavaScript:jsStr completionHandler:^(id _Nullable item, NSError *_Nullable error) {
                NSLog(@"alert pxq");
            }];
        }];
    }
}


@end
