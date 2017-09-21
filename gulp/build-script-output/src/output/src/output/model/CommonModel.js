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

function _classCallCheck(instance, Constructor) {
    if (!(instance instanceof Constructor)) {
        throw new TypeError("Cannot call a class as a function");
    }
}

var Common = function () {
    function Common() {
        _classCallCheck(this, Common);

        this._srcPath = './src/base/';
        this._srcPathImages = this.srcPath + 'common/img/*';
        this._srcPathStyles = this.srcPath + 'common/scss/*.scss';
        this._srcPathJavaScripts = this.srcPath + 'common/js/*.js';
        this._srcPathPluginArrays = [this.srcPath + 'plugins/**/*.css', this.srcPath + 'plugins/**/*.js'];

        this._buildOutputPath = './build/static/';
        this._buildOutputPathImages = this.buildOutputPath + 'images/common/';
        this._buildOutputPathJavaScripts = this.buildOutputPath + 'scripts/';
        this._buildOutputPathStyles = this.buildOutputPath + 'styles/';

        this._buildOutputPathPlugin = this.buildOutputPath + 'plugins/';
    }

    _createClass(Common, [{
        key: 'toString',
        value: function toString() {
            return "\n==============================>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + "\n公共模块" + "\n==============================>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + "\nsrcPath:\t\t\t\t\t\t" + this.srcPath + "\nsrcPathImages:\t\t\t\t\t" + this.srcPathImages + "\nsrcPathStyles:\t\t\t\t\t" + this.srcPathStyles + "\nsrcPathJavaScripts:\t\t\t\t" + this.srcPathJavaScripts + "\nsrcPathPluginArrays:\t\t\t" + this.srcPathPluginArrays + "\nbuildOutputPath:\t\t\t\t" + this.buildOutputPath + "\nbuildOutputPathImages:\t\t\t" + this.buildOutputPathImages + "\nbuildOutputPathJavaScripts:\t\t" + this.buildOutputPathJavaScripts + "\nbuildOutputPathStyles:\t\t\t" + this.buildOutputPathStyles + "\nbuildOutputPathPlugin:\t\t\t" + this.buildOutputPathPlugin + "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<==============================\n";
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
        key: 'srcPathPluginArrays',
        get: function get() {
            return this._srcPathPluginArrays;
        },
        set: function set(value) {
            this._srcPathPluginArrays = value;
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
        key: 'buildOutputPathStyles',
        get: function get() {
            return this._buildOutputPathStyles;
        },
        set: function set(value) {
            this._buildOutputPathStyles = value;
        }
    }, {
        key: 'buildOutputPathPlugin',
        get: function get() {
            return this._buildOutputPathPlugin;
        },
        set: function set(value) {
            this._buildOutputPathPlugin = value;
        }
    }]);

    return Common;
}();

exports.default = Common;