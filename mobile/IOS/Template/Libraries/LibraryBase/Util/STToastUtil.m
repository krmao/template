//
//  STToastUtil.m
//  Template
//
//  Created by krmao on 2020/12/23.
//  Copyright © 2020 smart. All rights reserved.
//

#import "STToastUtil.h"

@implementation STToastUtil

+ (void)show:(NSString *)message{
    
    BOOL animation = YES;
    UIView *view = [STSystemUtil topViewController].view;
    
    MBProgressHUD *hud = [[MBProgressHUD alloc]init];
    [view addSubview:hud];
    
    hud.bezelView.style = MBProgressHUDBackgroundStyleSolidColor;
    hud.bezelView.color = [UIColor colorWithWhite:0.f alpha:0.f];
    hud.userInteractionEnabled = NO;
    //
    // hud.backgroundColor = [UIColor clearColor]; // fullscreen background color
    // hud.contentColor = [UIColor orangeColor];
    hud.label.text = message;
    hud.label.numberOfLines = 0;
    hud.label.textColor = [UIColor whiteColor];
    // Move to bottm center.
    hud.offset = CGPointMake(0.f, MBProgressMaxOffset);
    hud.mode = MBProgressHUDModeText;
    hud.mode = MBProgressHUDModeCustomView;
    CGRect rect = [message boundingRectWithSize:CGSizeMake(MAXFLOAT, 20) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:14]} context:nil];
    hud.minSize = CGSizeMake(rect.size.width+30, 35);
    hud.bezelView.backgroundColor = [UIColor orangeColor];
    hud.bezelView.style = MBProgressHUDBackgroundStyleBlur;
    hud.bezelView.layer.masksToBounds = NO;
    hud.bezelView.layer.backgroundColor = [UIColor blueColor].CGColor;
    hud.bezelView.layer.cornerRadius = 15.0;

    UILabel *titleLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 5,rect.size.width+30 ,30)];
    titleLabel.text = message;
    titleLabel.textAlignment = NSTextAlignmentCenter;
    titleLabel.backgroundColor = [UIColor redColor];
    titleLabel.font = [UIFont systemFontOfSize:15];
    titleLabel.layer.cornerRadius = 15;
    titleLabel.layer.masksToBounds = YES;
    titleLabel.textColor = [UIColor whiteColor];
    [hud.bezelView addSubview:titleLabel];
    
    //
    [hud setRemoveFromSuperViewOnHide:YES];
    [hud showAnimated:animation];
    [hud hideAnimated:animation afterDelay:2.0];
}

@end
