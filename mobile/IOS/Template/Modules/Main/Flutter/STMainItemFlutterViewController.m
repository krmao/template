#import "STMainItemFlutterViewController.h"
//#import <FlutterPluginRegistrant/GeneratedPluginRegistrant.h>

@interface STMainItemFlutterViewController ()

/**
 * MRC（Manual Reference Counting） 手动引用计数
 * assign:
 *      assign一般用来修饰基本的数据类型，包括基础数据类型 （NSInteger，CGFloat）和C数据类型（int, float, double, char,
 *      等等），为什么呢？assign声明的属性是不会增加引用计数的，也就是说声明的属性释放后，就没有了，即使其他对象用到了它，也无法留住它，只会crash。
 *      但是，即使被释放，指针却还在，成为了野指针，如果新的对象被分配到了这个内存地址上，又会crash，
 *      所以一般只用来声明基本的数据类型，因为它们会被分配到栈上，而栈会由系统自动处理，不会造成野指针。
 *
 * retain：
 *      与assign相对，我们要解决对象被其他对象引用后释放造成的问题，就要用retain来声明。retain声明后的对象会更改引用计数，那么每次被引用，
 *      引用计数都会+1，释放后就会-1，即使这个对象本身释放了，只要还有对象在引用它，就会持有，不会造成什么问题，
 *      只有当引用计数为0时，就被dealloc析构函数回收内存了。
 *
 * copy：
 *      最常见到copy声明的应该是NSString。copy与retain的区别在于retain的引用是拷贝指针地址，而copy是拷贝对象本身，也就是说retain是浅复制，
 *      copy是深复制，如果是浅复制，当修改对象值时，都会被修改，而深复制不会。之所以在NSString这类有可变类型的对象上使用，
 *      是因为它们有可能和对应的可变类型如NSMutableString之间进行赋值操作，为了防止内容被改变，使用copy去深复制一份。
 *      copy工作由copy方法执行，此属性只对那些实现了NSCopying协议的对象类型有效 。
 *
 * 以上三个可以在MRC中使用，但是weak和strong就只能在ARC中使用，也就是自动引用计数，这时就不能手动去进行retain、release等操作了，ARC会帮我们完成这些工作。
 *
 * ARC（Automatic Reference Counting） 自动引用计数
 * weak：
 *      weak其实类似于assign，叫弱引用，也是不增加引用计数。一般只有在防止循环引用时使用，比如父类引用了子类，子类又去引用父类。
 *      IBOutlet、Delegate一般用的就是weak，这是因为它们会在类外部被调用，防止循环引用。
 *
 * strong：默认 strong
 *      相对的，strong就类似与retain了，叫强引用，会增加引用计数，类内部使用的属性一般都是strong修饰的，
 *      现在ARC已经基本替代了MRC，所以我们最常见的就是strong了。
 *
 * nonatomic：默认 atomic
 *      在修饰属性时，我们往往还会加一个nonatomic，这又是什么呢？它的名字叫非原子访问。对应的有atomic，是原子性的访问。
 *      我们知道，在使用多线程时为了避免在写操作时同时进行写导致问题，经常会对要写的对象进行加锁，也就是同一时刻只允许一个线程去操作它。
 *      如果一个属性是由atomic修饰的，那么系统就会进行线程保护，防止多个写操作同时进行。这有好处，但也有坏处，那就是消耗系统资源，
 *      所以对于iPhone这种小型设备，如果不是进行多线程的写操作，就可以使用nonatomic，取消线程保护，提高性能。
 *
 */
@property(nonatomic, strong) UIButton *bridgeButton;

@end

@implementation STMainItemFlutterViewController

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
    self.view.backgroundColor = UIColor.systemPinkColor;

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

    // 订阅事件
    [STEventSubscriber addTarget:self name:@"event_flutter" priority:STEventSubscriberPriorityDefault inMainTread:YES action:^(STEventUserInfo *info) {
        NSLog(@"%s, receive event:%@, thread:%@", __FUNCTION__, [info description], [NSThread currentThread]);
        [self.bridgeButton setTitle:@"CHANGED BY HOME" forState:UIControlStateNormal];
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

        [_bridgeButton setTitle:@"GOTO FLUTTER" forState:UIControlStateNormal];
        [_bridgeButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];

        // [_bridgeButton addTarget:self action:@selector(goToFlutter) forControlEvents:UIControlEventTouchUpInside];

        __weak typeof(self) weakSelf = self;
        [_bridgeButton setOnClickListener:^{
            [weakSelf goToFlutter];
        }];

    }
    return _bridgeButton;
}

- (void)goToFlutter {
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);

    // self.flutterEngine = [[FlutterEngine alloc] initWithName:@"my flutter engine"];
    // [[self.flutterEngine navigationChannel] invokeMethod:@"setInitialRoute" arguments:@"smart://template/flutter?page=bridge&params="];
    // [self.flutterEngine run];
    // [GeneratedPluginRegistrant registerWithRegistry:self.flutterEngine];
    // FlutterViewController *flutterViewController = [[FlutterViewController alloc] initWithEngine:self.flutterEngine nibName:nil bundle:nil];

//    FlutterViewController *flutterViewController = [[FlutterViewController alloc] initWithProject:nil nibName:nil bundle:nil];
//    [GeneratedPluginRegistrant registerWithRegistry:flutterViewController];
//    [flutterViewController setInitialRoute:@"smart://template/flutter?page=bridge&params="];
//
//    FlutterMethodChannel *methodChannel = [FlutterMethodChannel methodChannelWithName:@"smart.template.flutter/method" binaryMessenger:flutterViewController.binaryMessenger];
//
//    [methodChannel setMethodCallHandler:^(FlutterMethodCall *call, FlutterResult result) {
//        NSLog(@"%s, method=%@, arguments=%@", __FUNCTION__, call.method, call.arguments);
//    }];
//    [self.navigationController pushViewController:flutterViewController animated:YES];
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

    // 取消订阅
    [STEventSubscriber removeTarget:self];
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
