#import "STRNView.h"
#import "ReactBridge.h"
#import "STDragNavigationController.h"

@interface STRNView () <RCTBridgeDelegate>

@property(nonatomic, strong) RCTRootView *reactView;
@property(nonatomic, strong) STRNURL *url;
@property(nonatomic, strong) NSDictionary *initialProps;
@property(nonatomic, strong) NSDictionary *launchOptions;
@property(nonatomic, strong) RCTBridge *currentBridge;
@property(nonatomic, strong) ReactBridge *stRNBridge;

@end

@implementation STRNView

- (instancetype)initWithURL:(STRNURL *)stRNURL initialProperties:(NSDictionary *)initialProperties launchOptions:(nullable NSDictionary *)launchOptions {
    if (self = [self initWithURL:stRNURL frame:CGRectZero initialProperties:initialProperties launchOptions:launchOptions]) {
        [self loadSTRNViewWithURL:stRNURL];
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(dragToBackGuestureBegin)
                                                     name:kDragToBackGuestureBegin
                                                   object:nil];
    }
    return self;
}

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    NSLog(@"RNView页面已销毁!");
}

- (void)loadSTRNViewWithURL:(STRNURL *)url_ {
    if (url_ == nil) {
        return;
    }

    self.url = url_;
    [self loadSTRNView];
}

- (void)loadSTRNView {

    self.stRNBridge = [ReactBridge sharedManager];
    self.stRNBridge.url = self.url;
    self.reactView = [[RCTRootView alloc] initWithBridge:self.stRNBridge moduleName:self.url.rnModuleName initialProperties:self.initialProps];

    self.reactView.frame = self.bounds;
    [self addSubview:self.reactView];

    self.reactView.contentMode = UIViewContentModeCenter;
    self.reactView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;

}

- (void)dragToBackGuestureBegin {
    [self.reactView cancelTouches];
}

- (RCTBridge *)bridge {
    return _stRNBridge;
}

- (RCTRootView *)rnRootView {
    return _reactView;
}

- (id)initWithURL:(STRNURL *)stRNURL frame:(CGRect)frame initialProperties:(NSDictionary *)props launchOptions:(nullable NSDictionary *)options {

    if (self = [super initWithFrame:frame]) {
        self.url = stRNURL;
        self.initialProps = props;
        self.launchOptions = options;
    }

    return self;
}


- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge {
    return self.url.rnBundleURL;
}

@end
