//
//  STFlutterUtils.h
//  lib_flutter_base
//
//  Created by krmao on 2021/3/9.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface STFlutterUtils : NSObject

+(void) openBySchema:(UIViewController* _Nullable)fromViewController schemaUrl:(NSString* _Nullable) schemaUrl;
+(void) openByName:(UIViewController* _Nullable)fromViewController pageName:(NSString* _Nullable) pageName pageParams:(NSDictionary* _Nullable) pageParams;
+(void) openHomeByName:(UIViewController* _Nullable)fromViewController pageName:(NSString* _Nullable) pageName pageParams:(NSDictionary* _Nullable) pageParams;
+ (void) popRoute:(UIViewController* _Nullable)fromViewController uniqueId:(NSString * _Nullable)uniqueId;

@end
NS_ASSUME_NONNULL_END
