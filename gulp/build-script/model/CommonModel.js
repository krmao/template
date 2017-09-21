/**
 * 所有的 path 后面都必须包含 '/'
 */
class Common {

    constructor() {
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

    get srcPath() {
        return this._srcPath;
    }

    set srcPath(value) {
        this._srcPath = value;
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

    get srcPathPluginArrays() {
        return this._srcPathPluginArrays;
    }

    set srcPathPluginArrays(value) {
        this._srcPathPluginArrays = value;
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

    get buildOutputPathStyles() {
        return this._buildOutputPathStyles;
    }

    set buildOutputPathStyles(value) {
        this._buildOutputPathStyles = value;
    }

    get buildOutputPathPlugin() {
        return this._buildOutputPathPlugin;
    }

    set buildOutputPathPlugin(value) {
        this._buildOutputPathPlugin = value;
    }

    toString() {
        return "\n==============================>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" +
            "\nCommonModel" +
            "\n==============================>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" +
            "\nsrcPath:\t\t\t\t" + this.srcPath +
            "\nsrcPathImages:\t\t\t\t" + this.srcPathImages +
            "\nsrcPathStyles:\t\t\t\t" + this.srcPathStyles +
            "\nsrcPathJavaScripts:\t\t\t" + this.srcPathJavaScripts +
            "\nsrcPathPluginArrays:\t\t\t" + this.srcPathPluginArrays +
            "\nbuildOutputPath:\t\t\t" + this.buildOutputPath +
            "\nbuildOutputPathImages:\t\t\t" + this.buildOutputPathImages +
            "\nbuildOutputPathJavaScripts:\t\t" + this.buildOutputPathJavaScripts +
            "\nbuildOutputPathStyles:\t\t\t" + this.buildOutputPathStyles +
            "\nbuildOutputPathPlugin:\t\t\t" + this.buildOutputPathPlugin +
            "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<==============================\n";
    }
}

export default Common;
