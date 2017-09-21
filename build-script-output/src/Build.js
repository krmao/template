"use strict";

Object.defineProperty(exports, "__esModule", {
    value: true
});
exports.ModuleModel = exports.CommonModel = exports.Build = undefined;

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _gulp = require("gulp");

var _gulp2 = _interopRequireDefault(_gulp);

var _gulpZip = require("gulp-zip");

var _gulpZip2 = _interopRequireDefault(_gulpZip);

var _gulpTap = require("gulp-tap");

var _gulpTap2 = _interopRequireDefault(_gulpTap);

var _gulpSass = require("gulp-sass");

var _gulpSass2 = _interopRequireDefault(_gulpSass);

var _gulpBabel = require("gulp-babel");

var _gulpBabel2 = _interopRequireDefault(_gulpBabel);

var _gulpRename = require("gulp-rename");

var _gulpRename2 = _interopRequireDefault(_gulpRename);

var _gulpUglify = require("gulp-uglify");

var _gulpUglify2 = _interopRequireDefault(_gulpUglify);

var _gulpReplace = require("gulp-replace");

var _gulpReplace2 = _interopRequireDefault(_gulpReplace);

var _gulpStripComments = require("gulp-strip-comments");

var _gulpStripComments2 = _interopRequireDefault(_gulpStripComments);

var _gulpFileInclude = require("gulp-file-include");

var _gulpFileInclude2 = _interopRequireDefault(_gulpFileInclude);

var _gulpAutoprefixer = require("gulp-autoprefixer");

var _gulpAutoprefixer2 = _interopRequireDefault(_gulpAutoprefixer);

var _gulpTinypngNokey = require("gulp-tinypng-nokey");

var _gulpTinypngNokey2 = _interopRequireDefault(_gulpTinypngNokey);

var _del = require("del");

var _del2 = _interopRequireDefault(_del);

var _path = require("path");

var _path2 = _interopRequireDefault(_path);

var _browserSync = require("browser-sync");

var _browserSync2 = _interopRequireDefault(_browserSync);

var _CommonModel3 = require("./model/CommonModel");

var _CommonModel4 = _interopRequireDefault(_CommonModel3);

var _ModuleModel3 = require("./model/ModuleModel");

var _ModuleModel4 = _interopRequireDefault(_ModuleModel3);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

