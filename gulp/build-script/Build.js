import gulp from "gulp";
import zip from "gulp-zip";
import tap from "gulp-tap";
import sass from "gulp-sass";
import babel from "gulp-babel";
import rename from "gulp-rename";
// import eslint from "gulp-eslint";
import uglify from "gulp-uglify";
import replace from "gulp-replace";
import imageMin from "gulp-imagemin";
import strip from "gulp-strip-comments";    //删除代码注释
import fileInclude from "gulp-file-include";
import autoPreFixer from "gulp-autoprefixer";

import del from "del";
import path from "path";
import browserSync from "browser-sync";

// import proxy from "http-proxy-middleware";

class Build {

    constructor(module, commonModel) {
        this._module = module;
        this._commonModel = commonModel;
        console.log(this.toString());
    }

    get module() {
        return this._module;
    }

    set module(value) {
        this._module = value;
    }

    get commonModel() {
        return this._commonModel;
    }

    set commonModel(value) {
        this._commonModel = value;
    }

    //====================================================================================================
    //  run and watch
    //====================================================================================================

    static run(modules, commonModel) {
        let tasks = [];
        for (let index in modules) {
            if (modules.hasOwnProperty(index)) {
                tasks[index] = new Build(modules[index], commonModel).runModule();
            }
        }
        return gulp.parallel(tasks);
    }

    runModule() {
        let _this = this;
        return gulp.series(
            function cleanTask(done) {
                _this.clean();
                done()
            },
            gulp.parallel(
                function buildImagesTask() {
                    //如果返回 gulp.src(gulp可接受的五种类型) ,则直接返回
                    return _this.buildImages(false);
                }
                , function buildImagesCommonTask() {
                    return _this.buildImagesCommon(false);
                }
                , function buildScssCommonTask() {
                    return _this.buildScssCommon();
                }
                , function buildJsCommonTask() {
                    return _this.buildJsCommon();
                }
                , function buildPluginsTask() {
                    return _this.buildPlugins();
                }
                , function buildScssTask() {
                    return _this.buildScss();
                }
                , function buildJsTask() {
                    return _this.buildJs();
                }
                , function buildHtmlTask() {
                    return _this.buildHtml();
                }
            )
            , gulp.parallel(
                function syncTask() {
                    _this.sync();
                },
                function watchTask() {
                    _this.watch();
                }
            )
        )
    }

    static build(modules, commonModel) {
        let tasks = [];
        for (let index in modules) {
            if (modules.hasOwnProperty(index)) {
                tasks[index] = new Build(modules[index], commonModel).buildModule();
            }
        }
        return gulp.parallel(tasks);
    }

    buildModule() {
        let _this = this;
        return gulp.series(
            function cleanTask(done) {
                _this.clean();
                done()
            },
            gulp.parallel(
                function buildImagesTask() {
                    //如果返回 gulp.src(gulp可接受的五种类型) ,则直接返回
                    return _this.buildImages(true);
                }
                , function buildImagesCommonTask() {
                    return _this.buildImagesCommon(true);
                }
                , function buildScssCommonTask() {
                    return _this.buildScssCommon();
                }
                , function buildJsCommonTask() {
                    return _this.buildJsCommon();
                }
                , function buildPluginsTask() {
                    return _this.buildPlugins();
                }
                , function buildScssTask() {
                    return _this.buildScss();
                }
                , function buildJsTask() {
                    return _this.buildJs();
                }
                , function buildHtmlTask() {
                    return _this.buildHtml();
                }
            )
            , gulp.series(
                function buildZipTask(done) {
                    _this.zipAll();
                    done()
                }
            )
        )
    }

    sync() {
        let _this = this;
        if (_this.module.indexName === null) {
            return browserSync.create(_this.module.indexName).init({
                server: {
                    baseDir: './build/',
                },
                port: _this.module.port,
                ui: {
                    port: _this.module.port + 100,
                }
            });
        } else {
            return browserSync.create(_this.module.indexName).init({
                server: {
                    baseDir: './build/',
                    index: 'static/' + this.module.moduleName + '/' + this.module.indexName + '.html'
                },
                port: _this.module.port,
                ui: {
                    port: _this.module.port + 100,
                }
            });
        }
    }

