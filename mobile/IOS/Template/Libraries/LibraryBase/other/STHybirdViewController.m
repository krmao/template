#import "STHybirdViewController.h"
#import "STCommunication.h"

#define kURLPrefix          @"smart://hybird/bridge/"
#define kURLString          @"http://10.32.33.2:5389/#/"

@interface STHybirdViewController ()

@end

@implementation STHybirdViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    UIWebView *webView = [[UIWebView alloc] initWithFrame:CGRectMake(0, 100, self.view.frame.size.width, self.view.frame.size.height-200)];
    webView.delegate = self;
    webView.backgroundColor = [UIColor grayColor];
    [self.view addSubview:webView];
    self.webView = webView;
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:kURLString]];
    [self.webView loadRequest:request];
}

- (instancetype)initWithURL:(NSString*)url
{
    self = [super init];
    if (self)
    {
        self.url = url;
    }
    return self;
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    NSString *urlStr = request.URL.absoluteString;
    if ([urlStr hasPrefix:kURLPrefix]) {
        
        //1、获取JS传过来的参数
        NSArray *params =[request.URL.query componentsSeparatedByString:@"&"];
        NSMutableDictionary *paramsDic = [NSMutableDictionary dictionary];
        for (NSString *str in params) {
            NSArray *dicArray = [str componentsSeparatedByString:@"="];
            if (dicArray.count > 1) {
                NSString *decodeValue = [dicArray[1] stringByRemovingPercentEncoding];
                [paramsDic setObject:decodeValue forKey:dicArray[0]];
            }
        }
        
        //2、获取JS的方法名
        NSArray *requestParams = [urlStr componentsSeparatedByString:@"?"];
        NSArray *urlParmes = [[requestParams firstObject] componentsSeparatedByString:@"/"];
        NSString *functionName = [urlParmes lastObject];
        
        NSString *jsonStr = [paramsDic objectForKey:@"params"];
        NSString *callbackId = [paramsDic objectForKey:@"callbackId"];
        [STCommunication callFunction:functionName jsonStr:jsonStr callBackId:callbackId callback:^(id result, NSString *callBackId) {
            NSString *jsStr = [NSString stringWithFormat:@"window.bridge.onCallback(%@, '{\"key\":\"userName\"}')", callBackId];
            [self.webView stringByEvaluatingJavaScriptFromString:jsStr];
        }];
        
        return NO;
    }
    
    return YES;
}


@end
