#import "STBus.h"
#import <os/lock.h>

@interface STBus ()

@property(nonatomic, strong) NSMutableDictionary *dataBusObjectMap;

@end

static STBus *bus;


@implementation STBus
#if __IPHONE_OS_VERSION_MAX_ALLOWED >= __IPHONE_10_0

static os_unfair_lock aspect_lock = OS_UNFAIR_LOCK_INIT;

#else

static OSSpinLock busSpinLock = OS_SPINLOCK_INIT;


#endif


+ (void)initializeBusIfNeed {
    if (bus == NULL) {
        static dispatch_once_t onceToken;
        dispatch_once(&onceToken, ^{
            bus = [[STBus alloc] init];
        });
    }
}

+ (STBus *)sharedInstance {
    [STBus initializeBusIfNeed];

    return bus;
}

- (id)init {
    if (self = [super init]) {
        self.dataBusObjectMap = [NSMutableDictionary dictionary];
    }

    return self;
}

+ (STBusObject *)busObjectForName:(NSString *)bizName {

    STBusObject *busObj = nil;

#if __IPHONE_OS_VERSION_MAX_ALLOWED >= __IPHONE_10_0
    os_unfair_lock_lock(&aspect_lock);
    busObj = [STBus FindBusWithKey:bizName];
    os_unfair_lock_unlock(&aspect_lock);
#else
    OSSpinLockLock(&busSpinLock);
    busObj = [STBus FindBusWithKey: bizName];
    OSSpinLockUnlock(&busSpinLock);
#endif

    return busObj;
}


+ (void)register:(STBusObject *)busObj {
    if (busObj == NULL) {
        return;
    }

#if __IPHONE_OS_VERSION_MAX_ALLOWED >= __IPHONE_10_0
    static os_unfair_lock aspect_lock = OS_UNFAIR_LOCK_INIT;
    os_unfair_lock_lock(&aspect_lock);
    [STBus registerBus:busObj];
    os_unfair_lock_unlock(&aspect_lock);
#else
    static OSSpinLock busSpinLock = OS_SPINLOCK_INIT;
    OSSpinLockLock(&busSpinLock);
    [STBus registerBus:busObj];
    OSSpinLockUnlock(&busSpinLock);
#endif

}

+ (void)registerBus:(STBusObject *)busObj {
    if ([[STBus sharedInstance].dataBusObjectMap valueForKey:busObj.businessNamePrefixAndURLHost]) {
        NSAssert(false, @"host名=[%@]已注册，不可重复注册", busObj.businessNamePrefixAndURLHost);
    }
    [[STBus sharedInstance].dataBusObjectMap setValue:busObj forKey:busObj.businessNamePrefixAndURLHost];
}

+ (STBusObject *)FindBusWithKey:(NSString *)key {

    NSDictionary *busObjectMap = [STBus sharedInstance].dataBusObjectMap;
    NSArray *hostList = [busObjectMap allKeys];
    NSString *findKey = nil;
    for (NSString *aKey in hostList) {
        if ([[key lowercaseString] hasPrefix:[aKey lowercaseString]]) {
            findKey = aKey;
            break;
        }
    }
    return (STBusObject *) [busObjectMap valueForKey:findKey];

}

+ (id)callData:(NSString *)bizName param:(NSObject *)param, ... NS_REQUIRES_NIL_TERMINATION {
    STBusObject *busObj = [STBus busObjectForName:bizName];

    if (!busObj) {
        NSAssert(false, @"busObj nil");
    }

    NSMutableArray *paramArr = nil;
    id eachItem;
    va_list argumentList;
    if (param != nil) {
        paramArr = [NSMutableArray array];

        [paramArr addObject:param];
        va_start(argumentList, param);
        while ((eachItem = va_arg(argumentList, id))) {
            [paramArr addObject:eachItem];
        }
        va_end(argumentList);
    }

    id ret = [busObj doDataJob:bizName params:paramArr];
    return ret;
}

@end
