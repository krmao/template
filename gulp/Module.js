function Module(moduleName) {
    this._moduleDirName = moduleName;
    this._imgDirName = "img";
    this._htmlDirName = "html";
    this._jsDirName = "js";
    this._scssDirName = "scss";
    this._tmpDirName = "tmp";

    Object.defineProperty(this, "moduleName", {
        get: function () {
            return this._moduleDirName;
        },
        set: function (moduleName) {
            this._moduleDirName = moduleName;
        }
    });
    Object.defineProperty(this, "modulePath", {
        get: function () {
            return './src/modules/' + this.moduleName + '/';
        }
    });
    Object.defineProperty(this, "modulePathImg", {
        get: function () {
            return this.modulePath + this._imgDirName;
        }
    });
    Object.defineProperty(this, "modulePathJs", {
        get: function () {
            return this.modulePath + this._jsDirName;
        }
    });
    Object.defineProperty(this, "modulePathHtml", {
        get: function () {
            return this.modulePath + this._htmlDirName;
        }
    });
    Object.defineProperty(this, "modulePathScss", {
        get: function () {
            return this.modulePath + this._scssDirName;
        }
    });
    Object.defineProperty(this, "buildPath", {
        get: function () {
            return './build/';
        }
    });
    Object.defineProperty(this, "moduleTemplateFilePath", {
        get: function () {
            var fs = require("fs");
            var templateFilePath = './src/templates/' + this.moduleName + '.html';
            try {
                fs.accessSync(templateFilePath, fs.F_OK);
            } catch (e) {
                templateFilePath = './src/templates/template.html';
                console.warn("not find custom template: " + e + " and will use default template : " + templateFilePath);
            }
            return templateFilePath;
        }
    });
    Object.defineProperty(this, "buildModulePath", {
        get: function () {
            return this.buildPath + this.moduleName + '/';
        }
    });
    Object.defineProperty(this, "buildModulePathTmp", {
        get: function () {
            return this.buildModulePath + this._tmpDirName;
        }
    });
    Object.defineProperty(this, "buildModulePathImg", {
        get: function () {
            return this.buildModulePath + this._imgDirName;
        }
    });
    Object.defineProperty(this, "buildModulePathJs", {
        get: function () {
            return this.buildModulePath + this._jsDirName;
        }
    });
    Object.defineProperty(this, "buildModulePathHtml", {
        get: function () {
            return this.buildModulePath;
        }
    });
    Object.defineProperty(this, "buildModulePathScss", {
        get: function () {
            return this.buildModulePath + 'css';
        }
    });

    Module.prototype.toString = function () {
        return "\n==============================>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" +
            "\nmoduleName:\t\t\t\t" + this.moduleName +
            "\nmodulePath:\t\t\t\t" + this.modulePath +
            "\nmodulePathImg:\t\t\t" + this.modulePathImg +
            "\nmodulePathJs:\t\t\t" + this.modulePathJs +
            "\nmodulePathHtml:\t\t\t" + this.modulePathHtml +
            "\nmodulePathScss:\t\t\t" + this.modulePathScss +
            "\nbuildPath:\t\t\t\t" + this.buildPath +
            "\nbuildModulePath:\t\t" + this.buildModulePath +
            "\nbuildModulePathTmp:\t\t" + this.buildModulePathTmp +
            "\nbuildModulePathImg:\t\t" + this.buildModulePathImg +
            "\nbuildModulePathJs:\t\t" + this.buildModulePathJs +
            "\nbuildModulePathHtml:\t" + this.buildModulePathHtml +
            "\nbuildModulePathScss:\t" + this.buildModulePathScss +
            "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<==============================\n";
    }
}

module.exports = Module;