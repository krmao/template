#import "STMainItemReactNativeViewController.h"
#import "STRNURL.h"
#import "STRNUnZip.h"
#import "STRNFileManager.h"
#import "STRNViewController.h"

@interface STMainItemReactNativeViewController ()
@property(nonatomic, strong) UIButton *bridgeButton;
@end

@implementation STMainItemReactNativeViewController

- (instancetype)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
        NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
    }
    return self;
}

- (instancetype)initWithCoder:(NSCoder *)aDecoder {
    if (self == [super initWithCoder:aDecoder]) {
        NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
    }
    return self;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)loadView {
    [super loadView];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)viewDidLoad {
    [super viewDidLoad];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);

    // Do any additional setup after loading the view.
    self.view.backgroundColor = UIColor.blueColor;

    [self.view addSubview:self.bridgeButton];

    [self.bridgeButton makeConstraints:^(MASConstraintMaker *make) {
        if (@available(iOS 11, *)) {
            make.left.equalTo(self.view.safeAreaLayoutGuideLeft);
            make.bottom.equalTo(self.view.safeAreaLayoutGuideBottom);
            make.width.equalTo(self.view).multipliedBy(0.5);
            make.height.equalTo(50);
        } else {
            make.left.equalTo(self.view);
            make.bottom.equalTo(self.view);
            make.width.equalTo(self.view).multipliedBy(0.5);
            make.height.equalTo(50);
        }
    }];
}

- (UIButton *)bridgeButton {
    if (!_bridgeButton) {
        _bridgeButton = [UIButton buttonWithType:UIButtonTypeSystem];

        _bridgeButton.backgroundColor = [UIColor systemOrangeColor];
        _bridgeButton.contentEdgeInsets = UIEdgeInsetsMake(0, 10, 0, -10);
        _bridgeButton.titleEdgeInsets = UIEdgeInsetsMake(0, -10, 0, 10);
        _bridgeButton.imageEdgeInsets = UIEdgeInsetsMake(0, -10, 0, 10);
        _bridgeButton.titleLabel.font = [UIFont systemFontOfSize:16];

        [_bridgeButton setTitle:@"GOTO REACT NATIVE" forState:UIControlStateNormal];
        [_bridgeButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];

        // [_bridgeButton addTarget:self action:@selector(goToFlutter) forControlEvents:UIControlEventTouchUpInside];

        __weak typeof(self) weakSelf = self;
        [_bridgeButton setOnClickListener:^{
            [weakSelf goToReactNative];
        }];

    }
    return _bridgeButton;
}

- (void)goToReactNative {
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);

    STRNURL *url = [[STRNURL alloc] init];
    url.rnModuleName = @"cc-rn";

    NSString *rnPath = [[NSBundle mainBundle] pathForResource:@"bundle-rn" ofType:@"zip"];
    NSLog(@"rnPath:%@", rnPath);
    [STRNUnZip unzipFileAtPath:rnPath toDestination:[STRNFileManager fileNameBaseUnzip]];
    url.rnBundleURL = [NSURL fileURLWithPath:[[STRNFileManager fileNameBaseUnzip] stringByAppendingPathComponent:@"ios/index.ios.bundle"]];

    // url.rnNetURL = [NSURL URLWithString:@"http://10.32.33.2:5387/index.bundle?platform=ios"];
    url.urlType = STRNURLBundleURL; // 从 Bundle 读取而不是 远程 url

    NSLog(@"bundle-rn path:%@ isExists=%d", [[NSBundle mainBundle] pathForResource:@"bundle-rn" ofType:@"zip"], [STRNFileManager isFileNameBasePathExit]);

    STRNViewController *reactNativeViewController = [[STRNViewController alloc] initWithURL:url andInitialProperties:@{@"page": @"bridge"}];
    reactNativeViewController.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:reactNativeViewController animated:YES];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)viewWillLayoutSubviews {
    [super viewWillLayoutSubviews];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)dealloc {
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long)self.navigationController.viewControllers.count);
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
