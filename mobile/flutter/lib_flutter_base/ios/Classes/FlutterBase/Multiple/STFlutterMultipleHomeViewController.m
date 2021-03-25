//
//  STFlutterMultipleHomeViewController.m
//  lib_flutter_base
//
//  Created by krmao on 2021/3/9.
//

#import "STFlutterMultipleHomeViewController.h"
#import <LibIosBase/STInitializer.h>
#import <LibIosBase/STJsonUtil.h>
#import <LibIosBase/STThreadUtil.h>

@interface STFlutterMultipleHomeViewController (){
    UIImageView *splashScreen;
}
@end

@implementation STFlutterMultipleHomeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor clearColor];
    splashScreen = [[UIImageView alloc] initWithImage:[UIImage imageNamed: @"final_launch.9.png"]];
    splashScreen.contentMode = UIViewContentModeScaleAspectFill;
    splashScreen.frame = [UIScreen mainScreen].bounds;
    [self.view addSubview:splashScreen];
}

- (void)onFlutterUiDisplayed{
    [super onFlutterUiDisplayed];
    [self->splashScreen removeFromSuperview];
}

@end
