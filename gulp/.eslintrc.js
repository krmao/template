module.exports = {
    "env": {
        "browser": true,
        "es6": false,
        "jquery": true
    },
    //"extends": "eslint:recommended", //never used
    "rules": {
        "no-console": 0,
        "indent": ["error", 4, {"SwitchCase": 1}],
        "quotes": ["error", "single"],
        "semi": ["error", "always"],
        "eqeqeq": [2, "allow-null"],                                                    // 使用 === 替代 ==
        "key-spacing": ["error", {"beforeColon": false, "afterColon": true}],           // 键和值前保留一个空格
        "no-trailing-spaces": "error"                                                   // 不允许在语句后存在多余的空格
    }
};