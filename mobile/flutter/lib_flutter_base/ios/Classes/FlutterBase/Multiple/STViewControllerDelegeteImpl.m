//
//  STViewControllerDelegeteImpl.m
//  lib_flutter_base
//
//  Created by krmao on 2021/3/21.
//

#import "STViewControllerDelegeteImpl.h"

@implementation STViewControllerDelegeteImpl

- (void)setRequestData:(int)requestCode requestData:(NSDictionary *_Nullable)requestData{
    NSLog(@"[page] setRequestData uniqueId=%@, self=%@, requestCode=%d, requestData=%@", _uniqueId, self, requestCode, requestData);
    _requestCode = requestCode;
    _requestData = requestData;
}

- (int)getRequestCode{
    return _requestCode;
}

- (NSDictionary *_Nullable)getRequestData{
    return _requestData;
}

- (void)onViewControllerResult:(int)requestCode resultCode:(int)resultCode resultData:(NSDictionary *_Nullable)resultData{
    NSLog(@"[page] onViewControllerResult uniqueId=%@, self=%@, requestCode=%d, resultCode=%d, resultData=%@", _uniqueId, self, requestCode, resultCode, resultData);
}

- (void)viewDidLoad {
    _uniqueId  = [NSString stringWithFormat:@"%f-%lu", [[NSDate date] timeIntervalSince1970]*1000, (unsigned long) _viewController.hash]; // *1000 是精确到毫秒，不乘就是精确到秒
    NSLog(@"[page] viewDidLoad uniqueId=%@, self=%@", _uniqueId, self);
}

- (void)viewWillAppear:(BOOL)animated {
    NSLog(@"[page] viewWillAppear uniqueId=%@, self=%@", _uniqueId, self);
}

- (void)viewDidAppear:(BOOL)animated{
    NSLog(@"[page] viewDidAppear uniqueId=%@, self=%@", _uniqueId, self);
}

- (void)viewWillDisappear:(BOOL)animated{
    NSLog(@"[page] viewWillDisappear uniqueId=%@, self=%@", _uniqueId, self);
}

/**
 * 生命周期
 * https://seniorzhai.github.io/2014/12/11/Android%E3%80%81iOS%E5%A4%A7%E4%B8%8D%E5%90%8C%E2%80%94%E2%80%94%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F/
 */
- (void)viewDidDisappear:(BOOL)animated{
    NSLog(@"[page] viewDidDisappear uniqueId=%@, self=%@", _uniqueId, self);
}

- (void)didReceiveMemoryWarning{
    NSLog(@"[page] didReceiveMemoryWarning uniqueId=%@, self=%@", _uniqueId, self);
}

- (void)dealloc{
    NSLog(@"[page] dealloc uniqueId=%@, self=%@", _uniqueId, self);
}

@end
