//
//  STEventManager.h
//  Template
//
//  Created by krmao on 2020/12/25.
//  Copyright © 2020 smart. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void (^EventCallback)(NSDictionary * _Nullable data);
@class CTEventListenerObject;

NS_ASSUME_NONNULL_BEGIN

@interface STEventManager : NSObject

+(instancetype)sharedInstance;
-(void)registerListener:(id)listener eventName:(NSString *)eventName callback:(EventCallback)callback;
-(void)unRegisterListener:(id)listener eventName:(NSString *)eventName;
-(void)unRegisterListener:(id)listener;
-(void)sendEvent:(NSString *)eventName eventInfo:(nullable NSDictionary *)eventInfo;

@end

@interface STEventListenerObject : NSObject

@property (nonatomic, weak) id listener; // 需要在vc释放时unRegister，设置为weak，不然释放不了
@property (nonatomic, copy) NSString *eventName;
@property (nonatomic, copy) EventCallback callback;
- (instancetype)initWithListener:(id)listener eventName:(NSString *)eventName callback:(EventCallback)callback;

@end


NS_ASSUME_NONNULL_END
