#import "STRNDispatcher.h"
#import "STRNViewController.h"
#import "UIViewController+TopMostViewController.h"

@implementation STRNDispatcher

+ (BOOL)dispatcherURLString:(NSString *)urlString {
    if (![STRNURL isSTRNURL:urlString]) {
        return NO;
    }
    STRNURL *url = [[STRNURL alloc] initWithPath:urlString];
    return [self dispatcherURL:url];
}

+ (BOOL)dispatcherURL:(STRNURL *)url {
    
    if (url == NULL) {
        return NO;
    }
    
    STRNViewController *rnVc = [[STRNViewController alloc] initWithURL:url andInitialProperties:nil];
    
    [[UIViewController topMostViewController].navigationController pushViewController:rnVc animated:YES];
    
    return YES;
}

@end
