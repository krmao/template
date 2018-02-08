import Foundation

/**
 *
 *    AppCode 安装 GrepConsole
 *
 *    change Expression like this
 *
 *    .* I .*
 *    .* D .*
 *    .* W .*
 *    .* E .*
 *    .* V .*
 *
 */
public class CXLogUtil {

    public static let ERROR: Int = 1
    public static let WARN: Int = 2
    public static let INFO: Int = 3
    public static let DEBUG: Int = 4
    public static let VERBOSE: Int = 5

    public static func d(_ tag: String? = "", _ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line) {
        p(DEBUG, tag, message, error, file, function, line)
    }

    public static func d(_ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line) {
        d(nil, message, error, file, function, line)
    }

    public static func e(_ tag: String? = "", _ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line) {
        p(ERROR, tag, message, error, file, function, line)
    }

    public static func e(_ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line) {
        e(nil, message, error, file, function, line)
    }

    public static func w(_ tag: String? = "", _ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line) {
        p(WARN, tag, message, error, file, function, line)
    }

    public static func w(_ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line) {
        w(nil, message, error, file, function, line)
    }

    public static func j(_ level: Int = CXLogUtil.INFO, _ message: Any? = "", _ file: String = #file, _ function: String = #function, _ line: Int = #line) {
        p(level, nil, message, nil, file, function, line)
    }

    public static func v(_ tag: String? = "", _ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line) {
        p(VERBOSE, tag, message, error, file, function, line)
    }

    public static func v(_ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line) {
        v(nil, message, error, file, function, line)
    }

    public static func i(_ tag: String? = "", _ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line) {
        p(INFO, tag, message, error, file, function, line)
    }

    public static func i(_ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line) {
        i(nil, message, error, file, function, line)
    }

    public static func p(_ level: Int, _ tag: String? = "", _ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line) {
        print("\(getLevelDesc(level)) ❪\(getLocation(file, function, line))❫ █\t", getTag(tag), getMessage(message), getError(error))
    }

    private static func getTag(_ tag: String? = "") -> String {
        return "\(tag == nil ? "" : "\(tag!)")"
    }

    private static func getLevelDesc(_ level: Int) -> String {
        let levelDesc: String

        switch level {
        case ERROR:
            levelDesc = "❪\(CXTimeUtil.HmsS())❫ E"
            break
        case WARN:
            levelDesc = "❪\(CXTimeUtil.HmsS())❫ W"
            break
        case INFO:
            levelDesc = "❪\(CXTimeUtil.HmsS())❫ I"
            break
        case DEBUG:
            levelDesc = "❪\(CXTimeUtil.HmsS())❫ D"
            break
        case VERBOSE:
            levelDesc = "❪\(CXTimeUtil.HmsS())❫ V"
            break
        default:
            levelDesc = "❪\(CXTimeUtil.HmsS())❫ V"
            break
        }

        return levelDesc
    }

    private static func getMessage(_ message: Any? = "") -> String {
        return "\(message == nil ? "" : "\(message!)")"
    }

    private static func getError(_ error: Error? = nil) -> String {
        return "\(error == nil ? "" : "\(error!)")"
    }

    public static func getLocation(_ file: String = #file, _ function: String = #function, _ line: Int = #line) -> String {
        return "\(getFileName(file)):\(getLine(line))"
    }

    public static func getFileName(_ file: String = #file) -> String {
        return (file.substringAfter("/") ?? "").replace(".swift", "").replace(".h", " ").replace(".m", " ").paddingLeft(26, " ")
    }

    public static func getLine(_ line: Int = #line) -> String {
        return "\(line)".paddingRight(4, " ")
    }
}
