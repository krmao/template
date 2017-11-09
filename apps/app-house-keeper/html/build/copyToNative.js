let ncp = require('ncp').ncp
ncp.limit = 16;

let EasyZip = require('easy-zip2').EasyZip
let zip = new EasyZip()
let fs = require('fs')
let path = require('path')
let md5File = require('md5-file')
let TAG = "[module-config]"

function getFileList(filePath) {
    let fileList = []
    fs.readdirSync(filePath).forEach(function (filename) {
        var tmpPath = path.join(filePath, filename)
        let stats = fs.statSync(tmpPath)
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
    let temArray = []
    getFileList(intput).forEach(function (filePath) {
        let obj = Object.create(null);
        obj[filePath.replace("/Users/maokangren/workspace/template/apps/app-house-keeper/html/dist/", "")] = md5File.sync(filePath)
        temArray.push(obj)
    })
    data["moduleZipMd5"] = "xxxxxx"
    data["moduleFilesMd5"] = temArray

    writeJsonToFile(output, data)
    console.warn(TAG, "json -> " + output)
}

class CopyToNativePlugin {
    constructor(options = {}) {
        this.options = options
    }

    apply(compiler) {
        compiler.plugin('done', () => {
            console.warn(TAG, "start")
            if (compiler.outputFileSystem.constructor.name !== 'NodeOutputFileSystem') return
            console.warn(TAG, 'check input:', this.options.input, ' output:', this.options.output)

            if (this.options.baseInfo === null) this.options.baseInfo = Object.create(null)
            if (this.options.baseInfo.moduleName === null) this.options.baseInfo.moduleName = "bundle"

            if (this.options.input && this.options.baseInfo) {
                let outputDirPath = this.options.input + '/../build-output';
                let outputFilePath = this.options.input + '/../build-output/module-' + this.options.baseInfo.moduleName + '.zip'
                outputConfig(this.options.input, outputDirPath + "/module-" + this.options.baseInfo.moduleName + ".json", this.options.baseInfo)
                zip.zipFolder(this.options.input, {rootFolder: this.options.rootFolder}, () => {
                    if (fs.existsSync(outputFilePath)) fs.unlinkSync(outputFilePath);
                    if (!fs.existsSync(outputDirPath)) fs.mkdir(outputDirPath)
                    zip.writeToFile(outputFilePath, () => {
                        for (let index in this.options.output) {
                            if (this.options.output.hasOwnProperty(index)) {
                                let tmpOutPut = this.options.output[index];
                                if (fs.existsSync(tmpOutPut)) fs.unlinkSync(tmpOutPut)
                                console.warn(TAG, "output:", tmpOutPut)
                                ncp(outputFilePath, tmpOutPut, {stopOnErr: true}, (err) => {
                                    if (err) return console.error('[copyToNative] error! ', err)
                                    console.warn(TAG, "copy complete !")
                                });
                            }
                        }
                    })
                })
            } else {
                console.warn(TAG, "cope nothing !")
            }
        })
    }
}

module.exports = CopyToNativePlugin;
