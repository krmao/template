//
//  STViewControllerDelegeteImpl.m
//  lib_flutter_base
//
//  Created by krmao on 2021/3/21.
//

#import "STViewControllerDelegeteImpl.h"

@interface STViewControllerDelegeteImpl(){
    NSString *uniqueId;
    int requestCode;
    UIViewController *currentViewController;
    NSDictionary *requestData;
}
@end

@implementation STViewControllerDelegeteImpl

- (instancetype _Nonnull)initWithCurrentViewController:(UIViewController *_Nonnull)currentViewController{
    if(self = [super init]){
        NSLog(@"initWithCurrentViewController success");
        self->currentViewController = currentViewController;
        // *1000 是精确到毫秒，不乘就是精确到秒
        self->uniqueId = [NSString stringWithFormat:@"%f-%lu", [[NSDate date] timeIntervalSince1970]*1000, (unsigned long) self->currentViewController.hash];
    }
    return self;
}

- (UIViewController *_Nonnull) currentViewController{
    return self->currentViewController;
}

- (NSString *_Nonnull)getUniqueId{
    return self->uniqueId;
}

- (void)setRequestData:(int)requestCode requestData:(NSDictionary *_Nullable)requestData{
    NSLog(@"[page] setRequestData uniqueId=%@, self=%@, requestCode=%d, requestData=%@", self->uniqueId, self, requestCode, requestData);
    self->requestCode = requestCode;
    self->requestData = requestData;
}

- (int)getRequestCode{
    return self->requestCode;
}

- (NSDictionary *_Nullable)getRequestData{
    return self->requestData;
}

- (void)onViewControllerResult:(int)requestCode resultCode:(int)resultCode resultData:(NSDictionary *_Nullable)resultData{
    NSLog(@"[page] onViewControllerResult uniqueId=%@, self=%@, requestCode=%d, resultCode=%d, resultData=%@", self->uniqueId, self, requestCode, resultCode, resultData);
}

- (void)viewDidLoad {
    NSLog(@"[page] viewDidLoad uniqueId=%@, self=%@", self->uniqueId, self);
}

- (void)viewWillAppear:(BOOL)animated {
    NSLog(@"[page] viewWillAppear uniqueId=%@, self=%@", self->uniqueId, self);
}

- (void)viewDidAppear:(BOOL)animated{
    NSLog(@"[page] viewDidAppear uniqueId=%@, self=%@", self->uniqueId, self);
}

- (void)viewWillDisappear:(BOOL)animated{
    NSLog(@"[page] viewWillDisappear uniqueId=%@, self=%@", self->uniqueId, self);
}

/**
 * 生命周期
 * https://seniorzhai.github.io/2014/12/11/Android%E3%80%81iOS%E5%A4%A7%E4%B8%8D%E5%90%8C%E2%80%94%E2%80%94%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F/
 */
- (void)viewDidDisappear:(BOOL)animated{
    NSLog(@"[page] viewDidDisappear uniqueId=%@, self=%@", self->uniqueId, self);
}

- (void)didReceiveMemoryWarning{
    NSLog(@"[page] didReceiveMemoryWarning uniqueId=%@, self=%@", self->uniqueId, self);
}

- (void)onDealloc{
    self->currentViewController = nil;
    NSLog(@"[page] onDealloc uniqueId=%@, self=%@", self->uniqueId, self);
}

@end
