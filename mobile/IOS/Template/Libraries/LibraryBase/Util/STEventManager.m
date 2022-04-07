//
//  STEventManager.m
//  Template
//
//  Created by krmao on 2020/12/25.
//  Copyright © 2020 smart. All rights reserved.
//

#import "STEventManager.h"

static STEventManager *instance = nil;

@interface STEventManager()

@property (nonatomic, strong) NSMutableArray *eventListenerObjects;

@end

@implementation STEventManager

+(instancetype)sharedInstance{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[STEventManager alloc] init];
    });
    return instance;
}

- (instancetype)init{
    self = [super init];
    if (self) {
        self.eventListenerObjects = [NSMutableArray array];
    }
    return self;
}

-(void)registerListener:(id)listener eventName:(NSString *)eventName callback:(EventCallback)callback{
    if (!listener || eventName.length == 0) {
        return;
    }
    @synchronized (self.eventListenerObjects) {
        NSArray *tmpArray = [NSArray arrayWithArray:self.eventListenerObjects];
        for (STEventListenerObject *object in tmpArray) {
            if ([object.listener isEqual:listener] && [object.eventName isEqualToString:eventName]) {
                if (callback) {
                    object.callback = callback;  //覆盖callback，跟Android一致
                }
                return;
            }
        }
        STEventListenerObject *object = [[STEventListenerObject alloc] initWithListener:listener eventName:eventName callback:callback];
        [self.eventListenerObjects addObject:object];
    }

}

-(void)unRegisterListener:(id)listener eventName:(NSString *)eventName{
    if (!listener || eventName.length == 0) {
        return;
    }
    @synchronized (self.eventListenerObjects) {
        NSArray *tmpArray = [NSArray arrayWithArray:self.eventListenerObjects];
        for (STEventListenerObject *object in tmpArray) {
            if ([object.listener isEqual:listener] && [object.eventName isEqualToString:eventName]) {
                [self.eventListenerObjects removeObject:object];
            }
        }
    }
}

-(void)unRegisterListener:(id)listener{
    if (!listener) {
        return;
    }
    @synchronized (self.eventListenerObjects) {
        NSArray *tmpArray = [NSArray arrayWithArray:self.eventListenerObjects];
        for (STEventListenerObject *object in tmpArray) {
            if ([object.listener isEqual:listener] || object.listener == nil) {
                [self.eventListenerObjects removeObject:object];
            }
        }
    }
}

-(void)sendEvent:(NSString *)eventName eventInfo:(nullable NSDictionary *)eventInfo{
    if (eventName.length == 0) {
        return;
    }
    NSArray *tmpArray;
    @synchronized (self.eventListenerObjects) {
        tmpArray = [NSArray arrayWithArray:self.eventListenerObjects];
    }
    for (STEventListenerObject *object in tmpArray) {
        if ([object.eventName isEqualToString:eventName] ) {
            if (object.callback) {
                object.callback(eventInfo);
            }
        }
    }
}

@end

@implementation STEventListenerObject

- (instancetype)initWithListener:(id)listener eventName:(NSString *)eventName callback:(EventCallback)callback{
    self = [super init];
    if (self) {
        self.listener = listener;
        self.eventName = eventName;
        self.callback = callback;
    }
    return self;
}

@end

