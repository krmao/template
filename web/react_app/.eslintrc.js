module.exports = {
    "env": {
        "browser": true,
        "es6": true
    },
    "extends": "eslint:recommended",
    "globals": {
        "Atomics": "readonly",
        "SharedArrayBuffer": "readonly"
    },
    "parserOptions": {
        "ecmaFeatures": {
            "jsx": true
        },
        "ecmaVersion": 2018,
        "sourceType": "module"
    },
    "plugins": [
        "react"
    ],
    "rules": {// 0=off, 1==warn, 2==error
        "indent": [
            "error",
            4
        ],
        "eqeqeq": ["error", "smart"], // 全等于
        "linebreak-style": [
            "error",
            "unix"
        ],
        "quotes": [
            "error",
            "double"
        ],
        "semi": [
            "error",
            "always"
        ],
        "no-trailing-spaces": 1, //一行结束后面有空格就发出警告
        "no-undef": 0,
        "no-var": 2, //对var警告
        "no-unused-vars": ["off", {"vars": "all", "args": "after-used"}],
        "no-underscore-dangle": 2, //标识符不能以_开头或结尾
        "no-const-assign": 2, //禁止修改const声明的变量
        "no-use-before-define": 0, //未定义前不能使用
        "camelcase": 1, //强制驼峰法命名
        "no-dupe-keys": 2, //在创建对象字面量时不允许键重复
        "no-duplicate-case": 2, //switch中的case标签不能重复
        "no-dupe-args": 2, //函数参数不能重复
        "no-redeclare": 2, //禁止重复声明变量
        "use-isnan": 2,//禁止比较时使用NaN，只能用isNaN()
        "no-mixed-spaces-and-tabs": 2, //禁止混用tab和空格
        "jsx-quotes": [2, "prefer-double"], //强制在JSX属性（jsx-quotes）中一致使用双引号
    },
    "settings": {
        "import/ignore": [
            "node_modules"
        ]
    }
};
