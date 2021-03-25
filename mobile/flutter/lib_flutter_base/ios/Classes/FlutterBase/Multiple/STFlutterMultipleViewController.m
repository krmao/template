#import "STFlutterMultipleViewController.h"
#import "STFlutterMultipleInitializer.h"
#import "LibFlutterBaseMultiplePlugin.h"
#import "STViewControllerDelegate.h"
#import "STViewControllerDelegateImpl.h"
#import "STFlutterEventPlugin.h"
#import <LibIosBase/STInitializer.h>
#import <LibIosBase/STJsonUtil.h>
#import <LibIosBase/STThreadUtil.h>
#import <LibIosBase/STStringUtil.h>

@interface STFlutterMultipleViewController (){
    STViewControllerDelegateImpl *viewControllerDelegete;
    LibFlutterBaseMultiplePlugin *bridgePlugin;
}
@end

@implementation STFlutterMultipleViewController

- (instancetype _Nonnull) initWithDartEntrypointFunctionName:(NSString *_Nullable)dartEntrypointFunctionName{
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
    
    self->bridgePlugin = [LibFlutterBaseMultiplePlugin getPlugin:newEngine];
    [self->bridgePlugin setCurrentViewController:self];

    if(self = [super initWithEngine:newEngine nibName:nil bundle:nil]){
        self->viewControllerDelegete = [[STViewControllerDelegateImpl alloc] initWithCurrentViewController:self];
        NSLog(@"initWithDartEntrypointFunctionName success");
    }
    
    return self;
}

- (UIViewController *_Nonnull) currentViewController{
    return [self->viewControllerDelegete currentViewController];
}

- (NSString *)getUniqueId{
    return [self->viewControllerDelegete getUniqueId];
}

- (void)setRequestData:(int)requestCode requestData:(NSDictionary *)requestData{
    [self->viewControllerDelegete setRequestData:requestCode requestData:requestData];
}

- (int)getRequestCode{
    return [self->viewControllerDelegete getRequestCode];
}

- (NSDictionary *)getRequestData{
    return [self->viewControllerDelegete getRequestData];
}

- (void)onViewControllerResult:(int)requestCode resultCode:(int)resultCode resultData:(NSDictionary *_Nullable)resultData{
    [self->viewControllerDelegete onViewControllerResult:requestCode resultCode:resultCode resultData:resultData];

    NSString *argumentsJsonString = resultData[@"argumentsJsonString"];
    NSDictionary * eventInfo =  [STJsonUtil dictionaryWithJsonString:argumentsJsonString] ?: NSMutableDictionary.new;
    
    [STFlutterEventPlugin sendEventToDart:self eventKey:@"KEY_ARGUMENTS_JSON_STRING" eventInfo:eventInfo];
}

- (void)onFlutterUiDisplayed{
    NSLog(@"[page] onFlutterUiDisplayed uniqueId=%@, self=%@", [self getUniqueId], self);
}

- (LibFlutterBaseMultiplePlugin *)getBridgePlugin{
    return self->bridgePlugin;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self->viewControllerDelegete viewDidLoad];
    NSString *uniqueId = [self getUniqueId];
    int requestCode = [self getRequestCode];
    NSDictionary *requestData = [self getRequestData];
    NSLog(@"[page] viewDidLoad uniqueId=%@, requestCode=%d, requestData=%@", uniqueId, requestCode, requestData);
    
    self.view.backgroundColor = [UIColor clearColor];
    
    // [self setInitialRoute:@"/"];
    __weak typeof(self)weakSelf = self;
    [self setFlutterViewDidRenderCallback:^{
        __strong typeof(self) strongSelf = weakSelf;
        if (strongSelf) {
            NSLog(@"[page] viewDidLoad uniqueId=%@ onFlutterViewDidRenderCallback", [strongSelf getUniqueId]);
            [strongSelf onFlutterUiDisplayed];
        } else {
            NSLog(@"[page] viewDidLoad onFlutterViewDidRenderCallback released strongSelf==nil");
            return;
        }
    }];
    
    #if defined(DEBUG)
        NSLog(@"BUILD-TYPE -> DEBUG");
    #else
        NSLog(@"BUILD-TYPE -> RELEASE");
    #endif
    
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self->viewControllerDelegete viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    [self->viewControllerDelegete viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    [self->viewControllerDelegete viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
    [self->viewControllerDelegete viewDidDisappear:animated];
}

- (void)didReceiveMemoryWarning{
    [super didReceiveMemoryWarning];
    [self->viewControllerDelegete didReceiveMemoryWarning];
}

- (void)dealloc{
    [self->viewControllerDelegete onDealloc];
    self->viewControllerDelegete = nil;
    [[self getBridgePlugin] setCurrentViewController:nil];
    self->bridgePlugin = nil;
}
@end
