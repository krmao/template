var gulp = require('gulp');
var eslint = require('gulp-eslint');
var uglify = require('gulp-uglify');
var browserSync = require('browser-sync');
var rename = require("gulp-rename");
var fileInclude = require('gulp-file-include');
var sass = require('gulp-sass');
var autoPreFixer = require('gulp-autoprefixer');
var del = require('del');
var Module = require('./Module.js');
var imageMin = require('gulp-imagemin');
var proxy = require('http-proxy-middleware');

//==========================================================================================
// build
//==========================================================================================

//images
function buildImages(module) {
    return gulp.src(module.modulePathImg + '/*').pipe(imageMin()).pipe(gulp.dest(module.buildModulePathImg));
}

//sass
function buildScss(module) {
    return gulp.src(module.modulePathScss + '/*.scss')
        .pipe(sass({precision: 10, outputStyle: 'compact', errLogToConsole: true}).on('error', sass.logError))
        .pipe(autoPreFixer({browsers: ['> 1%', 'Last 10 versions', 'IE 8'], cascade: true, remove: true}))
        .pipe(rename({suffix: '', extname: '.css'}))
        .pipe(gulp.dest(module.buildModulePathScss))
        .pipe(browserSync.reload({stream: true}));
}

//scripts
function buildJs(module) {
    return gulp.src(module.modulePathJs + '/*.js')
        .pipe(eslint({configFle: "./.eslintrc.js"}))
        .pipe(eslint.format())
        .pipe(eslint.failOnError())
        .pipe(uglify())
        .pipe(rename({suffix: ''}))
        .pipe(gulp.dest(module.buildModulePathJs));
}

//html
function buildHtml(module) {
    return gulp.src(module.moduleTemplateFilePath)
        .pipe(fileInclude({prefix: '@@', basepath: '@file', context: {moduleName: module.moduleName, htmlName: 'index'}}))
        .pipe(rename({basename: 'index', extname: '.html'}))
        .pipe(gulp.dest(module.buildModulePathHtml));
}

//watch
function watch(module) {
    gulp.watch([module.modulePathJs + '/*.js'], gulp.series(buildJsTask, reloadTask));
    gulp.watch([module.modulePathScss + '/*.scss'], gulp.series(buildScssTask, reloadTask));
    gulp.watch([module.modulePathHtml + '/*.html', module.moduleTemplateFilePath], gulp.series(buildHtmlTask, reloadTask));
    gulp.watch([module.modulePathImg + '/*'], gulp.series(cleanImagesTask, buildImagesTask, reloadTask));
}

//remove images build
function cleanImages(module) {
    return del(module.buildModulePathImg);
}

//==========================================================================================
// tasks
//==========================================================================================

function buildImagesTask() {
    return buildImages(module);
}

function buildScssTask() {
    return buildScss(module);
}

function buildJsTask() {
    return buildJs(module);
}

function buildHtmlTask() {
    return buildHtml(module);
}

function watchTask() {
    return watch(module);
}

function cleanImagesTask() {
    return cleanImages(module);
}

function cleanTask() {
    return del(['build']);
}

function reloadTask(done) {
    browserSync.reload();
    done();
}

function syncTask() {
    return browserSync({
        server: {
            baseDir: module.buildModulePathHtml
        },
        port: 9999
    });
}

//==========================================================================================
// default
//==========================================================================================

var module = new Module('20170909');

gulp.task('default',
    gulp.series(
        cleanTask,
        gulp.parallel(buildImagesTask, buildScssTask, buildJsTask, buildHtmlTask),
        gulp.parallel(syncTask, watchTask)
    )
);