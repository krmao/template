#import <Foundation/Foundation.h>
#import "STFlutterPlugin.h"

NS_ASSUME_NONNULL_BEGIN

@interface STFlutterPluginManager : NSObject

+ (STFlutterPlugin *)pluginObjectForModuleClass:(NSString *)moduleClassName;

@end

NS_ASSUME_NONNULL_END
