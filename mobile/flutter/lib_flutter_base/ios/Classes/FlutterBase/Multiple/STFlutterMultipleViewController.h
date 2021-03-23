#import <Flutter/Flutter.h>
#import "STViewControllerDelegete.h"
#import "LibFlutterBaseMultiplePlugin.h"

@interface STFlutterMultipleViewController : FlutterViewController<STViewControllerDelegete>

- (instancetype _Nonnull) initWithDartEntrypointFunctionName:(NSString *_Nullable)dartEntrypointFunctionName;
- (void)onFlutterUiDisplayed;
- (LibFlutterBaseMultiplePlugin *_Nonnull)getBridgePlugin;

@end
