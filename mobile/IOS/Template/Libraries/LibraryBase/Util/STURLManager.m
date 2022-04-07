//
//  STURLManager.m
//  Template
//
//  Created by krmao on 2020/12/25.
//  Copyright © 2020 smart. All rights reserved.
//

#import "STURLManager.h"

static STURLManager *instance = nil;

@interface STURLManager()
@property Environments currentEnvironmentsType;
@end

@implementation STURLManager

+(instancetype)sharedInstance{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[STURLManager alloc] init];
    });
    return instance;
}

- (NSString *) currentEnvironmentTypeString{
    NSString* currentEnvironmentTypeString = @"DEV";
    switch (self.currentEnvironmentsType) {
        case DEV:
            currentEnvironmentTypeString = @"DEV";
            break;
        case SIT:
            currentEnvironmentTypeString = @"SIT";
            break;
        case PRD:
            currentEnvironmentTypeString = @"PRD";
            break;
        case UAT:
            currentEnvironmentTypeString = @"UAT";
            break;
        default:
            break;
    }
    return currentEnvironmentTypeString;
}

- (Environments) currentEnvironmentType{
    return DEV;
}

- (NSString *) currentEnvironmentURL{
    return @"http://127.0.0.1:1234";
}

@end
