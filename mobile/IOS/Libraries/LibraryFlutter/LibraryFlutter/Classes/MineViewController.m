//
//  MineViewController.m
//  LibraryFlutter
//
//  Created by krmao on 2019/9/4.
//

#import "MineViewController.h"

@interface MineViewController ()

@end

@implementation MineViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    UIButton *nativeButton = [UIButton buttonWithType:UIButtonTypeCustom];
    nativeButton.frame = [UIScreen.mainScreen bounds];
    nativeButton.backgroundColor = [UIColor grayColor];
    [nativeButton setTitle:@"native mine page" forState:UIControlStateNormal];
    [nativeButton addTarget:self action:@selector(onClick) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:nativeButton];
}

- (void)onClick {
    NSLog(@"you clicked");
}


@end
