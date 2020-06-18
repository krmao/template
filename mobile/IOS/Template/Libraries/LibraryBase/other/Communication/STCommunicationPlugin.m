#import "STCommunicationPlugin.h"
#import "STJSONSerialization.h"
#import "STRNFileManager.h"
#import "STSchemalRule.h"
#import "UIViewController+TopMostViewController.h"
@implementation STCommunicationPlugin

+ (void)getLocationInfoWithJsonStr:(NSString *)jsonStr callBackId:(nullable NSString *)callBackId callback:(NativeHybirdCallBackBlcok)callback {
    NSLog(@"Current method: %@",NSStringFromSelector(_cmd));
    
    NSDictionary * dictionary = @{@"result":@"189.6"};
    NSString * jsonString = [STJSONSerialization dictionaryToJsonString:dictionary];
    
    callback(jsonString, callBackId);

}

+ (void)showToastWithJsonStr:(NSString *)jsonStr callBackId:(nullable NSString *)callBackId callback:(NativeHybirdCallBackBlcok)callback {
    callback(@"",@"");

    NSLog(@"Current method: %@",NSStringFromSelector(_cmd));
    
    UIAlertAction *okAlert = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        //todo
    }];
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"OC提示框" message:jsonStr preferredStyle:UIAlertControllerStyleAlert];
    [alert addAction:okAlert];

    UIViewController *topRootVC = [UIApplication sharedApplication].keyWindow.rootViewController;
    [topRootVC presentViewController:alert animated:YES completion:nil];
}

+ (void)getWithJsonStr:(NSString *)jsonStr callBackId:(nullable NSString *)callBackId callback:(NativeHybirdCallBackBlcok)callback {
    NSUserDefaults *defatult = [NSUserDefaults standardUserDefaults];
    NSData *data = [jsonStr dataUsingEncoding:NSUTF8StringEncoding];
    NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:nil];
    if ([dic.allKeys containsObject:@"key"]) {
        
        NSString *user = [defatult valueForKey:[dic valueForKey:@"key"]];
        //    @{[dic valueForKey:@"key"]:user};
        NSDictionary * dictionary = @{@"result":!user ? @"" : user };
        NSData * jsonData = [NSJSONSerialization dataWithJSONObject:dictionary options:0 error:nil];
        NSString * jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
        
        callback(jsonString, callBackId);
    }
}

+ (void)closeWithJsonStr:(NSString *)jsonStr callBackId:(NSString *)callBackId callback:(NativeHybirdCallBackBlcok)callback
{
    callback(@"true",callBackId);

    dispatch_async(dispatch_get_main_queue(), ^{
        
        
        [[UIViewController topMostViewController].navigationController popViewControllerAnimated:true];

    });
}

+ (void)putWithJsonStr:(NSString *)jsonStr callBackId:(NSString *)callBackId callback:(NativeHybirdCallBackBlcok)callback {
    
//    NSLog(@"%@",functionName);
    //    NSLog(@"%@",functionName);
    NSData *data = [jsonStr dataUsingEncoding:NSUTF8StringEncoding];
    NSError *error = nil;
    NSDictionary *str = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&error];
    if (!error) {
        NSUserDefaults *defatult = [NSUserDefaults standardUserDefaults];
        if ([str valueForKey:@"value"]) {
            [defatult setValue:[str valueForKey:@"value"] forKey:[str valueForKey:@"key"]];
        }
        [defatult synchronize];
    }
    callback(@"true",callBackId);
    
}

+ (void)getDeviceInfoWithJsonStr:(NSString *)jsonStr callBackId:(NSString *)callBackId callback:(NativeHybirdCallBackBlcok)callback {
    
    NSDictionary * dictionary = @{@"result":[NSString stringWithFormat:@"DeviceName:%@,version:%@ %@", [[UIDevice currentDevice] name],[[UIDevice currentDevice] systemName],[[UIDevice currentDevice] systemVersion]]};
    NSString * jsonString = [STJSONSerialization dictionaryToJsonString:dictionary];

    
    callback(jsonString, callBackId);
    
}

+ (void)getUserInfoWithJsonStr:(NSString *)jsonStr callBackId:(NSString *)callBackId callback:(NativeHybirdCallBackBlcok)callback {
    
    NSDictionary * dictionary = @{@"result": @"iOS用户"};
    NSString * jsonString = [STJSONSerialization dictionaryToJsonString:dictionary];

    
    callback(jsonString, callBackId);
    
}

+ (void)openWithJsonStr:(NSString *)jsonStr callBackId:(NSString *)callBackId callback:(NativeHybirdCallBackBlcok)callback {
    
    NSDictionary *dic = [STJSONSerialization dictionaryWithJsonString:jsonStr];
    
    UIViewController *currentVc = [UIViewController topMostViewController];
    if ([dic.allKeys containsObject:@"url"]) {
        NSString *url = [dic valueForKey:@"url"];
        STSchemalType type = [STSchemalRule schemalType:url];
        NSArray *arry = [url componentsSeparatedByString:@"?"];
        if (arry && arry.count > 0) {
            
            NSDictionary *paramDic = [STSchemalRule queryParameters:arry[1]];
            dispatch_async(dispatch_get_main_queue(), ^{
                
                if (type == STSchemalRN) {
//                    URLROuter
//                    STRNURL *url = [[STRNURL alloc] init];
//                    url.rnModuleName =  [paramDic valueForKey:@"component"];
//                    url.rnBundleURL = [NSURL fileURLWithPath:[[STRNFileManager fileNameBaseUnzip] stringByAppendingPathComponent:@"ios/index.ios.bundle"]];
//                    STRNViewController *rnVc = [[STRNViewController alloc] initWithURL:url andInitialProperties:@{@"page":[paramDic valueForKey:@"page"]}];
//
//                    [currentVc.navigationController pushViewController:rnVc animated:true];
                    
                    
                } else if (type == STSchemalH5) {
                    
                } else {
                    
                }
                
            });
            
        }
    }
    
    
    
    
}

@end
