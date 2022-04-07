#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSDate (STExtension)

//是否是今天
- (BOOL)isToday;

//是否在某个日期以前
- (BOOL)isBeforeDate:(NSDate *)date;

//是否在某天以前或当天
- (BOOL)isBeforeOrEqualToDate:(NSDate *)date;

//是否某天以后
- (BOOL)isAfterDate:(NSDate *)date;

//是否某天以后或当天
- (BOOL)isAfterOrEqualToDate:(NSDate *)date;

//是否在两个日期之间 (earlyDate <= currentDate <= lateDate)
- (BOOL)isBetweenDate:(NSDate *)earlyDate andDate:(NSDate *)lateDate;

//距离现在的时间差
- (NSTimeInterval)timeIntervalUntilNow;

//判断某个时间是否为昨天
- (BOOL)isYesterday;

//判断某个时间是否为今年
- (BOOL)isYear;

//将某个时间格式化为yyyy-MM-dd
- (NSDate *)dateWithYMD;

/**
 * 计算2个时间的差值时间 传时间戳字符串，返回NSDateComponents
 */
+ (NSDateComponents *)deltaWithLargeTimeStamp:(NSString *)largeTimeStamp withSmallTimeStamp:(NSString *)smallTimeStamp;

- (NSDate *)dateAddMonth:(NSInteger)month;

- (NSDate *)dateAddDays:(NSInteger)days;

// 返回当前日期所在月的指定某日的日期
- (NSDate *)dateOnDay:(NSInteger)onDay;

- (NSString *)dateToyyyyMMdd;

- (NSString *)dateToyyyyMMddHHmmss;

// Unix时间戳 毫秒
- (NSString *)unixTsms;

// Unix时间戳 秒
- (NSString *)unixTss;

+ (NSString *)unixTsmsToDate:(NSString *)sTimestamp;

@end

NS_ASSUME_NONNULL_END
