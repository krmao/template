#import <UIKit/UIKit.h>
#import <flutter_boost/FlutterBoost.h>

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property(nullable, nonatomic, strong) UIWindow *window;

+ (UIViewController *_Nonnull)initRootViewController;

+ (void)initApplication;
@end

