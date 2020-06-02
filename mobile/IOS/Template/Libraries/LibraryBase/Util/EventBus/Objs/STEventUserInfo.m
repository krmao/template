
@implementation STEventUserInfo
- (NSString *)description {
    return [NSString stringWithFormat:@"<%p> - userInfo: %@, extobj: %@", self, [self.userInfo description], [self.extObj description]];
}
@end
