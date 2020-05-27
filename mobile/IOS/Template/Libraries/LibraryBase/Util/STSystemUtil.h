#import <Foundation/Foundation.h>

#pragma clang diagnostic push
#pragma ide diagnostic ignored "UnusedMethodInspection"
NS_ASSUME_NONNULL_BEGIN

@interface STSystemUtil : NSObject
+ (NSString *)systemName;

+ (double)systemVersion;

+ (NSString *)appName;

+ (NSString *)appVersion;

+ (NSString *)appBuildVersion;

+ (NSString *)appHomeDirectory;

+ (NSString *)appDocumentDirectory;

+ (NSString *)appLibraryDirectory;

+ (NSString *)appCachesDirectory;

+ (NSString *)appTemporaryDirectory;

+ (NSString *)deviceName;

+ (NSString *)deviceModel;

+ (NSString *)deviceUUID;

+ (NSString *)deviceMachineType;

+ (NSString *)deviceMachineTypeName;

+ (CGFloat)deviceBatteryLevel;

+ (CGFloat)deviceNavigationBarHeight;

+ (CGFloat)deviceStatusBarHeight;

+ (CGFloat)deviceNavigationBarAndStatusBarHeight;

+ (CGFloat)deviceTabBarHeight;

+ (CGFloat)deviceSafeAreaBottom;

+ (CGFloat)deviceSafeAreaTop;

+ (CGFloat)deviceSafeAreaLeft;

+ (CGFloat)deviceSafeAreaRight;

+ (UIEdgeInsets)deviceSafeAreaInsets;

+ (CGFloat)deviceScreenHeight;

+ (CGFloat)deviceScreenWidth;

+ (CGFloat)deviceScreenMaxLength;

+ (CGFloat)deviceScreenMinLength;


+ (BOOL)isIphone;

+ (BOOL)isIphone4OrLess;

+ (BOOL)isIphone5Or5S;

+ (BOOL)isIphone6Or7Or8;

+ (BOOL)isIphone6POr7POr8P;

+ (BOOL)isIphoneXOrMore;

+ (BOOL)isHaveSafeArea;

+ (UIWindow *)firstWindow;

+ (UIViewController *)rootViewController;

+ (UIViewController *)topViewController;

// 计算单行文字的size
+ (CGSize)getTextSize:(NSString *)text withFont:(UIFont *)font;

// 计算多行文字的CGRect
+ (CGRect)getTextSizeWithBoundingRect:(NSString *)str withSize:(CGSize)size withFont:(UIFont *)font;

+ (void)hideKeyboard:(UITextField *)textField;

+ (void)addHideButtonForKeyboard:(UITextField *)textField target:(nullable id)target action:(nullable SEL)action;

+ (void)printSystemInfo;
@end

NS_ASSUME_NONNULL_END

#pragma clang diagnostic pop