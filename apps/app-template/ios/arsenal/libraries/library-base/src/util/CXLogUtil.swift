import Foundation

/**
    AppCode 安装 GrepConsole

    http://docs.swiftybeaver.com/article/20-custom-format

    $L	Level, for example "VERBOSE"
    $M	Message, for example the foo in log.debug("foo")
    $J	JSON-encoded logging object (can not be combined with other format variables!)
    $N	Name of file without suffix
    $n	Name of file with suffix
    $F	Function
    $l	Line (lower-case l)
    $D	Datetime, followed by standard Swift datetime syntax
    $d	Datetime format end
    $T	Thread
    $C	Color start, is just supported by certain destinations and is ignored if unsupported
    $c	Color end
    $X	Optional context value of any type (see below)
*/
class CXLogUtil {

    static let ERROR: Int = 1
    static let WARN: Int = 2
    static let INFO: Int = 3
    static let DEBUG: Int = 4
    static let VERBOSE: Int = 5

    static func d(_ tag: Any? = "", _ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
         let msg = "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")"

        print("DEBUG", tag, msg, error, "\t\(function)", "\t\(line)", "\t\(function)", context)
    }

    static func d(_ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        let msg = "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")"

        print("DEBUG", msg, error, "\t\(function)", "\t\(line)", "\t\(function)", context)
    }

    static func e(_ tag: Any? = "", _ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        let msg = "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")"

        print("ERROR", tag, msg, error, "\t\(function)", "\t\(line)", "\t\(function)", context)
    }

    static func e(_ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        let msg = "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")"

        print("ERROR", msg, error, "\t\(function)", "\t\(line)", "\t\(function)", context)
    }

    static func w(_ tag: Any? = "", _ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        let msg = "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")"

        print("WARNING", tag, msg, error, "\t\(function)", "\t\(line)", "\t\(function)", context)
    }

    static func w(_ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        let msg = "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")"

        print("WARNING",msg, error, "\t\(function)", "\t\(line)", "\t\(function)", context)
    }

    static func j(_ level: Int = CXLogUtil.INFO, _ message: Any? = "", _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        let msg = "\(message == nil ? "" : "\(message!)")"

        print("VERBOSE",  msg, "\t\(function)", "\t\(line)", "\t\(function)", context)
    }

    static func v(_ tag: Any? = "", _ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        let msg = "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")"

        print("VERBOSE", tag, msg, error, "\t\(function)", "\t\(line)", "\t\(function)", context)
    }

    static func v(_ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        let msg = "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")"

        print("VERBOSE", msg, error, "\t\(function)", "\t\(line)", "\t\(function)", context)
    }

    static func i(_ tag: Any? = "", _ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        let msg = "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")"

        print("INFO", tag, msg, error, "\t\(function)", "\t\(line)", "\t\(function)", context)
    }

    static func i(_ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        let msg = "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")"

        print("INFO", msg, error, "\t\(function)", "\t\(line)", "\t\(function)", context)
    }
}
