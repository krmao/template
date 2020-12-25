//
//  STURLManager.h
//  Template
//
//  Created by krmao on 2020/12/25.
//  Copyright © 2020 smart. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum {
    DEV,
    SIT,
    UAT,
    PRD
} Environments;

NS_ASSUME_NONNULL_BEGIN

@interface STURLManager : NSObject

+(instancetype)sharedInstance;

- (Environments) currentEnvironmentType;
- (NSString *) currentEnvironmentTypeString;
- (NSString *) currentEnvironmentURL;

@end

NS_ASSUME_NONNULL_END
