#import "STFlutterMultipleViewController.h"
#import "STFlutterMultipleInitializer.h"
#import "STStringUtil.h"
#import "LibFlutterBaseMultiplePlugin.h"
#import <LibIosBase/STJsonUtil.h>
#import "STViewControllerDelegete.h"

@interface STFlutterMultipleViewController (){
    LibFlutterBaseMultiplePlugin *bridgePlugin;
    int requestCode;
    NSDictionary *requestData;
}
@end

@implementation STFlutterMultipleViewController

- (instancetype _Nonnull) initWithDartEntrypointFunctionName:(NSString *_Nullable)dartEntrypointFunctionName argumentsJsonString:(NSString * _Nullable)argumentsJsonString {
    _argumentsJsonString = argumentsJsonString;
    
    NSString *finalDartEntrypointFunctionName = [STStringUtil emptyOrNull:dartEntrypointFunctionName] ? @"main" : dartEntrypointFunctionName;
    FlutterEngine * newEngine = [[STFlutterMultipleInitializer sharedInstance].flutterEngineGroup makeEngineWithEntrypoint:finalDartEntrypointFunctionName libraryURI:nil];
    
    // register plugins
    Class clazz = NSClassFromString(@"GeneratedPluginRegistrant");
    if (clazz) {
        if ([clazz respondsToSelector:NSSelectorFromString(@"registerWithRegistry:")]) {
            SEL selector = NSSelectorFromString(@"registerWithRegistry:");
            IMP methodForSelectorIMP = [clazz methodForSelector:selector];
            void (*performSelector)(id, SEL, FlutterEngine *) = (void *)methodForSelectorIMP;
            performSelector(clazz, selector, newEngine);
            // PerformSelector may cause a leak because its selector is unknown
            // [clazz performSelector:NSSelectorFromString(@"registerWithRegistry:") withObject:newEngine];
        }
    }
    
    self->bridgePlugin = [LibFlutterBaseMultiplePlugin registerWithRegistrar2:[newEngine registrarForPlugin:@"LibFlutterBaseMultiplePlugin2"]];
    
    if(self = [super initWithEngine:newEngine nibName:nil bundle:nil]){
        NSLog(@"initWithDartEntrypointFunctionName success");
    }
    return self;
}

- (void)setRequestData:(int)requestCode requestData:(NSDictionary *)requestData{
    NSLog(@"[page] setRequestData uniqueId=%@, self=%@, requestCode=%d, requestData=%@", _uniqueId, self, requestCode, requestData);
    self->requestCode = requestCode;
    self->requestData = requestData;
}

- (int)getRequestCode{
    return self->requestCode;
}

- (NSDictionary *)getRequestData{
    return self->requestData;
}

- (void)onViewControllerResult:(int)requestCode resultCode:(int)resultCode resultData:(NSDictionary *)resultData{
    NSLog(@"[page] onViewControllerResult uniqueId=%@, self=%@, requestCode=%d, resultCode=%d, resultData=%@", _uniqueId, self, requestCode, resultCode, resultData);
    NSString *argumentsJsonString = resultData[@"KEY_JSON_OBJECT_STRING"];
    NSDictionary * eventInfo =  [STJsonUtil dictionaryWithJsonString:argumentsJsonString];
    if (eventInfo == nil) {
        eventInfo = NSMutableDictionary.new;
    }
    // [LibFlutterBaseMultiplePlugin sendEventToDart:self.engine eventKey:@"KEY_ARGUMENTS_JSON_STRING" eventInfo:eventInfo];
    [LibFlutterBaseMultiplePlugin sendEventToDart2:self->bridgePlugin eventKey:@"KEY_ARGUMENTS_JSON_STRING" eventInfo:eventInfo];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    _uniqueId  = [NSString stringWithFormat:@"%f-%lu", [[NSDate date] timeIntervalSince1970]*1000, (unsigned long)self.hash]; // *1000 是精确到毫秒，不乘就是精确到秒
    NSLog(@"[page] viewDidLoad uniqueId=%@, self=%@", _uniqueId, self);
    
    // [self setInitialRoute:@"/"];
    __weak typeof(self)weakSelf = self;
    [self setFlutterViewDidRenderCallback:^{
        __strong typeof(self) strongSelf = weakSelf;
        if (strongSelf) {
            NSLog(@"[page] viewDidLoad uniqueId=%@ onFlutterViewDidRenderCallback", strongSelf->_uniqueId);
            [strongSelf onFlutterUiDisplayed];
        } else {
            NSLog(@"[page] viewDidLoad onFlutterViewDidRenderCallback released strongSelf==nil");
            return;
        }
    }];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    NSLog(@"[page] viewWillAppear uniqueId=%@, self=%@", _uniqueId, self);
}

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    NSLog(@"[page] viewDidAppear uniqueId=%@, self=%@", _uniqueId, self);
}

- (void)onViewControllerResult:(NSString * _Nullable) argumentsJsonString{
    NSLog(@"[page] onViewControllerResult uniqueId=%@, self=%@, argumentsJsonString=%@", _uniqueId, self, argumentsJsonString);
    NSDictionary * eventInfo =  [STJsonUtil dictionaryWithJsonString:argumentsJsonString];
    if (eventInfo == nil) {
        eventInfo = NSMutableDictionary.new;
    }
    // [LibFlutterBaseMultiplePlugin sendEventToDart:self.engine eventKey:@"KEY_ARGUMENTS_JSON_STRING" eventInfo:eventInfo];
    [LibFlutterBaseMultiplePlugin sendEventToDart2:self->bridgePlugin eventKey:@"KEY_ARGUMENTS_JSON_STRING" eventInfo:eventInfo];
}

- (void)onFlutterUiDisplayed{
    NSLog(@"[page] onFlutterUiDisplayed uniqueId=%@, self=%@", _uniqueId, self);
}

- (void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    NSLog(@"[page] viewWillDisappear uniqueId=%@, self=%@", _uniqueId, self);
}

/**
 * 生命周期
 * https://seniorzhai.github.io/2014/12/11/Android%E3%80%81iOS%E5%A4%A7%E4%B8%8D%E5%90%8C%E2%80%94%E2%80%94%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F/
 */
- (void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
    NSLog(@"[page] viewDidDisappear uniqueId=%@, self=%@", _uniqueId, self);
}

- (void)didReceiveMemoryWarning{
    [super didReceiveMemoryWarning];
    NSLog(@"[page] didReceiveMemoryWarning uniqueId=%@, self=%@", _uniqueId, self);
}

- (void)dealloc{
    NSLog(@"[page] dealloc uniqueId=%@, self=%@", _uniqueId, self);
}

@end
