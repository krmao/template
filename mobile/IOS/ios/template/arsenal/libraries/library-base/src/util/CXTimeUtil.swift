import Foundation

class CXTimeUtil {

    static func yMdHmsS() -> String {
        return yMdHmsS(Date())
    }

    static func yMdHmsS(_ millisecond: Int) -> String {
        return yMdHmsS(Date(millisecond))
    }

    static func yMdHmsS(_ date: Date) -> String {
        return format("yyyy-MM-dd HH:mm:ss SSS", date)
    }

    static func HmsS() -> String {
        return HmsS(Date())
    }

    static func HmsS(_ millisecond: Int) -> String {
        return HmsS(Date(millisecond))
    }

    static func HmsS(_ date: Date) -> String {
        return format("HH:mm:ss SSS", date)
    }

    static func Hms() -> String {
        return Hms(Date())
    }

    static func Hms(_ millisecond: Int) -> String {
        return Hms(Date(millisecond))
    }

    static func Hms(_ date: Date) -> String {
        return format("HH:mm:ss", date)
    }

    static func format(_ pattern: String, _ date: Date) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = pattern
        return dateFormatter.string(from: date)
    }

    static func parse(_ pattern: String, _ value: String) -> Date? {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = pattern
        return dateFormatter.date(from: value)
    }
}

extension Date {
    public init(_ millisecond: Int) {
        self.init(timeIntervalSince1970: TimeInterval(Double(millisecond) / 1000.0))
    }
}