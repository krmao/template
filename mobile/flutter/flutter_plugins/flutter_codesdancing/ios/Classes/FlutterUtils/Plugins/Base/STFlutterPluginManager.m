#import "STFlutterPluginManager.h"

@interface STFlutterPluginManager()

@property (nonatomic, strong) NSMutableDictionary *pluginObjectsDict;

@end

@implementation STFlutterPluginManager

+ (instancetype)sharedInstance
{
    static STFlutterPluginManager *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[STFlutterPluginManager alloc] init];
    });
    
    return instance;
}

- (instancetype)init
{
    self = [super init];
    if (self) {
        self.pluginObjectsDict = [NSMutableDictionary dictionary];
    }
    return self;
}

+ (STFlutterPlugin *)pluginObjectForModuleClass:(NSString *)moduleClassName{
    if (![moduleClassName isKindOfClass:[NSString class]]) {
        return nil;
    }

    STFlutterPlugin *object = nil;
    STFlutterPluginManager *sharedInstance = [STFlutterPluginManager sharedInstance];
    @synchronized(sharedInstance.pluginObjectsDict){
        object = [sharedInstance.pluginObjectsDict valueForKey:moduleClassName];
    }
    if (object) {
        return object;
    }
    Class cls = NSClassFromString(moduleClassName);
    if (cls) {
        object = [[cls alloc] init];
        @synchronized(sharedInstance.pluginObjectsDict){
            [sharedInstance.pluginObjectsDict setValue:object forKey:moduleClassName];
        }
        return object;
    }
    return nil;
}

@end