var Build = exports.Build = function () {
    function Build(module, commonModel) {
        _classCallCheck(this, Build);

        this._module = module;
        this._commonModel = commonModel;
        this._server = _browserSync2.default.create(this.module.indexName);
    }

    _createClass(Build, [{
        key: "runModule",
        value: function runModule() {
            var _this = this;
            return _gulp2.default.series(function cleanTask(done) {
                _this.clean();
                done();
            }, _gulp2.default.parallel(function buildImagesTask() {
                return _this.buildImages(false);
            }, function buildImagesCommonTask() {
                return _this.buildImagesCommon(false);
            }, function buildScssCommonTask() {
                return _this.buildScssCommon();
            }, function buildJsCommonTask() {
                return _this.buildJsCommon();
            }, function buildPluginsTask() {
                return _this.buildPlugins();
            }, function buildScssTask() {
                return _this.buildScss();
            }, function buildJsTask() {
                return _this.buildJs();
            }, function buildHtmlTask() {
                return _this.buildHtml();
            }), _gulp2.default.parallel(function syncTask() {
                _this.sync();
            }, function watchTask() {
                _this.watch();
            }));
        }
    }, {
        key: "buildModule",
        value: function buildModule() {
            var _this = this;
            return _gulp2.default.series(function cleanTask(done) {
                _this.clean();
                done();
            }, _gulp2.default.parallel(function buildImagesTask() {
                return _this.buildImages(true);
            }, function buildImagesCommonTask() {
                return _this.buildImagesCommon(true);
            }, function buildScssCommonTask() {
                return _this.buildScssCommon();
            }, function buildJsCommonTask() {
                return _this.buildJsCommon();
            }, function buildPluginsTask() {
                return _this.buildPlugins();
            }, function buildScssTask() {
                return _this.buildScss();
            }, function buildJsTask() {
                return _this.buildJs();
            }, function buildHtmlTask() {
                return _this.buildHtml();
            }), _gulp2.default.series(function buildZipTask(done) {
                _this.zipAll();
                done();
            }));
        }
    }, {
        key: "sync",
        value: function sync() {
            var _this = this;
            if (_this.module.indexName === null) {
                return this.server.init({
                    server: {
                        baseDir: './build/'
                    },
                    port: _this.module.port,
                    ui: {
                        port: _this.module.port + 100
                    },
                    reloadDelay: 0,
                    browser: "google chrome",
                    watchOptions: {
                        ignored: 'node_modules/*',
                        ignoreInitial: true
                    },
                    injectChanges: true
                });
            } else {
                return this.server.init({
                    server: {
                        baseDir: './build/',
                        index: 'static/' + this.module.moduleName + '/' + this.module.indexName + '.html'
                    },
                    port: _this.module.port,
                    ui: {
                        port: _this.module.port + 100
                    },
                    reloadDelay: 0,
                    browser: "google chrome",
                    watchOptions: {
                        ignored: 'node_modules/*',
                        ignoreInitial: true
                    },
                    injectChanges: true
                });
            }
        }
    }, {
        key: "watch",
        value: function watch() {
            var _this = this;
            _gulp2.default.watch([_this.module.srcPathHtmls + '*.html', _this.module.srcPathTemplateFile], _gulp2.default.series(function cleanHtml(done) {
                _this.cleanHtml();
                return done();
            }, function buildHtml() {
                return _this.buildHtml();
            }, function reload(done) {
                _this.server.reload();
                return done();
            }));
            _gulp2.default.watch([_this.module.srcPathStyles + '*.scss'], _gulp2.default.series(function cleanScss(done) {
                _this.cleanScss();
                return done();
            }, function buildScss() {
                return _this.buildScss();
            }, function reload(done) {
                _this.server.reload();
                return done();
            }));
            _gulp2.default.watch([_this.module.srcPathJavaScripts + '*.js'], _gulp2.default.series(function cleanJs(done) {
                _this.cleanJs();
                return done();
            }, function buildJs() {
                return _this.buildJs();
            }, function reload(done) {
                _this.server.reload();
                return done();
            }));
            _gulp2.default.watch([_this.module.srcPathImages + '*'], _gulp2.default.series(function cleanImages(done) {
                _this.cleanImages();
                return done();
            }, function buildImages() {
                return _this.buildImages(false);
            }, function reload(done) {
                _this.server.reload();
                return done();
            }));
        }
    }, {
        key: "zipAll",
        value: function zipAll() {
            this.zipJs();
            this.zipImages();
            this.zipScss();
            this.zipHtml();
        }
    }, {
        key: "zipJs",
        value: function zipJs() {
            return _gulp2.default.src(this.module.zipSrcPathJavaScripts, { base: this.module.zipPathBaseJavaScripts }).pipe((0, _gulpZip2.default)(this.module.zipFileNameJavaScripts)).pipe(_gulp2.default.dest(this.module.zipPathOutput));
        }
    }, {
        key: "zipImages",
        value: function zipImages() {
            return _gulp2.default.src(this.module.zipSrcPathImages, { base: this.module.zipPathBaseImages }).pipe((0, _gulpZip2.default)(this.module.zipFileNameImages)).pipe(_gulp2.default.dest(this.module.zipPathOutput));
        }
    }, {
        key: "zipScss",
        value: function zipScss() {
            return _gulp2.default.src(this.module.zipSrcPathStyles, { base: this.module.zipPathBaseStyles }).pipe((0, _gulpZip2.default)(this.module.zipFileNameStyles)).pipe(_gulp2.default.dest(this.module.zipPathOutput));
        }
    }, {
        key: "zipHtml",
        value: function zipHtml() {
            return _gulp2.default.src(this.module.zipSrcPathHtmls, { base: this.module.zipPathBaseHtmls }).pipe((0, _gulpZip2.default)(this.module.zipFileNameHtmls)).pipe(_gulp2.default.dest(this.module.zipPathOutput));
        }
    }, {
        key: "buildImages",
        value: function buildImages(enableMin) {
            if (enableMin) return _gulp2.default.src(this.module.srcPathImages + '*').pipe((0, _gulpTinypngNokey2.default)()).pipe(_gulp2.default.dest(this.module.buildOutputPathImages));else return _gulp2.default.src(this.module.srcPathImages + '*').pipe(_gulp2.default.dest(this.module.buildOutputPathImages));
        }
    }, {
        key: "buildScss",
        value: function buildScss() {
            return _gulp2.default.src(this.module.srcPathStyles + '*.scss').pipe(_gulpStripComments2.default.text()).pipe((0, _gulpReplace2.default)('../img', '../../images/' + this.module.moduleName)).pipe((0, _gulpSass2.default)({ precision: 10, outputStyle: 'compact', errLogToConsole: true }).on('error', _gulpSass2.default.logError)).pipe((0, _gulpAutoprefixer2.default)({ browsers: ['> 1%', 'Last 10 versions', 'IE 8'], cascade: true, remove: true })).pipe(_gulp2.default.dest('./build/static/styles/' + this.module.moduleName + '/')).pipe(_gulp2.default.dest(this.module.buildOutputPathStyles)).pipe((0, _gulpSass2.default)({ precision: 10, outputStyle: 'compressed', errLogToConsole: true }).on('error', _gulpSass2.default.logError)).pipe((0, _gulpAutoprefixer2.default)({ browsers: ['> 1%', 'Last 10 versions', 'IE 8'], cascade: true, remove: true })).pipe((0, _gulpRename2.default)({ suffix: '.min', extname: '.css' })).pipe(_gulp2.default.dest('./build/static/styles/' + this.module.moduleName + '/'));
        }
    }, {
        key: "buildJs",
        value: function buildJs() {
            return _gulp2.default.src(this.module.srcPathJavaScripts + '*.js').pipe(_gulpStripComments2.default.text()).pipe((0, _gulpBabel2.default)({
                presets: ['env']
            })).pipe(_gulp2.default.dest(this.module.buildOutputPathJavaScripts)).pipe((0, _gulpUglify2.default)()).pipe((0, _gulpRename2.default)({ suffix: '.min' })).pipe(_gulp2.default.dest(this.module.buildOutputPathJavaScripts));
        }
    }, {
        key: "buildHtml",
        value: function buildHtml() {
            var _this = this;
            return _gulp2.default.src(_this.module.srcPathHtmls + '*.html').pipe((0, _gulpTap2.default)(function (file) {
                var htmlName = _path2.default.basename(file.path).replace('.html', '');
                _gulp2.default.src(_this.module.srcPathTemplateFile).pipe(_gulpStripComments2.default.text()).pipe((0, _gulpFileInclude2.default)({ prefix: '@@', basepath: '@file', context: { moduleName: _this.module.moduleName, htmlName: htmlName } })).pipe((0, _gulpReplace2.default)('../img', '../../static/images/' + _this.module.moduleName)).pipe((0, _gulpRename2.default)({ basename: htmlName, extname: '.html' })).pipe(_gulp2.default.dest(_this.module.buildOutputPathHtmls));
            }));
        }
    }, {
        key: "buildPlugins",
        value: function buildPlugins() {
            return _gulp2.default.src(this.commonModel.srcPathPluginArrays).pipe(_gulp2.default.dest(this.commonModel.buildOutputPathPlugin));
        }
    }, {
        key: "buildImagesCommon",
        value: function buildImagesCommon(enableMin) {
            if (enableMin) return _gulp2.default.src(this.commonModel.srcPathImages + '*').pipe((0, _gulpTinypngNokey2.default)()).pipe(_gulp2.default.dest(this.commonModel.buildOutputPathImages));else return _gulp2.default.src(this.commonModel.srcPathImages + '*').pipe(_gulp2.default.dest(this.commonModel.buildOutputPathImages));
        }
    }, {
        key: "buildJsCommon",
        value: function buildJsCommon() {
            return _gulp2.default.src(this.commonModel.srcPathJavaScripts).pipe(_gulpStripComments2.default.text()).pipe((0, _gulpBabel2.default)({
                presets: ['env']
            })).pipe(_gulp2.default.dest(this.commonModel.buildOutputPathJavaScripts)).pipe((0, _gulpUglify2.default)()).pipe((0, _gulpRename2.default)({ suffix: '.min' })).pipe(_gulp2.default.dest(this.commonModel.buildOutputPathJavaScripts));
        }
    }, {
        key: "buildScssCommon",
        value: function buildScssCommon() {
            return _gulp2.default.src(this.commonModel.srcPathStyles).pipe(_gulpStripComments2.default.text()).pipe((0, _gulpSass2.default)({ precision: 10, outputStyle: 'compact', errLogToConsole: true }).on('error', _gulpSass2.default.logError)).pipe((0, _gulpAutoprefixer2.default)({ browsers: ['> 1%', 'Last 10 versions', 'IE 8'], cascade: true, remove: true })).pipe(_gulp2.default.dest(this.commonModel.buildOutputPathStyles)).pipe((0, _gulpSass2.default)({ precision: 10, outputStyle: 'compressed', errLogToConsole: true }).on('error', _gulpSass2.default.logError)).pipe((0, _gulpAutoprefixer2.default)({ browsers: ['> 1%', 'Last 10 versions', 'IE 8'], cascade: true, remove: true })).pipe((0, _gulpRename2.default)({ suffix: '.min', extname: '.css' })).pipe(_gulp2.default.dest(this.commonModel.buildOutputPathStyles));
        }
    }, {
        key: "cleanImages",
        value: function cleanImages() {
            return _del2.default.sync([this.module.buildOutputPathImages, this.module.zipPathOutput + this.module.zipFileNameImages]);
        }
    }, {
        key: "cleanHtml",
        value: function cleanHtml() {
            return _del2.default.sync([this.module.buildOutputPathHtmls, this.module.zipPathOutput + this.module.zipFileNameHtmls]);
        }
    }, {
        key: "cleanScss",
        value: function cleanScss() {
            return _del2.default.sync([this.module.buildOutputPathStyles, this.module.zipPathOutput + this.module.zipFileNameStyles]);
        }
    }, {
        key: "cleanJs",
        value: function cleanJs() {
            return _del2.default.sync([this.module.buildOutputPathJavaScripts, this.module.zipPathOutput + this.module.zipFileNameJavaScripts]);
        }
    }, {
        key: "cleanCommon",
        value: function cleanCommon() {
            return _del2.default.sync([this.commonModel.buildOutputPathImages, this.commonModel.buildOutputPathJavaScripts, this.commonModel.buildOutputPathStyles, this.commonModel.buildOutputPathPlugin]);
        }
    }, {
        key: "clean",
        value: function clean() {
            this.cleanCommon();
            this.cleanJs();
            this.cleanScss();
            this.cleanHtml();
            this.cleanImages();
        }
    }, {
        key: "toString",
        value: function toString() {
            return "\n>>>>>>>>************************************************************<<<<<<<<" + "\n>>>>>>>>************************************************************<<<<<<<<" + "\n>>>>>>>>************************************************************<<<<<<<<" + "\n\n            hybird framework 【GULP】" + "\n\n                         ---- by krmao 20170919" + "\n\n>>>>>>>>************************************************************<<<<<<<<" + "\n>>>>>>>>************************************************************<<<<<<<<" + "\n>>>>>>>>************************************************************<<<<<<<<\n\n";
        }
    }, {
        key: "module",
        get: function get() {
            return this._module;
        },
        set: function set(value) {
            this._module = value;
        }
    }, {
        key: "commonModel",
        get: function get() {
            return this._commonModel;
        },
        set: function set(value) {
            this._commonModel = value;
        }
    }, {
        key: "server",
        get: function get() {
            return this._server;
        },
        set: function set(value) {
            this._server = value;
        }
    }], [{
        key: "run",
        value: function run(modules, commonModel) {
            var tasks = [];
            for (var index in modules) {
                if (modules.hasOwnProperty(index)) {
                    tasks[index] = new Build(modules[index], commonModel).runModule();
                }
            }
            return _gulp2.default.parallel(tasks);
        }
    }, {
        key: "build",
        value: function build(modules, commonModel) {
            var tasks = [];
            for (var index in modules) {
                if (modules.hasOwnProperty(index)) {
                    tasks[index] = new Build(modules[index], commonModel).buildModule();
                }
            }
            return _gulp2.default.parallel(tasks);
        }
    }, {
        key: "clean",
        value: function clean() {
            return function () {
                return (0, _del2.default)(['./build/']);
            };
        }
    }]);

    return Build;
}();

var CommonModel = exports.CommonModel = function (_CommonModel2) {
    _inherits(CommonModel, _CommonModel2);

    function CommonModel() {
        _classCallCheck(this, CommonModel);

        return _possibleConstructorReturn(this, (CommonModel.__proto__ || Object.getPrototypeOf(CommonModel)).apply(this, arguments));
    }

    return CommonModel;
}(_CommonModel4.default);

var ModuleModel = exports.ModuleModel = function (_ModuleModel2) {
    _inherits(ModuleModel, _ModuleModel2);

    function ModuleModel() {
        _classCallCheck(this, ModuleModel);

        return _possibleConstructorReturn(this, (ModuleModel.__proto__ || Object.getPrototypeOf(ModuleModel)).apply(this, arguments));
    }

    return ModuleModel;
}(_ModuleModel4.default);