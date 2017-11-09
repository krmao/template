let ncp = require('ncp').ncp;
ncp.limit = 16;

let EasyZip = require('easy-zip2').EasyZip;
let zip = new EasyZip();
let fs = require('fs');
let path = require('path');
let md5File = require('md5-file')

/**
 * 文件遍历方法
 * @param filePath 需要遍历的文件路径
 */
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
    fs.writeFileSync(filePath, JSON.stringify(json, null, 4));
}

function outputConfig(intput, output) {
    console.log("--------------------------------------------------")
    let temArray = []
    getFileList(intput).forEach(function (filePath) {
        let obj = Object.create(null);
        obj[filePath.replace("/Users/maokangren/workspace/template/apps/app-house-keeper/html/dist/", "")] = md5File.sync(filePath)
        temArray.push(obj)
    })
    writeJsonToFile(output, temArray)
    console.log("--------------------------------------------------")
}

class CopyToNativePlugin {
    constructor(options = {}) {
        this.options = options;
    }

    apply(compiler) {
        compiler.plugin('done', () => {
            console.warn("[copyToNative] start");
            if (compiler.outputFileSystem.constructor.name !== 'NodeOutputFileSystem') {
                return;
            }
            console.log('[copyToNative] check input:', this.options.input, ' output:', this.options.output);

            if (this.options.input && this.options.output) {
                let outputDirPath = this.options.input + '/../build-output';
                let outputFilePath = this.options.input + '/../build-output/bundle.zip';
                outputConfig(this.options.input, outputDirPath + "/config.json")
                zip.zipFolder(this.options.input, {rootFolder: this.options.rootFolder}, () => {

                    if (fs.existsSync(outputFilePath)) {
                        fs.unlinkSync(outputFilePath);
                    }

                    if (!fs.existsSync(outputDirPath)) {
                        fs.mkdir(outputDirPath)
                    }

                    zip.writeToFile(outputFilePath, () => {

                        for (let index in this.options.output) {

                            if (this.options.output.hasOwnProperty(index)) {

                                let tmpOutPut = this.options.output[index];
                                if (fs.existsSync(tmpOutPut))
                                    fs.unlinkSync(tmpOutPut);
                                console.log("[copyToNative] output:", tmpOutPut);
                                ncp(outputFilePath, tmpOutPut, {stopOnErr: true}, (err) => {
                                    if (err) {
                                        return console.error('[copyToNative] error! ', err);
                                    }
                                    console.warn("[copyToNative] copy complete !");
                                });
                            }
                        }
                    });
                });
            } else {
                console.warn("[copyToNative] cope nothing !");
            }
        });
    }
}

module.exports = CopyToNativePlugin;
