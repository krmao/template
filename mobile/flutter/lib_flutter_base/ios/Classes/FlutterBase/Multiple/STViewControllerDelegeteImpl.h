//
//  STViewControllerDelegeteImpl.h
//  lib_flutter_base
//
//  Created by krmao on 2021/3/21.
//

#import <Foundation/Foundation.h>
#import "STFlutterMultipleInitializer.h"
#import "STStringUtil.h"
#import "LibFlutterBaseMultiplePlugin.h"
#import <LibIosBase/STJsonUtil.h>
#import "STViewControllerDelegete.h"

NS_ASSUME_NONNULL_BEGIN

@interface STViewControllerDelegeteImpl : NSObject<STViewControllerDelegete>

- (instancetype _Nonnull)initWithCurrentViewController:(UIViewController *_Nonnull)currentViewController;
- (void)onDealloc;

@end

NS_ASSUME_NONNULL_END
