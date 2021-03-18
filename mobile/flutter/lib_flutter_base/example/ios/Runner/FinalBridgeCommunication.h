//
//  FinalBridgeCommunication.h
//  LibIosBase
//
//  Created by krmao on 2021/2/9.
//

#import <Foundation/Foundation.h>
#import <LibIosBase/STInitializer.h>
#import <UIKit/UIKit.h>

@interface FinalBridgeCommunication : NSObject

+(void) handleBridge:(nullable UIViewController *) viewController functionName:(nullable NSString *) functionName params:(nullable NSString *) params callBackId:(nullable NSString *) callBackId callback: (nullable BridgeHandlerCallback) callback;
@end
