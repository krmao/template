#import "STRNBusObject.h"

@implementation STRNBusObject

- (id)initWithHost:(NSString *)host {
    
    if (self = [super initWithHost:host]) {
        {
            //通信层所需
//            businessDistrictEx = [[BusinessDistrictEx alloc] init];
        }
    }
    
    return self;
}


- (id)doDataJob:(NSString *)businessName params:(NSArray *)params {
    
    
    NSLog(@"businessName %@,params %@",businessName,params);
    
    
    return nil;
}


@end
