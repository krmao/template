import gulp from "gulp";
import {Build, ModuleModel, CommonModel} from /*"gulp-builders"; */ "./build-script-output/src/Build";

let commonModel = new CommonModel();
let modules = [new ModuleModel("20170915hd", 7780, "index"), new ModuleModel("20170915hd", 7781)];

gulp.task('default', Build.run(modules, commonModel));          //no imageMin and zip
gulp.task('build', Build.build(modules, commonModel));          //do imageMin and zip
gulp.task('clean', Build.clean());                              //remove ./build/

//=====================================================================================
// for gulp-mbuilder script
//=====================================================================================

import babel from "gulp-babel";
import strip from "gulp-strip-comments";

gulp.task('build-builders', function buildJs() {
    return gulp.src('./build-script/**/*.js').pipe(strip.text()).pipe(babel({presets: ['env']})).pipe(gulp.dest('./build-script-output/src'))
});
