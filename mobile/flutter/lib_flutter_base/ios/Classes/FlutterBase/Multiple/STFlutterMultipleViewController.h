#import <Flutter/Flutter.h>

@interface STFlutterMultipleViewController : FlutterViewController

@property(nonatomic, strong, nullable, readonly) NSString* uniqueId;
@property(nonatomic, strong, nullable, readonly) NSString* argumentsJsonString;

- (instancetype _Nonnull) initWithDartEntrypointFunctionName:(NSString *_Nullable)dartEntrypointFunctionName argumentsJsonString:(NSString * _Nullable)argumentsJsonString;
- (void)onViewControllerResult:(NSString * _Nullable) argumentsJsonString;
- (void)onFlutterUiDisplayed;

@end
