#import "STMainTabBarViewController.h"

#import "STBusObject.h"
#import "STBus.h"


@interface AppDelegate ()

@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    if (@available(iOS 13.0, *)) {
        // [[UIDevice currentDevice].systemVersion doubleValue] >= 13.0
    } else {
        // [[UIDevice currentDevice].systemVersion doubleValue] < 13.0
        self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
        self.window.backgroundColor = UIColor.whiteColor;
        self.window.rootViewController = [AppDelegate initRootViewController];
        [self.window makeKeyAndVisible];

        [AppDelegate initApplication];
    }
    return YES;
}

+ (UIViewController *)initRootViewController {
    STMainTabBarViewController *rootViewController = [STMainTabBarViewController new];
    STBaseNavigationController *rootViewNavigationController = [[STBaseNavigationController alloc] initWithRootViewController:rootViewController];

    rootViewNavigationController.navigationBarHidden = YES;
    return rootViewNavigationController;
}

+ (void)initApplication {
    NSDictionary *busMap = @{@"STRNBusObject": @"rn"};
    [busMap enumerateKeysAndObjectsUsingBlock:^(id key, id obj, BOOL *stop) {
        STBusObject *busObject = [(STBusObject *) [NSClassFromString((NSString *) key) alloc] initWithHost:(NSString *) obj];
        [STBus register:busObject];
    }];
}


#pragma mark - UISceneSession lifecycle


- (UISceneConfiguration *)application:(UIApplication *)application configurationForConnectingSceneSession:(UISceneSession *)connectingSceneSession options:(UISceneConnectionOptions *)options  API_AVAILABLE(ios(13.0)) {
    // Called when a new scene session is being created.
    // Use this method to select a configuration to create the new scene with.
    return [[UISceneConfiguration alloc] initWithName:@"Default Configuration" sessionRole:connectingSceneSession.role];
}


- (void)application:(UIApplication *)application didDiscardSceneSessions:(NSSet<UISceneSession *> *)sceneSessions  API_AVAILABLE(ios(13.0)) {
    // Called when the user discards a scene session.
    // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
    // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
}


@end
