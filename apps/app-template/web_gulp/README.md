### hybird framework template [html]

# [gulp-builders](https://www.npmjs.com/package/gulp-builders)

## make gulp build easier !

* open multi modules for listeners(diff ports), see example bellow
* build multi modules
* config ModuleModel and CommonModel path
* tinypng to compress images
* custom html template

### config package.json
```$xslt
{
    "devDependencies": {
        "gulp": "github:gulpjs/gulp#4.0",
        "gulp-builders": "^1.0.1"
    }
}

```

### config gulpfile.babel.js
```$xslt
import gulp from "gulp";
import {Build, ModuleModel, CommonModel} from "gulp-builders";  //"./build-script-output/src/Build";

let commonModel = new CommonModel();
let modules = [new ModuleModel("20170915hd", 7780, "index")];

gulp.task('default', Build.run(modules, commonModel));          //no imageMin and zip
gulp.task('build', Build.build(modules, commonModel));          //do imageMin and zip
gulp.task('clean', Build.clean());                              //remove ./build/
```

### run project
```$xslt
$ gulp
```

### build project
```
$ gulp build
```

### clean project
```
$ gulp clean
```

### publish to npm
```$xslt
npm pack && npm publish
```

## Unresolved function or method
```
//Issues when coding node.js using IntelliJ IDEA , please check all
IntelliJ IDEA -> Preferences -> JavaScript -> Libraries -> [Ensure 'Node.js Globals' is checked]
```

## gulp upgrade to 4.0

```
# Uninstall previous Gulp installation and related packages, if any
$ npm rm gulp -g
$ npm rm gulp-cli -g
$ cd [your-project-dir/]
$ npm rm gulp --save-dev
$ npm rm gulp --save
$ npm rm gulp --save-optional
$ npm cache clean

# Install the latest Gulp CLI tools globally
$ npm install gulpjs/gulp-cli -g

# Install Gulp 4 into your project from 4.0 GitHub branch as dev dependency
$ npm install gulpjs/gulp#4.0 --save-dev

# Check the versions installed. Make sure your versions are not lower than shown.
$ gulp -v
```

#### src dirs

* dirs in project
```$xslt
.
├── README.md
├── build
├── gulpfile.babel.js
├── node_modules
├── package-lock.json
├── package.json
└── src

```
* dirs in project/src
```$xslt
.
├── base
│   ├── common
│   │   ├── img
│   │   │   ├── backtop.png
│   │   │   └── share.png
│   │   ├── js
│   │   │   └── common.js
│   │   └── scss
│   │       ├── _common.scss
│   │       ├── _mixins.scss
│   │       ├── _reset.scss
│   │       └── main.scss
│   └── plugins
│       ├── sm
│       │   ├── sm.min.css
│       │   └── sm.min.js
│       ├── url.min.js
│       ├── vue-resource.min.js
│       └── vue.min.js
├── modules
│   └── 20170915hd
│       ├── html
│       │   └── index.html
│       ├── js
│       │   └── index.js
│       └── scss
│           └── index.scss
└── templates
    ├── adds
    │   ├── links.html
    │   ├── scripts.html
    │   └── tools.html
    ├── template-clean.html
    └── template.html

```

#### example run
```$xslt
$ gulp
[17:18:42] Requiring external module babel-register
[17:18:43] Using gulpfile ~/workspace/template/gulp/gulpfile.babel.js
[17:18:43] Starting 'default'...
[17:18:43] Starting 'cleanTask'...
[17:18:43] Starting 'cleanTask'...
[17:18:43] Finished 'cleanTask' after 12 ms
[17:18:43] Finished 'cleanTask' after 14 ms
[17:18:43] Starting 'buildImagesTask'...
[17:18:43] Starting 'buildImagesCommonTask'...
[17:18:43] Starting 'buildScssCommonTask'...
[17:18:43] Starting 'buildJsCommonTask'...
[17:18:43] Starting 'buildPluginsTask'...
[17:18:43] Starting 'buildScssTask'...
[17:18:43] Starting 'buildJsTask'...
[17:18:43] Starting 'buildHtmlTask'...
[17:18:43] Starting 'buildImagesTask'...
[17:18:43] Starting 'buildImagesCommonTask'...
[17:18:43] Starting 'buildScssCommonTask'...
[17:18:43] Starting 'buildJsCommonTask'...
[17:18:43] Starting 'buildPluginsTask'...
[17:18:43] Starting 'buildScssTask'...
[17:18:43] Starting 'buildJsTask'...
[17:18:43] Starting 'buildHtmlTask'...
[17:18:43] Finished 'buildImagesTask' after 42 ms
[17:18:43] Finished 'buildImagesTask' after 42 ms
not find custom template: Error: ENOENT: no such file or directory, access './src/templates/20170915hd.html' and will use default template : ./src/templates/template.html
[17:18:43] Finished 'buildHtmlTask' after 270 ms
not find custom template: Error: ENOENT: no such file or directory, access './src/templates/20170915hd.html' and will use default template : ./src/templates/template.html
[17:18:43] Finished 'buildHtmlTask' after 271 ms
[17:18:44] Finished 'buildJsCommonTask' after 475 ms
[17:18:44] Finished 'buildJsTask' after 479 ms
[17:18:44] Finished 'buildJsCommonTask' after 480 ms
[17:18:44] Finished 'buildJsTask' after 527 ms
[17:18:44] Finished 'buildImagesCommonTask' after 531 ms
[17:18:44] Finished 'buildImagesCommonTask' after 532 ms
[17:18:44] Finished 'buildScssTask' after 588 ms
[17:18:44] Finished 'buildScssTask' after 588 ms
[17:18:44] Finished 'buildScssCommonTask' after 596 ms
[17:18:44] Finished 'buildScssCommonTask' after 596 ms
[17:18:44] Finished 'buildPluginsTask' after 612 ms
[17:18:44] Starting 'syncTask'...
[17:18:44] Starting 'watchTask'...
[17:18:44] Finished 'buildPluginsTask' after 650 ms
[17:18:44] Starting 'syncTask'...
[17:18:44] Starting 'watchTask'...
[Browsersync] Access URLs:
 -------------------------------------
       Local: http://localhost:7780
    External: http://10.47.60.128:7780
 -------------------------------------
          UI: http://localhost:7880
 UI External: http://10.47.60.128:7880
 -------------------------------------
[Browsersync] Serving files from: ./build/
[Browsersync] Access URLs:
 -------------------------------------
       Local: http://localhost:7781
    External: http://10.47.60.128:7781
 -------------------------------------
          UI: http://localhost:7881
 UI External: http://10.47.60.128:7881
 -------------------------------------
[Browsersync] Serving files from: ./build/
```

