//
//  STMainItemHomeViewController.m
//  Template
//
//  Created by krmao on 2020/5/22.
//  Copyright © 2020 smart. All rights reserved.
//

#import "STMainItemHomeViewController.h"
#import "STListViewController.h"

@interface STMainItemHomeViewController ()
@property(nonatomic, strong) UIImageView *imageView;
@property(nonatomic, strong) UILabel *labelView;
@end

@implementation STMainItemHomeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.view.backgroundColor = UIColor.darkGrayColor;
    [self.view addSubview:self.imageView];
    [self.view addSubview:self.labelView];
    
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

- (void) viewWillAppear:(BOOL)animated{
    NSLog(@"lifecycle main tab item home viewWillAppear, %lu", (unsigned long)self.navigationController.viewControllers.count);
}

- (void)viewDidAppear:(BOOL)animated{
    NSLog(@"lifecycle main tab item home viewDidAppear, %lu", (unsigned long)self.navigationController.viewControllers.count);
}

- (void)viewWillDisappear:(BOOL)animated{
    NSLog(@"lifecycle main tab item home tab viewWillDisappear, %lu", (unsigned long)self.navigationController.viewControllers.count);
}

- (void)viewDidDisappear:(BOOL)animated{
    NSLog(@"lifecycle main tab item home tab viewDidDisappear, %lu", (unsigned long)self.navigationController.viewControllers.count);
}

- (UILabel *)labelView{
    if(!_labelView){
        NSLog(@"_labelView is nil");

        _labelView = [[UILabel alloc] init];
        [_labelView setText:@"UILabel"];

        NSLog(@"_labelView is nil? %@", _labelView);

        _labelView.backgroundColor = [UIColor blueColor];

        __weak typeof(self) weakSelf = self;
        [_labelView setOnClickListener:^ {
            NSLog(@"on labelView clicked, %@", weakSelf);
            [weakSelf.navigationController pushViewController:[STListViewController new] animated:YES];
        }];
    }else{
        NSLog(@"_labelView is not nil");
    }
    NSLog(@"_labelView get %@", _labelView);
    return _labelView;
}

- (UIImageView *)imageView{
    if(!_imageView){
        NSLog(@"_imageView is nil");

        _imageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"icon_flutter"]];

        NSLog(@"_imageView is nil? %@", _imageView);

        _imageView.backgroundColor = [UIColor yellowColor];

        __weak typeof(self) weakSelf = self;
        [_imageView setOnClickListener:^ {
            NSLog(@"on imageView clicked, %@", weakSelf);
            [weakSelf.navigationController pushViewController:[STListViewController new] animated:YES];
        }];
    }else{
        NSLog(@"_imageView is not nil");
    }
    NSLog(@"_imageView get %@", _imageView);
    return _imageView;
}


/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
