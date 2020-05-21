#import "SceneDelegate.h"
#import <FlutterPluginRegistrant/GeneratedPluginRegistrant.h>
#import "STNavigationController.h"

@interface SceneDelegate ()

@end

@implementation SceneDelegate


- (void)scene:(UIScene *)scene willConnectToSession:(UISceneSession *)session options:(UISceneConnectionOptions *)connectionOptions {
    // Use this method to optionally configure and attach the UIWindow `window` to the provided UIWindowScene `scene`.
    // If using a storyboard, the `window` property will automatically be initialized and attached to the scene.
    // This delegate does not imply the connecting scene or session are new (see `application:configurationForConnectingSceneSession` instead).
   
    /*
    self.flutterEngine = [[FlutterEngine alloc] initWithName:@"my flutter engine"];
    [[self.flutterEngine navigationChannel] invokeMethod:@"setInitialRoute" arguments:@"smart://template/flutter?page=bridge&params="];
    [self.flutterEngine run];
    [GeneratedPluginRegistrant registerWithRegistry:self.flutterEngine];
    FlutterViewController *flutterViewController = [[FlutterViewController alloc] initWithEngine:self.flutterEngine nibName:nil bundle:nil];
     */

     FlutterViewController *flutterViewController = [[FlutterViewController alloc] initWithProject:nil nibName:nil bundle:nil];
     [GeneratedPluginRegistrant registerWithRegistry:flutterViewController];
     [flutterViewController setInitialRoute:@"smart://template/flutter?page=bridge&params="];

    FlutterMethodChannel* methodChannel = [FlutterMethodChannel methodChannelWithName:@"smart.template.flutter/method"
                                            binaryMessenger:flutterViewController.binaryMessenger];

    [methodChannel setMethodCallHandler:^(FlutterMethodCall* call, FlutterResult result) {
        NSLog(@"method=%@, arguments=%@",call.method, call.arguments);
    }];

    STNavigationController *rootViewNavigationController = [[STNavigationController alloc] initWithRootViewController:flutterViewController];
    rootViewNavigationController.navigationBarHidden = YES;

    UIWindowScene *windowScene = (UIWindowScene *)scene;
    self.window = [[UIWindow alloc] initWithWindowScene:windowScene];
    self.window.frame = windowScene.coordinateSpace.bounds;
    [self.window setBackgroundColor:[UIColor whiteColor]];

    self.window.rootViewController = rootViewNavigationController;
   [self.window makeKeyAndVisible];
}


- (void)sceneDidDisconnect:(UIScene *)scene {
    // Called as the scene is being released by the system.
    // This occurs shortly after the scene enters the background, or when its session is discarded.
    // Release any resources associated with this scene that can be re-created the next time the scene connects.
    // The scene may re-connect later, as its session was not neccessarily discarded (see `application:didDiscardSceneSessions` instead).
}


- (void)sceneDidBecomeActive:(UIScene *)scene {
    // Called when the scene has moved from an inactive state to an active state.
    // Use this method to restart any tasks that were paused (or not yet started) when the scene was inactive.
}


- (void)sceneWillResignActive:(UIScene *)scene {
    // Called when the scene will move from an active state to an inactive state.
    // This may occur due to temporary interruptions (ex. an incoming phone call).
}


- (void)sceneWillEnterForeground:(UIScene *)scene {
    // Called as the scene transitions from the background to the foreground.
    // Use this method to undo the changes made on entering the background.
}


- (void)sceneDidEnterBackground:(UIScene *)scene {
    // Called as the scene transitions from the foreground to the background.
    // Use this method to save data, release shared resources, and store enough scene-specific state information
    // to restore the scene back to its current state.
}


@end
