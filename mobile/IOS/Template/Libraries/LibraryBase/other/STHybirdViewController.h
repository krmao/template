#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface STHybirdViewController : UIViewController<UIWebViewDelegate>

@property(nonatomic, strong) UIWebView *webView;

@property(nonatomic, copy) NSString *url;

- (instancetype)initWithURL:(NSString*)url;

@end

NS_ASSUME_NONNULL_END
