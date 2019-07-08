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
    "plugins": ["react"],
    "rules": { // off:0, warn:1, error:2
        "camelcase": "warn", // 强制驼峰法命名
        "use-isnan": "error", // 只能使用 isNaN() 进行比较
        "indent": ["error", 4], // 缩进强制使用 4 个空格
        "semi": ["error", "always"], // 强制使用双引号
        "eqeqeq": ["error", "smart"], // 全等于, 使用智能模式 0==1 不报错
        "quotes": ["error", "double"],
        "linebreak-style": ["error", "unix"],
        "no-var": "error", // 对var警告
        "no-undef": "off", // 对检测到使用 未定义的变量 报错
        "no-console": "off", // 对检测到使用 console 打印日志 报错
        "no-dupe-keys": "error", // 在创建对象字面量时不允许键重复
        "no-dupe-args": "error", // 函数参数不能重复
        "no-redeclare": "error", // 禁止重复声明变量
        "no-const-assign": "error", // 禁止修改const声明的变量
        "no-useless-concat": "off", // 没有必要将两个字符串连接在一起
        "no-duplicate-case": "error", // switch中的case标签不能重复
        "no-trailing-spaces": "warn", // 一行结束后面有空格就发出警告
        "no-underscore-dangle": "error", // 标识符不能以_开头或结尾
        "no-use-before-define": "off", // 未定义前不能使用
        "no-mixed-spaces-and-tabs": "error", // 禁止混用tab和空格
        "no-unused-vars": ["off", {"vars": "all", "args": "after-used"}], // 检测到未被使用的变量
        "jsx-quotes": ["error", "prefer-double"], // 强制在JSX属性（jsx-quotes）中一致使用双引号
    },
    "settings": {
        "import/ignore": ["node_modules"]
    }
};
