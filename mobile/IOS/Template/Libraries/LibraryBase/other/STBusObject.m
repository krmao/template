#import "STBusObject.h"

@interface STBusObject() {
    NSString *bizNamePrefix;
    NSString *urlHost;
}

@end
@implementation STBusObject


- (id)initWithHost:(NSString *)host {
    if (self = [super init]) {
        urlHost = [host lowercaseString];
    }
    return self;
}

- (NSString *)businessNamePrefixAndURLHost {
    return urlHost;
}


- (id)doDataJob:(NSString *)businessName params:(NSArray *)params {
    //TO BE OVERRIDE
    return NULL;
}


@end
