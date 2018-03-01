import gulp from "gulp";
import {Build, ModuleModel, CommonModel} from "gulp-builders";

let commonModel = new CommonModel();
let modules = [new ModuleModel("20170915hd", 7780, "index")];

gulp.task('default', Build.run(modules, commonModel));          //no imageMin and zip
gulp.task('build', Build.build(modules, commonModel));          //do imageMin and zip
gulp.task('clean', Build.clean());                              //remove ./build/

gulp.task('copyToNative', function buildJs() {
    return gulp.src('./build/**/*').pipe(gulp.dest('../android/arsenal/modules/module-fruitshop-hybird/src/main/assets/'))
});
