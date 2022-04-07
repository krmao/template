#import <UIKit/UIKit.h>
#import <React/RCTRootView.h>
#import "STRNURL.h"

@class STRNView;

@protocol STRNViewLoadingDelegate <NSObject>

@optional
- (void)showLoadingView;

- (void)hideLoadingView;

- (void)showLoadFailView;
@end

@protocol STRNViewDelegate <NSObject>

@optional
- (void)rnViewLoadFailed:(STRNView *)view errorCode:(NSNumber *)code;

- (void)rnViewWillAppear:(STRNView *)view;

@end

NS_ASSUME_NONNULL_BEGIN

@interface STRNView : UIView

@property(nonatomic, readonly, strong) RCTRootView *rnRootView;
@property(nonatomic, weak) id <STRNViewLoadingDelegate> loadingDelegate;
@property(nonatomic, weak) id <STRNViewDelegate> viewDelegate;
@property(nonatomic) RCTBridge *bridge;

- (instancetype)initWithURL:(STRNURL *)stRNURL
          initialProperties:(NSDictionary *)initialProperties
              launchOptions:(nullable NSDictionary *)launchOptions;

- (void)loadSTRNViewWithURL:(STRNURL *)url_;

@end

NS_ASSUME_NONNULL_END