    watch() {
        let _this = this;
        gulp.watch([_this.module.srcPathHtmls + '*.html', _this.module.srcPathTemplateFile],
            gulp.series(
                function cleanHtml(done) {
                    _this.cleanHtml();
                    return done();
                },
                function buildHtml() {
                    return _this.buildHtml();
                },
                function reload(done) {
                    browserSync.reload();
                    return done();
                }
            )
        );
        gulp.watch([_this.module.srcPathStyles + '*.scss'],
            gulp.series(
                function cleanScss(done) {
                    _this.cleanScss();
                    return done();
                },
                function buildScss() {
                    return _this.buildScss();
                },
                function reload(done) {
                    browserSync.reload();
                    return done();
                }
            )
        );
        gulp.watch([_this.module.srcPathJavaScripts + '*.js'],
            gulp.series(
                function cleanJs(done) {
                    _this.cleanJs();
                    return done();
                },
                function buildJs() {
                    return _this.buildJs();
                },
                function reload(done) {
                    browserSync.reload();
                    return done();
                }
            )
        );
        gulp.watch([_this.module.srcPathImages + '*'],
            gulp.series(
                function cleanImages(done) {
                    _this.cleanImages();
                    return done();
                },
                function buildImages() {
                    return _this.buildImages(false);
                },
                function reload(done) {
                    browserSync.reload();
                    return done();
                }
            )
        );
    }

    //====================================================================================================
    //  zip
    //====================================================================================================

    zipAll() {
        this.zipJs();
        this.zipImages();
        this.zipScss();
        this.zipHtml();
    }

    zipJs() {
        return gulp.src(this.module.zipSrcPathJavaScripts, {base: this.module.zipPathBaseJavaScripts})
            .pipe(zip(this.module.zipFileNameJavaScripts))
            .pipe(gulp.dest(this.module.zipPathOutput));
    }

    zipImages() {
        return gulp.src(this.module.zipSrcPathImages, {base: this.module.zipPathBaseImages})
            .pipe(zip(this.module.zipFileNameImages))
            .pipe(gulp.dest(this.module.zipPathOutput));
    }

    zipScss() {
        return gulp.src(this.module.zipSrcPathStyles, {base: this.module.zipPathBaseStyles})
            .pipe(zip(this.module.zipFileNameStyles))
            .pipe(gulp.dest(this.module.zipPathOutput));
    }

    zipHtml() {
        return gulp.src(this.module.zipSrcPathHtmls, {base: this.module.zipPathBaseHtmls})
            .pipe(zip(this.module.zipFileNameHtmls))
            .pipe(gulp.dest(this.module.zipPathOutput));
    }

    //====================================================================================================
    //  build
    //====================================================================================================

    buildImages(enableMin) {
        if (enableMin)
            return gulp.src(this.module.srcPathImages + '*').pipe(imageMin()).pipe(gulp.dest(this.module.buildOutputPathImages));
        else
            return gulp.src(this.module.srcPathImages + '*').pipe(gulp.dest(this.module.buildOutputPathImages));
    }

    /**
     * sass
     *
     * fixme important
     * 由于生产环境只识别 url("../../images/20170920hd/mt_bg.png")
     * 新框架由于目录结构调整会使用 url("../img/mt_bg.png")
     * 所以最终编译时需要替换为生产环境可识别的路径，即必须执行 .pipe(replace('../img', '../../images/' + module.moduleName))
     *
     * @returns {*}
     */
    buildScss() {
        return gulp.src(this.module.srcPathStyles + '*.scss')
            .pipe(strip.text())
            .pipe(replace('../img', '../../images/' + this.module.moduleName)) //fixme important
            .pipe(sass({precision: 10, outputStyle: 'compact', errLogToConsole: true}).on('error', sass.logError))
            .pipe(autoPreFixer({browsers: ['> 1%', 'Last 10 versions', 'IE 8'], cascade: true, remove: true}))
            .pipe(gulp.dest('./build/static/styles/' + this.module.moduleName + '/'))
            .pipe(gulp.dest(this.module.buildOutputPathStyles))

            .pipe(sass({precision: 10, outputStyle: 'compressed', errLogToConsole: true}).on('error', sass.logError))
            .pipe(autoPreFixer({browsers: ['> 1%', 'Last 10 versions', 'IE 8'], cascade: true, remove: true}))
            .pipe(rename({suffix: '.min', extname: '.css'}))
            .pipe(gulp.dest('./build/static/styles/' + this.module.moduleName + '/'));
    }

    buildJs() {
        return gulp.src(this.module.srcPathJavaScripts + '*.js')
            .pipe(strip.text())
            .pipe(babel({
                presets: ['env']//['env']
            }))
            //.pipe(eslint({configFle: './.eslintrc.js'})).pipe(eslint.format()).pipe(eslint.failOnError())
            .pipe(gulp.dest(this.module.buildOutputPathJavaScripts))
            .pipe(uglify())
            .pipe(rename({suffix: '.min'}))
            .pipe(gulp.dest(this.module.buildOutputPathJavaScripts));
    }

