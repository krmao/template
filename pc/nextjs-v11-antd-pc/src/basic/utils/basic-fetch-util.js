// noinspection JSValidateTypes,JSUnusedGlobalSymbols

export default class BasicFetchUtil {
    static fetchByRelativeUrl = (url, param) => {
        return BasicFetchUtil.fetchByFullUrl("xxx" + url, param);
    };

    static fetchByFullUrl = (fullUrl, param, extension = []) => {
        console.log("--[request](start)", fullUrl, param);
        return new Promise((resolve, reject) => {
            let body = {
                body: param,
                extension: extension
            };
            new CModel()
                .fetch(fullUrl, body)
                .then((result) => {
                    console.log("--[response](success)", fullUrl, result);
                    resolve(result);
                })
                .catch((error) => {
                    console.log("--[response](failure)", fullUrl, error);
                    reject(error);
                });
        });
    };

    static fetchOnlyForCrossOrigin = (fullUrl, param) => {
        console.log("--[request](start)", fullUrl, param);
        return new Promise((resolve, reject) => {
            // noinspection JSCheckFunctionSignatures
            fetch(
                fullUrl,
                {
                    method: "post",
                    body: JSON.stringify(param),
                    headers: {
                        "Content-Type": "application/json;charset=UTF-8"
                    }
                },
                {credentials: "include"}
            )
                .then((response) => {
                    let result = response.json();
                    console.log("--[response](success)", fullUrl, result);
                    if (response && response.ok && response.status === 200) {
                        resolve(result);
                    } else {
                        reject("error !ok or status!===200");
                    }
                })
                .catch((error) => {
                    console.log("--[response](failure)", fullUrl, error);
                    reject(error);
                });
        });
    };
}
