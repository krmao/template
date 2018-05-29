__d(function (global, _require, module, exports, _dependencyMap) {
    var UNKNOWN_FUNCTION = '<unknown>';
    var StackTraceParser = {
        parse: function parse(stackString) {
            var chrome = /^\s*at (?:(?:(?:Anonymous function)?|((?:\[object object\])?\S+(?: \[as \S+\])?)) )?\(?((?:file|http|https):.*?):(\d+)(?::(\d+))?\)?\s*$/i,
                gecko = /^(?:\s*([^@]*)(?:\((.*?)\))?@)?(\S.*?):(\d+)(?::(\d+))?\s*$/i,
                node = /^\s*at (?:((?:\[object object\])?\S+(?: \[as \S+\])?) )?\(?(.*?):(\d+)(?::(\d+))?\)?\s*$/i,
                lines = stackString.split('\n'),
                stack = [],
                parts,
                element;

            for (var i = 0, j = lines.length; i < j; ++i) {
                if (parts = gecko.exec(lines[i])) {
                    element = {
                        'file': parts[3],
                        'methodName': parts[1] || UNKNOWN_FUNCTION,
                        'lineNumber': +parts[4],
                        'column': parts[5] ? +parts[5] : null
                    };
                } else if (parts = chrome.exec(lines[i])) {
                    element = {
                        'file': parts[2],
                        'methodName': parts[1] || UNKNOWN_FUNCTION,
                        'lineNumber': +parts[3],
                        'column': parts[4] ? +parts[4] : null
                    };
                } else if (parts = node.exec(lines[i])) {
                    element = {
                        'file': parts[2],
                        'methodName': parts[1] || UNKNOWN_FUNCTION,
                        'lineNumber': +parts[3],
                        'column': parts[4] ? +parts[4] : null
                    };
                } else {
                    continue;
                }

                stack.push(element);
            }

            return stack;
        }
    };
    module.exports = StackTraceParser;
},"fb643f3f6041812920e592011600c7f5",[],"node_modules/stacktrace-parser/lib/stacktrace-parser.js");