//
//  STFlutterMultipleUtils.h
//  lib_flutter_base
//
//  Created by krmao on 2021/3/9.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface STFlutterMultipleUtils : NSObject

+(void) openNewFlutterViewControllerBySchema:(UIViewController* _Nullable)fromViewController schemaUrl:(NSString* _Nullable) schemaUrl;
+(void) openNewFlutterViewControllerByName:(UIViewController* _Nullable)fromViewController pageName:(NSString* _Nullable) pageName pageParams:(NSDictionary* _Nullable) pageParams;
+(void) openNewFlutterHomeViewControllerByName:(UIViewController* _Nullable)fromViewController pageName:(NSString* _Nullable) pageName pageParams:(NSDictionary* _Nullable) pageParams;
+(void) popRoute:(UIViewController* _Nullable)fromViewController;

@end
NS_ASSUME_NONNULL_END
