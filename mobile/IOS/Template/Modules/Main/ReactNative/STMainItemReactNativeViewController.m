#import "STMainItemReactNativeViewController.h"

@interface STMainItemReactNativeViewController ()

@end

@implementation STMainItemReactNativeViewController

- (instancetype)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
        NSLog(@"%s, %lu", __FUNCTION__, (unsigned long)self.navigationController.viewControllers.count);
    }
    return self;
}

- (instancetype)initWithCoder:(NSCoder *)aDecoder {
    if(self == [super initWithCoder:aDecoder]){
        NSLog(@"%s, %lu", __FUNCTION__, (unsigned long)self.navigationController.viewControllers.count);
    }
    return self;
}

- (void)awakeFromNib {
    [super awakeFromNib];
     NSLog(@"%s, %lu", __FUNCTION__, (unsigned long)self.navigationController.viewControllers.count);
}

- (void)loadView {
    [super loadView];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long)self.navigationController.viewControllers.count);
}

- (void)viewDidLoad {
    [super viewDidLoad];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long)self.navigationController.viewControllers.count);
    
    // Do any additional setup after loading the view.
    self.view.backgroundColor = UIColor.blueColor;
}

- (void) viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long)self.navigationController.viewControllers.count);
}

- (void)viewWillLayoutSubviews {
    [super viewWillLayoutSubviews];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long)self.navigationController.viewControllers.count);
}

- (void)viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long)self.navigationController.viewControllers.count);
}

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long)self.navigationController.viewControllers.count);
}

- (void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long)self.navigationController.viewControllers.count);
}

- (void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long)self.navigationController.viewControllers.count);
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long)self.navigationController.viewControllers.count);
}

- (void)dealloc {
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long)self.navigationController.viewControllers.count);
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
