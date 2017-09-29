let ncp = require('ncp').ncp;
ncp.limit = 16;

let EasyZip = require('easy-zip2').EasyZip;
let zip = new EasyZip();
let fs = require('fs');

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
                zip.zipFolder(this.options.input, {rootFolder: this.options.rootFolder}, () => {
                    let tmpPath = this.options.input + '/bundle.zip';

                    if (fs.existsSync(tmpPath))
                        fs.unlinkSync(tmpPath);

                    zip.writeToFile(tmpPath, () => {

                        for (let index in this.options.output) {

                            if (this.options.output.hasOwnProperty(index)) {

                                let tmpOutPut = this.options.output[index];
                                if (fs.existsSync(tmpOutPut))
                                    fs.unlinkSync(tmpOutPut);
                                console.log("[copyToNative] output:", tmpOutPut);
                                ncp(tmpPath, tmpOutPut, (err) => {
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
