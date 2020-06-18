#import "STRNUnZip.h"
#import <SSZipArchive/SSZipArchive.h>

@implementation STRNUnZip

+ (BOOL)unzipFileAtPath:(NSString *)path toDestination:(NSString *)destination {

    return [SSZipArchive unzipFileAtPath:path toDestination:destination];
}

@end
