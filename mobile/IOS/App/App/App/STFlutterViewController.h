//
//  FlutterViewController.h
//  App
//
//  Created by krmao on 2019/9/4.
//  Copyright © 2019 smart. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <FlutterBoost.h>

NS_ASSUME_NONNULL_BEGIN

@interface STFlutterViewController : FLBFlutterViewContainer
    
+ (STFlutterViewController *) create: (nonnull NSString *)url urlParams:(nonnull NSDictionary *)urlParams;

@end

NS_ASSUME_NONNULL_END
