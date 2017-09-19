import fs from "fs";

/**
 * 所有的 path 后面都必须包含 '/'
 */
class Module {

    constructor(moduleName, port, indexName) {
        this._indexName = indexName || null;    //模块默认打开的网页 Ex: 'index'
        this._port = port || 7777;
        this._moduleName = moduleName || '';  //模块名称

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

    get moduleName() {
        return this._moduleName;
    }

    set moduleName(value) {
        this._moduleName = value;
    }

    get indexName() {
        return this._indexName;
    }

    set indexName(value) {
        this._indexName = value;
    }

    get port() {
        return this._port;
    }

    set port(value) {
        this._port = value;
    }

    get zipSrcPathImages() {
        return this._zipSrcPathImages;
    }

    set zipSrcPathImages(value) {
        this._zipSrcPathImages = value;
    }

    get zipSrcPathJavaScripts() {
        return this._zipSrcPathJavaScripts;
    }

    set zipSrcPathJavaScripts(value) {
        this._zipSrcPathJavaScripts = value;
    }

    get zipSrcPathHtmls() {
        return this._zipSrcPathHtmls;
    }

    set zipSrcPathHtmls(value) {
        this._zipSrcPathHtmls = value;
    }

    get zipSrcPathStyles() {
        return this._zipSrcPathStyles;
    }

    set zipSrcPathStyles(value) {
        this._zipSrcPathStyles = value;
    }

    get zipFileNameImages() {
        return this._zipFileNameImages;
    }

    set zipFileNameImages(value) {
        this._zipFileNameImages = value;
    }

    get zipFileNameJavaScripts() {
        return this._zipFileNameJavaScripts;
    }

    set zipFileNameJavaScripts(value) {
        this._zipFileNameJavaScripts = value;
    }

    get zipFileNameHtmls() {
        return this._zipFileNameHtmls;
    }

    set zipFileNameHtmls(value) {
        this._zipFileNameHtmls = value;
    }

    get zipFileNameStyles() {
        return this._zipFileNameStyles;
    }

    set zipFileNameStyles(value) {
        this._zipFileNameStyles = value;
    }

    get zipPathOutput() {
        return this._zipPathOutput;
    }

    set zipPathOutput(value) {
        this._zipPathOutput = value;
    }

    get zipPathBaseImages() {
        return this._zipPathBaseImages;
    }

    set zipPathBaseImages(value) {
        this._zipPathBaseImages = value;
    }

    get zipPathBaseJavaScripts() {
        return this._zipPathBaseJavaScripts;
    }

    set zipPathBaseJavaScripts(value) {
        this._zipPathBaseJavaScripts = value;
    }

    get zipPathBaseStyles() {
        return this._zipPathBaseStyles;
    }

    set zipPathBaseStyles(value) {
        this._zipPathBaseStyles = value;
    }

    get zipPathBaseHtmls() {
        return this._zipPathBaseHtmls;
    }

    set zipPathBaseHtmls(value) {
        this._zipPathBaseHtmls = value;
    }

    get srcPath() {
        return this._srcPath;
    }

    set srcPath(value) {
        this._srcPath = value;
    }

    get srcPathHtmls() {
        return this._srcPathHtmls;
    }

    set srcPathHtmls(value) {
        this._srcPathHtmls = value;
    }

    get srcPathImages() {
        return this._srcPathImages;
    }


    set srcPathImages(value) {
        this._srcPathImages = value;
    }

    get srcPathStyles() {
        return this._srcPathStyles;
    }

    set srcPathStyles(value) {
        this._srcPathStyles = value;
    }

    get srcPathJavaScripts() {
        return this._srcPathJavaScripts;
    }

    set srcPathJavaScripts(value) {
        this._srcPathJavaScripts = value;
    }

    get srcPathTemplateFile() {
        try {
            fs.accessSync(this._srcPathTemplateFile, fs.F_OK);
        } catch (e) {
            this._srcPathTemplateFile = './src/templates/template.html';
            console.warn("not find custom template: " + e + " and will use default template : " + this._srcPathTemplateFile);
        }
        return this._srcPathTemplateFile;
    }

    set srcPathTemplateFile(value) {
        this._srcPathTemplateFile = value;
    }

    get buildOutputPath() {
        return this._buildOutputPath;
    }

    set buildOutputPath(value) {
        this._buildOutputPath = value;
    }

    get buildOutputPathImages() {
        return this._buildOutputPathImages;
    }

    set buildOutputPathImages(value) {
        this._buildOutputPathImages = value;
    }

    get buildOutputPathJavaScripts() {
        return this._buildOutputPathJavaScripts;
    }

    set buildOutputPathJavaScripts(value) {
        this._buildOutputPathJavaScripts = value;
    }

    get buildOutputPathHtmls() {
        return this._buildOutputPathHtmls;
    }

    set buildOutputPathHtmls(value) {
        this._buildOutputPathHtmls = value;
    }

    get buildOutputPathStyles() {
        return this._buildOutputPathStyles;
    }

    set buildOutputPathStyles(value) {
        this._buildOutputPathStyles = value;
    }

    toString() {
        return "\n==============================>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" +
            "\n当前模块" +
            "\n==============================>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" +
            "\nindexName:\t\t\t\t\t\t" + this.indexName +
            "\nmoduleName:\t\t\t\t\t\t" + this.moduleName +
            "\nport:\t\t\t\t\t\t" + this.port +
            "\nsrcPath:\t\t\t\t\t\t" + this.srcPath +
            "\nsrcPathHtmls:\t\t\t\t\t" + this.srcPathHtmls +
            "\nsrcPathImages:\t\t\t\t\t" + this.srcPathImages +
            "\nsrcPathStyles:\t\t\t\t\t" + this.srcPathStyles +
            "\nsrcPathJavaScripts:\t\t\t\t" + this.srcPathJavaScripts +
            "\nsrcPathTemplateFile:\t\t\t" + this.srcPathTemplateFile +
            "\nbuildOutputPath:\t\t\t\t" + this.buildOutputPath +
            "\nbuildOutputPathImages:\t\t\t" + this.buildOutputPathImages +
            "\nbuildOutputPathJavaScripts:\t\t" + this.buildOutputPathJavaScripts +
            "\nbuildOutputPathHtmls:\t\t\t" + this.buildOutputPathHtmls +
            "\nbuildOutputPathStyles:\t\t\t" + this.buildOutputPathStyles +

            "\nzipSrcPathImages:\t\t\t\t" + this.zipSrcPathImages +
            "\nzipSrcPathJavaScripts:\t\t\t" + this.zipSrcPathJavaScripts +
            "\nzipSrcPathHtmls:\t\t\t\t" + this.zipSrcPathHtmls +
            "\nzipSrcPathStyles:\t\t\t\t" + this.zipSrcPathStyles +
            "\nzipFileNameImages:\t\t\t\t" + this.zipFileNameImages +
            "\nzipFileNameJavaScripts:\t\t\t" + this.zipFileNameJavaScripts +
            "\nzipFileNameHtmls:\t\t\t\t" + this.zipFileNameHtmls +
            "\nzipFileNameStyles:\t\t\t\t" + this.zipFileNameStyles +
            "\nzipPathOutput:\t\t\t\t\t" + this.zipPathOutput +
            "\nzipPathBaseImages:\t\t\t\t" + this.zipPathBaseImages +
            "\nzipPathBaseJavaScripts:\t\t\t" + this.zipPathBaseJavaScripts +
            "\nzipPathBaseStyles:\t\t\t\t" + this.zipPathBaseStyles +
            "\nzipPathBaseHtmls:\t\t\t\t" + this.zipPathBaseHtmls +
            "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<==============================\n";
    }
}

export default Module;
