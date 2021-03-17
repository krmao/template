//
//  STFlutterInitializer.h
//  lib_flutter_base
//
//  Created by krmao on 2021/3/9.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface STFlutterInitializer : NSObject

@property(nonatomic, readonly) BOOL debug;
@property(nonatomic, readonly) BOOL enableMultiple;
@property(nonatomic, readonly) BOOL enableMultiEnginesWithSingleRoute;
@property(nonatomic, strong, nullable, readonly) UIApplication* application;
@property(nonatomic, readonly) BOOL isInitialized;

-(void) initial:(UIApplication*)application debug:(BOOL)debug;
+(instancetype) sharedInstance;
@end

NS_ASSUME_NONNULL_END
