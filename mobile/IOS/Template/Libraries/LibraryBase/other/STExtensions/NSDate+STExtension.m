#import "NSDate+STExtension.h"

@implementation NSDate (STExtension)

- (long)dayNumber {
    return (long) floor(([self timeIntervalSinceReferenceDate] + [[NSTimeZone localTimeZone] secondsFromGMTForDate:self]) / (double) (60 * 60 * 24));
}

- (BOOL)isToday {
    return [self isSameDayAsDate:[NSDate date]];
}

- (BOOL)isSameDayAsDate:(NSDate *)other {
    BOOL retValue = YES;

    if (self != other) {
        retValue = ([self dayNumber] == [other dayNumber]);
    }

    return retValue;
}

- (BOOL)isBeforeDate:(NSDate *)date {
    return ([self compare:date] == NSOrderedAscending);
}

- (BOOL)isAfterDate:(NSDate *)date {
    return ([self compare:date] == NSOrderedDescending);
}

- (BOOL)isBeforeOrEqualToDate:(NSDate *)date {
    return ![self isAfterDate:date];
}

- (BOOL)isAfterOrEqualToDate:(NSDate *)date {
    return ![self isBeforeDate:date];
}

- (BOOL)isBetweenDate:(NSDate *)earlyDate andDate:(NSDate *)lateDate {
    return ([self isAfterOrEqualToDate:earlyDate] && [self isBeforeOrEqualToDate:lateDate]);
}

- (NSTimeInterval)timeIntervalUntilNow {
    return -[self timeIntervalSinceNow];
}

- (BOOL)isYesterday {
    NSDate *nowDate = [[NSDate date] dateWithYMD];
    NSDate *selfDate = [self dateWithYMD];
    //获得nowDate和selfDate的差距
    NSCalendar *calendar = [NSCalendar currentCalendar];
    NSDateComponents *cmps = [calendar components:NSCalendarUnitDay fromDate:selfDate toDate:nowDate options:0];
    return cmps.day == 1;

}

- (BOOL)isYear {
    NSCalendar *calendar = [NSCalendar currentCalendar];
    int unit = NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay;

    //1.获得当前时间的年月日
    NSDateComponents *nowCmps = [calendar components:unit fromDate:[NSDate date]];

    //2.获得self的年月日
    NSDateComponents *selfCmps = [calendar components:unit fromDate:self];

    return nowCmps.year == selfCmps.year;
}

- (NSDate *)dateWithYMD {
    NSDateFormatter *fmt = [[NSDateFormatter alloc] init];
    fmt.dateFormat = @"yyyy-MM-dd";
    NSString *str = [fmt stringFromDate:self];
    return [fmt dateFromString:str];
}

+ (NSDateComponents *)deltaWithLargeTimeStamp:(NSString *)largeTimeStamp withSmallTimeStamp:(NSString *)smallTimeStamp {

    NSDate *largeDate = [NSDate dateWithTimeIntervalSince1970:([largeTimeStamp doubleValue] / 1000)];

    NSDate *smallDate = [NSDate dateWithTimeIntervalSince1970:([smallTimeStamp doubleValue] / 1000)];

    // 当前日历
    NSCalendar *calendar = [NSCalendar currentCalendar];
    // 需要对比的时间数据
    NSCalendarUnit unit = NSCalendarUnitYear | NSCalendarUnitMonth
            | NSCalendarUnitDay | NSCalendarUnitHour | NSCalendarUnitMinute | NSCalendarUnitSecond;
    // 对比时间差
    NSDateComponents *dateCom = [calendar components:unit fromDate:smallDate toDate:largeDate options:0];

    return dateCom;
}

- (NSDate *)dateAddMonth:(NSInteger)month {
    NSDateComponents *comps = [[NSDateComponents alloc] init];
    [comps setMonth:month];
    NSCalendar *calender = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    NSDate *mDate = [calender dateByAddingComponents:comps toDate:self options:0];
    return mDate;
}

- (NSDate *)dateAddDays:(NSInteger)days {
    NSDateComponents *comps = [[NSDateComponents alloc] init];
    [comps setDay:days];
    NSCalendar *calender = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    NSDate *mDate = [calender dateByAddingComponents:comps toDate:self options:0];
    return mDate;
}

- (NSDate *)dateOnDay:(NSInteger)onDay {
    NSCalendar *calendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];

    NSInteger unitFlags = NSCalendarUnitYear |
            NSCalendarUnitMonth |
            NSCalendarUnitDay |
            NSCalendarUnitWeekday |
            NSCalendarUnitHour |
            NSCalendarUnitMinute |
            NSCalendarUnitSecond;

    NSDateComponents *comps = [calendar components:unitFlags fromDate:self];

    NSInteger month = [comps month];

    [comps setMonth:month];
    [comps setDay:onDay];

    NSCalendar *retCalendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    NSDate *retDate = [retCalendar dateFromComponents:comps];
    return retDate;
}

- (NSString *)dateToyyyyMMdd {
    return [self d2s:@"yyyy-MM-dd"];
}

- (NSString *)dateToyyyyMMddHHmmss {
    return [self d2s:@"yyyyMMddHHmmss"];
}

- (NSString *)d2s:(NSString *)fmt {
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:fmt];
    NSString *dateString = [formatter stringFromDate:self];
    return dateString;
}

- (NSString *)unixTsms {
    UInt64 recordTime = [self timeIntervalSince1970] * 1000;
    return [NSString stringWithFormat:@"%@", @(recordTime)];
}

- (NSString *)unixTss {
    UInt64 recordTime = [self timeIntervalSince1970];
    return [NSString stringWithFormat:@"%@", @(recordTime)];
}

+ (NSString *)unixTsmsToDate:(NSString *)sTimestamp {
    long long int seconds = (long long int) [sTimestamp integerValue] / 1000;
    NSDate *outDate = [NSDate dateWithTimeIntervalSince1970:seconds];

    return [outDate dateToyyyyMMdd];
}

@end