    /**
     * html
     *
     * fixme important
     * 由于生产环境只识别 url("../../static/images/20170920hd/mt_bg.png")
     * 新框架由于目录结构调整会使用 url("../img/mt_bg.png")
     * 所以最终编译时需要替换为生产环境可识别的路径，即必须执行 .pipe(replace('../img', '../../static/images/20170920hd' + module.moduleName))
     *
     * @returns {*}
     */
    buildHtml() {
        let _this = this;
        return gulp.src(_this.module.srcPathHtmls + '*.html')
            .pipe(tap(function (file) {
                    let htmlName = path.basename(file.path).replace('.html', '');
                    gulp.src(_this.module.srcPathTemplateFile)
                        .pipe(strip.text())
                        .pipe(fileInclude({prefix: '@@', basepath: '@file', context: {moduleName: _this.module.moduleName, htmlName: htmlName}}))
                        .pipe(replace('../img', '../../static/images/' + _this.module.moduleName)) //fixme important
                        .pipe(rename({basename: htmlName, extname: '.html'}))
                        .pipe(gulp.dest(_this.module.buildOutputPathHtmls));
                }
            ))
    }

    //====================================================================================================
    //  build-common
    //====================================================================================================

    buildPlugins() {
        return gulp.src(this.commonModel.srcPathPluginArrays).pipe(gulp.dest(this.commonModel.buildOutputPathPlugin))
    }

    buildImagesCommon(enableMin) {
        if (enableMin)
            return gulp.src(this.commonModel.srcPathImages + '*').pipe(imageMin()).pipe(gulp.dest(this.commonModel.buildOutputPathImages));
        else
            return gulp.src(this.commonModel.srcPathImages + '*').pipe(gulp.dest(this.commonModel.buildOutputPathImages));
    }

    buildJsCommon() {
        return gulp.src(this.commonModel.srcPathJavaScripts)
            .pipe(strip.text())
            .pipe(babel({
                presets: ['env']//['env']
            }))
            //.pipe(eslint({configFle: './.eslintrc.js'})).pipe(eslint.format()).pipe(eslint.failOnError())
            .pipe(gulp.dest(this.commonModel.buildOutputPathJavaScripts))
            .pipe(uglify())
            .pipe(rename({suffix: '.min'}))
            .pipe(gulp.dest(this.commonModel.buildOutputPathJavaScripts));
    }

    buildScssCommon() {
        return gulp.src(this.commonModel.srcPathStyles)
            .pipe(strip.text())
            .pipe(sass({precision: 10, outputStyle: 'compact', errLogToConsole: true}).on('error', sass.logError))
            .pipe(autoPreFixer({browsers: ['> 1%', 'Last 10 versions', 'IE 8'], cascade: true, remove: true}))
            .pipe(gulp.dest(this.commonModel.buildOutputPathStyles))

            .pipe(sass({precision: 10, outputStyle: 'compressed', errLogToConsole: true}).on('error', sass.logError))
            .pipe(autoPreFixer({browsers: ['> 1%', 'Last 10 versions', 'IE 8'], cascade: true, remove: true}))
            .pipe(rename({suffix: '.min', extname: '.css'}))
            .pipe(gulp.dest(this.commonModel.buildOutputPathStyles));
    }


    //====================================================================================================
    //  clean
    //====================================================================================================

    cleanImages() {
        return del.sync([this.module.buildOutputPathImages, this.module.zipPathOutput + this.module.zipFileNameImages]);
    }

    cleanHtml() {
        return del.sync([this.module.buildOutputPathHtmls, this.module.zipPathOutput + this.module.zipFileNameHtmls]);
    }

    cleanScss() {
        return del.sync([this.module.buildOutputPathStyles, this.module.zipPathOutput + this.module.zipFileNameStyles]);
    }

    cleanJs() {
        return del.sync([this.module.buildOutputPathJavaScripts, this.module.zipPathOutput + this.module.zipFileNameJavaScripts]);
    }

    cleanCommon() {
        return del.sync([this.commonModel.buildOutputPathImages,
            this.commonModel.buildOutputPathJavaScripts,
            this.commonModel.buildOutputPathStyles,
            this.commonModel.buildOutputPathPlugin]);
    }

    clean() {
        this.cleanCommon();
        this.cleanJs();
        this.cleanScss();
        this.cleanHtml();
        this.cleanImages();
    }

    //====================================================================================================
    //  toString
    //====================================================================================================

    toString() {
        return "\n>>>>>>>>************************************************************<<<<<<<<" +
            "\n>>>>>>>>************************************************************<<<<<<<<" +
            "\n>>>>>>>>************************************************************<<<<<<<<" +
            "\n\n            车享 hybird 构建框架 【GULP】（第三版）" +
            "\n\n                         ---- 20170919" +
            "\n\n>>>>>>>>************************************************************<<<<<<<<" +
            "\n>>>>>>>>************************************************************<<<<<<<<" +
            "\n>>>>>>>>************************************************************<<<<<<<<\n\n" +
            this.module + this.commonModel;
    }
}

export default Build;
