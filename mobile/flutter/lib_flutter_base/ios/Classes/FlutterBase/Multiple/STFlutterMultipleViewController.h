#import <Flutter/Flutter.h>
#import "STViewControllerDelegete.h"
#import "LibFlutterBaseMultiplePlugin.h"

@interface STFlutterMultipleViewController : FlutterViewController<STViewControllerDelegete>

@property(nonatomic, strong, nullable, readonly) NSString* uniqueId;
@property(nonatomic, strong, nullable, readonly) NSString* argumentsJsonString;
@property(nonatomic, strong, nullable, readonly) LibFlutterBaseMultiplePlugin *bridgePlugin;

- (instancetype _Nonnull) initWithDartEntrypointFunctionName:(NSString *_Nullable)dartEntrypointFunctionName argumentsJsonString:(NSString * _Nullable)argumentsJsonString;
- (void)onViewControllerResult:(NSString * _Nullable) argumentsJsonString;
- (void)onFlutterUiDisplayed;

@end
