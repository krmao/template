//
//  STViewControllerDelegateImpl.h
//  lib_flutter_base
//
//  Created by krmao on 2021/3/21.
//

#import <Foundation/Foundation.h>
#import "STFlutterMultipleInitializer.h"
#import "STStringUtil.h"
#import "LibFlutterBaseMultiplePlugin.h"
#import <LibIosBase/STJsonUtil.h>
#import "STViewControllerDelegate.h"

NS_ASSUME_NONNULL_BEGIN

@interface STViewControllerDelegateImpl : NSObject<STViewControllerDelegate>

- (instancetype _Nonnull)initWithCurrentViewController:(UIViewController *_Nonnull)currentViewController;
- (void)onDealloc;

@end

NS_ASSUME_NONNULL_END
