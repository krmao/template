import gulp from "gulp";
import Build from "./build-script/Build";
import ModuleModel from "./build-script/model/ModuleModel";
import CommonModel from "./build-script/model/CommonModel";

let commonModel = new CommonModel();
let modules = [new ModuleModel("20170915hd", 7780, "index")/*, new ModuleModel("20170915hd_other",7781, "index2")*/];

gulp.task('default', Build.run(modules, commonModel)); //no imageMin and zip
gulp.task('build', Build.build(modules, commonModel)); //do imageMin and zip
gulp.task('clean', Build.clean()); //remove ./build/