#### example build
```$xslt
gulp build
[17:27:30] Requiring external module babel-register
[17:27:31] Using gulpfile ~/workspace/template/gulp/gulpfile.babel.js
[17:27:31] Starting 'build'...
[17:27:31] Starting 'cleanTask'...
[17:27:31] Starting 'cleanTask'...
[17:27:31] Finished 'cleanTask' after 10 ms
[17:27:31] Finished 'cleanTask' after 12 ms
[17:27:31] Starting 'buildImagesTask'...
[17:27:31] Starting 'buildImagesCommonTask'...
[17:27:31] Starting 'buildScssCommonTask'...
[17:27:31] Starting 'buildJsCommonTask'...
[17:27:31] Starting 'buildPluginsTask'...
[17:27:31] Starting 'buildScssTask'...
[17:27:31] Starting 'buildJsTask'...
[17:27:31] Starting 'buildHtmlTask'...
[17:27:31] Starting 'buildImagesTask'...
[17:27:31] Starting 'buildImagesCommonTask'...
[17:27:31] Starting 'buildScssCommonTask'...
[17:27:31] Starting 'buildJsCommonTask'...
[17:27:31] Starting 'buildPluginsTask'...
[17:27:31] Starting 'buildScssTask'...
[17:27:31] Starting 'buildJsTask'...
[17:27:31] Starting 'buildHtmlTask'...
[17:27:31] gulp-tinypng-nokey : [compress completed] skiped: 0 imgs, compressed: 0 imgs, totalSize: 0%
[17:27:31] gulp-tinypng-nokey : [compress completed] skiped: 0 imgs, compressed: 0 imgs, totalSize: 0%
[17:27:31] Finished 'buildImagesTask' after 49 ms
[17:27:31] Finished 'buildImagesTask' after 48 ms
not find custom template: Error: ENOENT: no such file or directory, access './src/templates/20170915hd.html' and will use default template : ./src/templates/template.html
[17:27:31] Finished 'buildHtmlTask' after 239 ms
not find custom template: Error: ENOENT: no such file or directory, access './src/templates/20170915hd.html' and will use default template : ./src/templates/template.html
[17:27:31] Finished 'buildHtmlTask' after 239 ms
[17:27:31] gulp-tinypng-nokey : [tinypng request] backtop.png
[17:27:32] gulp-tinypng-nokey : [tinypng request] backtop.png
[17:27:32] Finished 'buildJsCommonTask' after 452 ms
[17:27:32] Finished 'buildJsTask' after 456 ms
[17:27:32] Finished 'buildJsTask' after 456 ms
[17:27:32] Finished 'buildJsCommonTask' after 459 ms
[17:27:32] Finished 'buildScssTask' after 526 ms
[17:27:32] Finished 'buildScssTask' after 526 ms
[17:27:32] Finished 'buildScssCommonTask' after 558 ms
[17:27:32] Finished 'buildScssCommonTask' after 560 ms
[17:27:32] Finished 'buildPluginsTask' after 564 ms
[17:27:32] Finished 'buildPluginsTask' after 571 ms
[17:27:34] gulp-tinypng-nokey : [compressing] Ok backtop.png (97.3%)
[17:27:34] gulp-tinypng-nokey : [tinypng request] share.png
[17:27:35] gulp-tinypng-nokey : [compressing] Ok backtop.png (97.3%)
[17:27:35] gulp-tinypng-nokey : [tinypng request] share.png
[17:27:35] gulp-tinypng-nokey : [compressing] Ok share.png (97.7%)
[17:27:35] gulp-tinypng-nokey : [compress completed] skiped: 0 imgs, compressed: 3 imgs, totalSize: 97.38%
[17:27:35] Finished 'buildImagesCommonTask' after 4.24 s
[17:27:35] Starting 'buildZipTask'...
[17:27:35] Finished 'buildZipTask' after 4.78 ms
[17:27:36] gulp-tinypng-nokey : [compressing] Ok share.png (97.7%)
[17:27:36] gulp-tinypng-nokey : [compress completed] skiped: 0 imgs, compressed: 4 imgs, totalSize: 97.43%
[17:27:36] Finished 'buildImagesCommonTask' after 5.24 s
[17:27:36] Starting 'buildZipTask'...
[17:27:36] Finished 'buildZipTask' after 4.22 ms
[17:27:36] Finished 'build' after 5.27 s
maokangren:gulp maokangren$
```

