/**
 * 模块打包插件 for chexiang.com
 *
 * 功能:
 * <p>
 *    1: 自动压缩 dist 目录 到 dist/../build-output/module-name.zip
 *    2: 支持对module-name 自动拷贝到其它目录, 配置 output 数组即可
 *    3: 自动遍历dist 下左右文件, 生成配置文件(包含各文件的 md5 值, 以及其它配置信息)
 * </p>
 *
 *
 * 依赖:
 * <p>
 *    npm install -D fs-extra easy-zip2 path md5-file
 * </p>
 *
 * 案例:
 * <p>
 *    new ModuleConfigPlugin({
 *         rootFolder: '',
 *         baseInfo: {
 *           moduleVersion: 1,
 *           moduleName: "base",
 *           moduleSchemeUrls: {
 *             "prd": "h.jia.com",
 *             "pre": "h.jia.com",
 *             "sit": "h.jia.com"
 *           },
 *           moduleDownloadUrl: "http://www.cheixang.com/download",
 *           moduleUpdateStrategy: 0,
 *           moduleRoutesUpdateStrategy: {
 *             "/base/route1": 1,
 *             "/base/route2": 0,
 *             "/base/route3": 1,
 *           },
 *         },
 *         input: config.build.assetsRoot,
 *         output: [
 *           // path.resolve(config.build.assetsRoot, '../../android/arsenal/modules/module-housekeeper-hybird/src/main/assets/bundle.zip'),
 *           // path.resolve(config.build.assetsRoot, '../../ios/arsenal/modules/module-housekeeper-hybird/assets/bundle.zip')
 *         ]
 *       })
 * </p>
 *
 */

// let ncp = require('ncp').ncp
// ncp.limit = 16
// let fs = require('fs')
let fs = require('fs-extra');
let EasyZip = require('easy-zip2').EasyZip;
let md5File = require('md5-file');
let TAG = '[module-config]';
let path = require('path');
let zip = new EasyZip();

function getFileList(filePath) {
    let fileList = [];
    fs.readdirSync(filePath).forEach(function (filename) {
        let tmpPath = path.join(filePath, filename);
        let stats = fs.statSync(tmpPath);
        if (stats.isFile()) {
            fileList.push(tmpPath)
        } else if (stats.isDirectory()) {
            fileList = fileList.concat(getFileList(tmpPath))
        }
    });
    return fileList
}

function writeJsonToFile(filePath, json) {
    fs.writeFileSync(filePath, JSON.stringify(json, null, 4))
}

function outputConfig(intput, output, data) {
    let obj = Object.create(null);
    getFileList(intput).forEach(function (filePath) {

        let tmpPathList = filePath.split("dist");
        if (tmpPathList.length >= 2) {
            obj[tmpPathList[1]] = md5File.sync(filePath);
        } else {
            console.error(TAG, filePath, "can not split by dist !")
        }
    });
    data['moduleFilesMd5'] = obj;

    writeJsonToFile(output, data);
    console.warn(TAG, 'json -> ' + output)
}

class CopyToNativePlugin {
    constructor(options = {}) {
        this.options = options
    }

    apply(compiler) {
        compiler.plugin('done', () => {
            console.warn(TAG, '========================================>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>');
            if (compiler.outputFileSystem.constructor.name !== 'NodeOutputFileSystem') return;
            console.warn(TAG, 'check input:', this.options.input, ' output:', this.options.output);

            if (this.options.baseInfo === null) this.options.baseInfo = Object.create(null);
            if (this.options.baseInfo.moduleName === null) this.options.baseInfo.moduleName = 'bundle';
            if (this.options.rootFolder === null) this.options.rootFolder = '';

            if (this.options.input && this.options.baseInfo) {

                let jsonFileName = this.options.baseInfo.moduleName + '.json';
                let zipFileName = this.options.baseInfo.moduleName + '-' + this.options.baseInfo.moduleVersion + '.zip';

                let outputDirPath = this.options.input + '/../build-output';
                let outputFilePath = this.options.input + '/../build-output/' + zipFileName;

                zip.zipFolder(this.options.input, {rootFolder: this.options.rootFolder}, () => {
                    if (fs.existsSync(outputFilePath)) fs.unlinkSync(outputFilePath);
                    if (!fs.existsSync(outputDirPath)) fs.mkdir(outputDirPath);

                    zip.writeToFileSync(outputFilePath);

                    let zipMd5 = md5File.sync(outputFilePath);
                    console.warn(TAG, 'zip  to ' + outputFilePath, 'success -> md5:', zipMd5);
                    this.options.baseInfo['moduleZipMd5'] = zipMd5;

                    let jsonFilePath = outputDirPath + '/' + jsonFileName;
                    outputConfig(this.options.input, jsonFilePath, this.options.baseInfo);

                    for (let index in this.options.output) {
                        if (this.options.output.hasOwnProperty(index)) {
                            let tmpOutPut = this.options.output[index];

                            try {
                                let tmpOutPutJsonFile = tmpOutPut + '/' + jsonFileName;
                                if (fs.existsSync(tmpOutPutJsonFile)) fs.unlinkSync(tmpOutPutJsonFile);
                                fs.copySync(jsonFilePath, tmpOutPutJsonFile);
                                console.warn(TAG, 'copy to ' + tmpOutPut, 'success -> md5:', md5File.sync(tmpOutPutJsonFile))
                            } catch (err) {
                                console.error(tag, 'copy to ' + tmpOutPut + ' error', err);
                                return
                            }
                            try {
                                let tmpOutPutZipFile = tmpOutPut + '/' + zipFileName;
                                if (fs.existsSync(tmpOutPutZipFile)) fs.unlinkSync(tmpOutPutZipFile);
                                fs.copySync(outputFilePath, tmpOutPutZipFile);
                                console.warn(TAG, 'copy to ' + tmpOutPut, 'success -> md5:', md5File.sync(tmpOutPutZipFile))
                            } catch (err) {
                                console.error(tag, 'copy to ' + tmpOutPut + ' error', err);
                                return
                            }
                            /*ncp(outputFilePath, tmpOutPut, {stopOnErr: true}, (err) => {
                             if (err) return console.error(tag, 'copy to ' + tmpOutPut + ' error', err)
                             console.warn(TAG, 'copy to ' + tmpOutPut, 'success -> md5:', md5File.sync(tmpOutPut))
                             })*/
                        }
                    }
                    console.warn(TAG, '<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<========================================')
                })
            } else {
                console.warn(TAG, 'cope nothing !')
            }
        })
    }
}

module.exports = CopyToNativePlugin;
