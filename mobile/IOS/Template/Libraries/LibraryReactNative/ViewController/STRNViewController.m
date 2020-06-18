#import "STRNViewController.h"
#import "STRNView.h"
#import "STDragNavigationController.h"

#define kNativeDragToBackFinish @"nativeDragToBackFinish"

@interface STRNViewController ()

@property(nonatomic, strong) STRNURL *url;
@property(nonatomic, strong) STRNView *stRNView;
@property(nonatomic, strong) UIViewController *vc;
@property(nonatomic, strong) NSDictionary *initialProperties;
@property(nonatomic, strong) RCTBridge *rctBridge;

@end

@implementation STRNViewController

- (instancetype)initWithURL:(STRNURL *)url andInitialProperties:(NSDictionary *)initialProperties {

    if (self = [super init]) {
        self.url = url;
        self.initialProperties = initialProperties;
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(dragToBackGuestureEnd)
                                                     name:kDragToBackGuestureEnd
                                                   object:nil];
    }

    return self;
}


- (void)viewDidLoad {
    [super viewDidLoad];
    [self initUI];
}

- (void)dealloc {
}

- (void)initUI {
    [self.stRNView removeFromSuperview];
    self.view.backgroundColor = [UIColor whiteColor];
    self.stRNView = [[STRNView alloc] initWithURL:self.url initialProperties:self.initialProperties launchOptions:nil];
    self.stRNView.frame = self.view.bounds;
    [self.view addSubview:self.stRNView];

    [self.stRNView loadSTRNViewWithURL:self.url];
    self.rctBridge = self.stRNView.bridge;

}

/**
 解决导航手势问题
 */
- (void)dragToBackGuestureEnd {
    [self.rctBridge enqueueJSCall:@"RCTDeviceEventEmitter.emit" args:@[kNativeDragToBackFinish]];
}

- (STRNURL *)rnRUL {
    return self.url;
}

@end
