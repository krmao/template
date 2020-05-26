#import "STMainItemHomeViewController.h"
#import "STListViewController.h"

@interface STMainItemHomeViewController ()
@property(nonatomic, strong) UIImageView *imageView;
@property(nonatomic, strong) UILabel *labelView;
@end

@implementation STMainItemHomeViewController

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
    self.view.backgroundColor = UIColor.darkGrayColor;
    [self.view addSubview:self.imageView];
    [self.view addSubview:self.labelView];
}

- (void) viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long)self.navigationController.viewControllers.count);
}

/**
 * Called to notify the view controller that its view is about to layout its subviews. This method is called every time the frame changes like for example when rotate or it’s marked as needing layout.
 * It’s the first step where the view bounds are final. If you are not using autoresizing masks or constraints and the view size changes you probably want to update the subviews here.
 */
- (void)viewWillLayoutSubviews {
    [super viewWillLayoutSubviews];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long)self.navigationController.viewControllers.count);
    [self.imageView makeConstraints:^(MASConstraintMaker *make) {
       if (@available(iOS 11,*)) {
           make.left.equalTo(self.view.safeAreaLayoutGuideLeft);
           make.bottom.equalTo(self.view.safeAreaLayoutGuideBottom);
           make.width.equalTo(self.view).multipliedBy(0.5);
           make.height.equalTo(50);
       }else{
           make.left.equalTo(self.view);
           make.bottom.equalTo(self.view);
           make.width.equalTo(self.view).multipliedBy(0.5);
           make.height.equalTo(50);
       }
    }];
    [self.labelView makeConstraints:^(MASConstraintMaker *make) {
           make.left.equalTo(self.imageView);
           make.bottom.equalTo(self.imageView.top).offset(10);
           make.width.equalTo(self.view).multipliedBy(0.5);
           make.height.equalTo(50);
    }];
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

- (UILabel *)labelView{
    if(!_labelView){
        _labelView = [[UILabel alloc] init];
        [_labelView setText:@"UILabel"];

        _labelView.backgroundColor = [UIColor blueColor];

        __weak typeof(self) weakSelf = self;
        [_labelView setOnClickListener:^ {
            NSLog(@"%s, %lu", __FUNCTION__, (unsigned long)weakSelf.navigationController.viewControllers.count);
            [weakSelf.navigationController pushViewController:[STListViewController new] animated:YES];
        }];
    }
    return _labelView;
}

- (UIImageView *)imageView{
    if(!_imageView){
        _imageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"icon_flutter"]];
        _imageView.backgroundColor = [UIColor yellowColor];

        __weak typeof(self) weakSelf = self;
        [_imageView setOnClickListener:^ {
            NSLog(@"%s, %lu", __FUNCTION__, (unsigned long)weakSelf.navigationController.viewControllers.count);
            [weakSelf.navigationController pushViewController:[STListViewController new] animated:YES];
        }];
    }
    return _imageView;
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
