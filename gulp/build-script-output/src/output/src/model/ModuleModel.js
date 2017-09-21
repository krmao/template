'use strict';

Object.defineProperty(exports, "__esModule", {
    value: true
});

var _createClass = function () {
    function defineProperties(target, props) {
        for (var i = 0; i < props.length; i++) {
            var descriptor = props[i];descriptor.enumerable = descriptor.enumerable || false;descriptor.configurable = true;if ("value" in descriptor) descriptor.writable = true;Object.defineProperty(target, descriptor.key, descriptor);
        }
    }return function (Constructor, protoProps, staticProps) {
        if (protoProps) defineProperties(Constructor.prototype, protoProps);if (staticProps) defineProperties(Constructor, staticProps);return Constructor;
    };
}();

var _fs = require('fs');

var _fs2 = _interopRequireDefault(_fs);

function _interopRequireDefault(obj) {
    return obj && obj.__esModule ? obj : { default: obj };
}

function _classCallCheck(instance, Constructor) {
    if (!(instance instanceof Constructor)) {
        throw new TypeError("Cannot call a class as a function");
    }
}

var Module = function () {
    function Module(moduleName, port, indexName) {
        _classCallCheck(this, Module);

        this._indexName = indexName || null;
        this._port = port || 7777;
        this._moduleName = moduleName || '';

        this._srcPath = './src/modules/' + this.moduleName + '/';
        this._srcPathHtmls = this.srcPath + 'html/';
        this._srcPathImages = this.srcPath + 'img/';
        this._srcPathStyles = this.srcPath + 'scss/';
        this._srcPathJavaScripts = this.srcPath + 'js/';
        this._srcPathTemplateFile = './src/templates/' + this.moduleName + '.html';

        this._buildOutputPath = './build/static/';
        this._buildOutputPathImages = this.buildOutputPath + 'images/' + this.moduleName + '/';
        this._buildOutputPathJavaScripts = this.buildOutputPath + 'scripts/' + this.moduleName + '/';
        this._buildOutputPathStyles = this.buildOutputPath + 'styles/' + this.moduleName + '/';
        this._buildOutputPathHtmls = this.buildOutputPath + this.moduleName + '/';

        this._zipSrcPathImages = this.buildOutputPathImages + '*';
        this._zipSrcPathJavaScripts = this.buildOutputPathJavaScripts + '*.min.js';
        this._zipSrcPathHtmls = this.buildOutputPathHtmls + '*.html';
        this._zipSrcPathStyles = this.buildOutputPathStyles + '*.min.css';

        this._zipFileNameImages = 'zip/img/' + this.moduleName + '.zip';
        this._zipFileNameJavaScripts = 'zip/js/' + this.moduleName + '.zip';
        this._zipFileNameHtmls = 'zip/' + this.moduleName + '.zip';
        this._zipFileNameStyles = 'zip/css/' + this.moduleName + '.zip';

        this._zipPathOutput = './build/';
        this._zipPathBaseImages = this.zipPathOutput + 'static/images/';
        this._zipPathBaseJavaScripts = this.zipPathOutput + 'static/scripts/';
        this._zipPathBaseStyles = this.zipPathOutput + 'static/styles/';
        this._zipPathBaseHtmls = this.zipPathOutput + 'static/';
        this._port = port;
    }

    _createClass(Module, [{
        key: 'toString',
        value: function toString() {
            return "\n==============================>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + "\n当前模块" + "\n==============================>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + "\nindexName:\t\t\t\t\t\t" + this.indexName + "\nmoduleName:\t\t\t\t\t\t" + this.moduleName + "\nport:\t\t\t\t\t\t" + this.port + "\nsrcPath:\t\t\t\t\t\t" + this.srcPath + "\nsrcPathHtmls:\t\t\t\t\t" + this.srcPathHtmls + "\nsrcPathImages:\t\t\t\t\t" + this.srcPathImages + "\nsrcPathStyles:\t\t\t\t\t" + this.srcPathStyles + "\nsrcPathJavaScripts:\t\t\t\t" + this.srcPathJavaScripts + "\nsrcPathTemplateFile:\t\t\t" + this.srcPathTemplateFile + "\nbuildOutputPath:\t\t\t\t" + this.buildOutputPath + "\nbuildOutputPathImages:\t\t\t" + this.buildOutputPathImages + "\nbuildOutputPathJavaScripts:\t\t" + this.buildOutputPathJavaScripts + "\nbuildOutputPathHtmls:\t\t\t" + this.buildOutputPathHtmls + "\nbuildOutputPathStyles:\t\t\t" + this.buildOutputPathStyles + "\nzipSrcPathImages:\t\t\t\t" + this.zipSrcPathImages + "\nzipSrcPathJavaScripts:\t\t\t" + this.zipSrcPathJavaScripts + "\nzipSrcPathHtmls:\t\t\t\t" + this.zipSrcPathHtmls + "\nzipSrcPathStyles:\t\t\t\t" + this.zipSrcPathStyles + "\nzipFileNameImages:\t\t\t\t" + this.zipFileNameImages + "\nzipFileNameJavaScripts:\t\t\t" + this.zipFileNameJavaScripts + "\nzipFileNameHtmls:\t\t\t\t" + this.zipFileNameHtmls + "\nzipFileNameStyles:\t\t\t\t" + this.zipFileNameStyles + "\nzipPathOutput:\t\t\t\t\t" + this.zipPathOutput + "\nzipPathBaseImages:\t\t\t\t" + this.zipPathBaseImages + "\nzipPathBaseJavaScripts:\t\t\t" + this.zipPathBaseJavaScripts + "\nzipPathBaseStyles:\t\t\t\t" + this.zipPathBaseStyles + "\nzipPathBaseHtmls:\t\t\t\t" + this.zipPathBaseHtmls + "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<==============================\n";
        }
    }, {
        key: 'moduleName',
        get: function get() {
            return this._moduleName;
        },
        set: function set(value) {
            this._moduleName = value;
        }
    }, {
        key: 'indexName',
        get: function get() {
            return this._indexName;
        },
        set: function set(value) {
            this._indexName = value;
        }
    }, {
        key: 'port',
        get: function get() {
            return this._port;
        },
        set: function set(value) {
            this._port = value;
        }
    }, {
        key: 'zipSrcPathImages',
        get: function get() {
            return this._zipSrcPathImages;
        },
        set: function set(value) {
            this._zipSrcPathImages = value;
        }
    }, {
        key: 'zipSrcPathJavaScripts',
        get: function get() {
            return this._zipSrcPathJavaScripts;
        },
        set: function set(value) {
            this._zipSrcPathJavaScripts = value;
        }
    }, {
        key: 'zipSrcPathHtmls',
        get: function get() {
            return this._zipSrcPathHtmls;
        },
        set: function set(value) {
            this._zipSrcPathHtmls = value;
        }
    }, {
        key: 'zipSrcPathStyles',
        get: function get() {
            return this._zipSrcPathStyles;
        },
        set: function set(value) {
            this._zipSrcPathStyles = value;
        }
    }, {
        key: 'zipFileNameImages',
        get: function get() {
            return this._zipFileNameImages;
        },
        set: function set(value) {
            this._zipFileNameImages = value;
        }
    }, {
        key: 'zipFileNameJavaScripts',
        get: function get() {
            return this._zipFileNameJavaScripts;
        },
        set: function set(value) {
            this._zipFileNameJavaScripts = value;
        }
    }, {
        key: 'zipFileNameHtmls',
        get: function get() {
            return this._zipFileNameHtmls;
        },
        set: function set(value) {
            this._zipFileNameHtmls = value;
        }
    }, {
        key: 'zipFileNameStyles',
        get: function get() {
            return this._zipFileNameStyles;
        },
        set: function set(value) {
            this._zipFileNameStyles = value;
        }
    }, {
        key: 'zipPathOutput',
        get: function get() {
            return this._zipPathOutput;
        },
        set: function set(value) {
            this._zipPathOutput = value;
        }
    }, {
        key: 'zipPathBaseImages',
        get: function get() {
            return this._zipPathBaseImages;
        },
        set: function set(value) {
            this._zipPathBaseImages = value;
        }
    }, {
        key: 'zipPathBaseJavaScripts',
        get: function get() {
            return this._zipPathBaseJavaScripts;
        },
        set: function set(value) {
            this._zipPathBaseJavaScripts = value;
        }
    }, {
        key: 'zipPathBaseStyles',
        get: function get() {
            return this._zipPathBaseStyles;
        },
        set: function set(value) {
            this._zipPathBaseStyles = value;
        }
    }, {
        key: 'zipPathBaseHtmls',
        get: function get() {
            return this._zipPathBaseHtmls;
        },
        set: function set(value) {
            this._zipPathBaseHtmls = value;
        }
    }, {
        key: 'srcPath',
        get: function get() {
            return this._srcPath;
        },
        set: function set(value) {
            this._srcPath = value;
        }
    }, {
        key: 'srcPathHtmls',
        get: function get() {
            return this._srcPathHtmls;
        },
        set: function set(value) {
            this._srcPathHtmls = value;
        }
    }, {
        key: 'srcPathImages',
        get: function get() {
            return this._srcPathImages;
        },
        set: function set(value) {
            this._srcPathImages = value;
        }
    }, {
        key: 'srcPathStyles',
        get: function get() {
            return this._srcPathStyles;
        },
        set: function set(value) {
            this._srcPathStyles = value;
        }
    }, {
        key: 'srcPathJavaScripts',
        get: function get() {
            return this._srcPathJavaScripts;
        },
        set: function set(value) {
            this._srcPathJavaScripts = value;
        }
    }, {
        key: 'srcPathTemplateFile',
        get: function get() {
            try {
                _fs2.default.accessSync(this._srcPathTemplateFile, _fs2.default.F_OK);
            } catch (e) {
                this._srcPathTemplateFile = './src/templates/template.html';
                console.warn("not find custom template: " + e + " and will use default template : " + this._srcPathTemplateFile);
            }
            return this._srcPathTemplateFile;
        },
        set: function set(value) {
            this._srcPathTemplateFile = value;
        }
    }, {
        key: 'buildOutputPath',
        get: function get() {
            return this._buildOutputPath;
        },
        set: function set(value) {
            this._buildOutputPath = value;
        }
    }, {
        key: 'buildOutputPathImages',
        get: function get() {
            return this._buildOutputPathImages;
        },
        set: function set(value) {
            this._buildOutputPathImages = value;
        }
    }, {
        key: 'buildOutputPathJavaScripts',
        get: function get() {
            return this._buildOutputPathJavaScripts;
        },
        set: function set(value) {
            this._buildOutputPathJavaScripts = value;
        }
    }, {
        key: 'buildOutputPathHtmls',
        get: function get() {
            return this._buildOutputPathHtmls;
        },
        set: function set(value) {
            this._buildOutputPathHtmls = value;
        }
    }, {
        key: 'buildOutputPathStyles',
        get: function get() {
            return this._buildOutputPathStyles;
        },
        set: function set(value) {
            this._buildOutputPathStyles = value;
        }
    }]);

    return Module;
}();

exports.default = Module;