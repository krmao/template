//
//  STViewControllerDelegete.h
//  Pods
//
//  Created by krmao on 2021/3/22.
//

#ifndef STViewControllerDelegete_h
#define STViewControllerDelegete_h

#define RESULT_OK 0
#define RESULT_CANCEL -1

@protocol STViewControllerDelegete

@required
- (UIViewController *_Nonnull) currentViewController;

@required
- (NSString *_Nonnull)getUniqueId;

@required
- (void)setRequestData:(int)requestCode requestData:(NSDictionary *_Nullable)requestData;

@required
- (int)getRequestCode;

@required
- (NSDictionary *_Nullable)getRequestData;

@required
- (void)onViewControllerResult:(int)requestCode resultCode:(int)resultCode resultData:(NSDictionary *_Nullable)resultData;

@required
- (void)viewDidLoad;

@required
- (void)viewWillAppear:(BOOL)animated;

@required
- (void)viewDidAppear:(BOOL)animated;

@required
- (void)viewWillDisappear:(BOOL)animated;

/**
 * 生命周期
 * https://seniorzhai.github.io/2014/12/11/Android%E3%80%81iOS%E5%A4%A7%E4%B8%8D%E5%90%8C%E2%80%94%E2%80%94%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F/
 */
@required
- (void)viewDidDisappear:(BOOL)animated;

@required
- (void)didReceiveMemoryWarning;

@end

#endif /* STViewControllerDelegete_h */
