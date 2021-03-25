#import "STFlutterPlugin.h"
#import "STFlutterPluginManager.h"
#import <Flutter/Flutter.h>
#import <LibIosBase/STInitializer.h>
#import <LibIosBase/STJsonUtil.h>
#import <LibIosBase/STThreadUtil.h>
#import <LibIosBase/STStringUtil.h>
#import <LibIosBase/STSystemUtil.h>

@implementation STFlutterPlugin

+ (void)callModule:(UIViewController *)currentViewController
        moduleName:(NSString *)moduleName
          function:(NSString *)functionName
         arguments:(id)arguments
            result:(FlutterResult)result {
    NSLog(@"[page]-[STFlutterPlugin] callModule currentViewController=%@, functionName=%@, arguments=%@", currentViewController, functionName, arguments);
    NSString *moduleClassName = [[@"STFlutter" stringByAppendingString:moduleName] stringByAppendingString:@"Plugin"];
    STFlutterPlugin *object = [STFlutterPluginManager pluginObjectForModuleClass:moduleClassName];
    if (object){
        [object callFunction:currentViewController functionName:functionName arguments:arguments result:result];
    }
    else {
        result(FlutterMethodNotImplemented);
    }
}

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result {
    //subclass override.
}

@end
