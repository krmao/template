//
//  STFlutterMultipleInitializer.h
//  lib_flutter_base
//
//  Created by krmao on 2021/3/9.
//

#import <Foundation/Foundation.h>
#import <Flutter/Flutter.h>

NS_ASSUME_NONNULL_BEGIN

@interface STFlutterMultipleInitializer : NSObject

@property(nonatomic, readonly) BOOL debug;
@property(nonatomic, strong, nullable, readonly) UIApplication* application;
@property(nonatomic, strong, nullable, readonly) FlutterEngineGroup* flutterEngineGroup;
@property(nonatomic, readonly) BOOL isInitialized;

-(void) initial:(UIApplication*)application debug:(BOOL)debug;
+(instancetype) sharedInstance;
@end

NS_ASSUME_NONNULL_END
