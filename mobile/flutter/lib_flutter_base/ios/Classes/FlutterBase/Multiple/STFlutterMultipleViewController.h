#import <Flutter/Flutter.h>
#import "STViewControllerDelegate.h"
#import "LibFlutterBaseMultiplePlugin.h"

@interface STFlutterMultipleViewController : FlutterViewController<STViewControllerDelegate>

- (instancetype _Nonnull) initWithDartEntrypointFunctionName:(NSString *_Nullable)dartEntrypointFunctionName;
- (void)onFlutterUiDisplayed;
- (LibFlutterBaseMultiplePlugin *_Nonnull)getBridgePlugin;

@end
