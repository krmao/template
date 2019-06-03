import Foundation
import SwiftyBeaver


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
class CXLogUtil2 {

    static var log: SwiftyBeaver.Type {
        get {
            var _log = SwiftyBeaver.self
            _log.removeAllDestinations()
            var console = ConsoleDestination()
            console.format = "$Dmm:ss.SSS$d $C$L$c: $M"
            _log.addDestination(console)
            return _log
        }
    }
    static var logJson: SwiftyBeaver.Type {
        get {
            var _log = SwiftyBeaver.self
            _log.removeAllDestinations()
            var console = ConsoleDestination()
            console.format = "$Dmm:ss.SSS$d $C$L$c: \n$M"
            _log.addDestination(console)
            return _log
        }
    }

    static let ERROR: Int = log.Level.error.rawValue
    static let WARN: Int = log.Level.warning.rawValue
    static let INFO: Int = log.Level.info.rawValue
    static let DEBUG: Int = log.Level.debug.rawValue
    static let VERBOSE: Int = log.Level.verbose.rawValue

    static func d(_ tag: Any? = "", _ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        log.custom(
                level: .debug,
                message: "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")",
                file: file,
                function: function,
                line: line,
                context: context
        )
    }

    static func d(_ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        log.custom(
                level: .debug,
                message: "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")",
                file: file,
                function: function,
                line: line,
                context: context
        )
    }

    static func e(_ tag: Any? = "", _ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        log.custom(
                level: .error,
                message: "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")",
                file: file,
                function: function,
                line: line,
                context: context
        )
    }

    static func e(_ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        log.custom(
                level: .error,
                message: "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")",
                file: file,
                function: function,
                line: line,
                context: context
        )
    }

    static func w(_ tag: Any? = "", _ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        log.custom(
                level: .warning,
                message: "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")",
                file: file,
                function: function,
                line: line,
                context: context
        )
    }

    static func w(_ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        log.custom(
                level: .warning,
                message: "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")",
                file: file,
                function: function,
                line: line,
                context: context
        )
    }

    static func j(_ level: Int = CXLogUtil.INFO, _ message: Any? = "", _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        logJson.custom(
                level: SwiftyBeaver.Level(rawValue: level) ?? SwiftyBeaver.Level.warning,
                message: message ?? "",
                file: file,
                function: function,
                line: line,
                context: context
        )
    }

    static func v(_ tag: Any? = "", _ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        log.custom(
                level: .verbose,
                message: "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")",
                file: file,
                function: function,
                line: line,
                context: context
        )
    }

    static func v(_ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        log.custom(
                level: .verbose,
                message: "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")",
                file: file,
                function: function,
                line: line,
                context: context
        )
    }

    static func i(_ tag: Any? = "", _ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        log.custom(
                level: .info,
                message: "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")",
                file: file,
                function: function,
                line: line,
                context: context
        )
    }

    static func i(_ message: Any? = "", _ error: Error? = nil, _ file: String = #file, _ function: String = #function, _ line: Int = #line, _ context: Any? = nil) {
        log.custom(
                level: .info,
                message: "\(message == nil ? "" : "\(message!)") \(error == nil ? "" : "\(error!)")",
                file: file,
                function: function,
                line: line,
                context: context
        )
    }
}
