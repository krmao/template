#import <Flutter/Flutter.h>

typedef void(^OnViewControllerResult)(NSString * _Nullable jsonObjectString);

@interface STFlutterMultipleViewController : FlutterViewController

@property(nonatomic, strong, nullable, readonly) NSString* uniqueId;
@property(nonatomic, copy, nullable, readonly) OnViewControllerResult onViewControllerResult;

- (instancetype _Nonnull) initWithDartEntrypointFunctionName:(NSString *_Nullable)dartEntrypointFunctionName onViewControllerResult:(OnViewControllerResult _Nullable )onViewControllerResult;
- (void)onFlutterUiDisplayed;

@end
