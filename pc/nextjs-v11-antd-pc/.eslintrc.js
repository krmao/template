module.exports = {
    env: {
        browser: true,
        node: true,
        es6: true
    },
    extends: [
        "eslint:recommended",
        "plugin:react/recommended",
        "plugin:jsx-a11y/recommended",
        "next",
        "next/core-web-vitals",
        "prettier"
    ],
    globals: {
        Atomics: "readonly",
        SharedArrayBuffer: "readonly"
    },
    parser: "babel-eslint",
    parserOptions: {
        ecmaFeatures: {
            jsx: true
        },
        ecmaVersion: 2018,
        sourceType: "module"
    },
    plugins: ["react", "jsx-a11y", "import", "react-hooks"],
    rules: {
        // off:0, warn:1, error:2
        "no-extra-boolean-cast": "off",
        "react/prop-types": "off",
        "react/display-name": "off",
        "no-spaced-func": "error", //函数调用时 函数名与()之间不能有空格
        // "space-before-blocks": "error", //强制在块之前使用一致的空格，这里先注释掉，windows和mac不一致
        "space-infix-ops": "off", //要求中缀操作符周围有空格
        "array-element-newline": ["error", "consistent"], //数组元素换行
        "keyword-spacing": ["error", {before: true}], //强制关键字周围空格的一致性
        "arrow-spacing": ["error", {before: true, after: true}], //=>的前/后括号
        "no-multiple-empty-lines": ["error", {max: 2}], //空行最多不能超过2行
        "no-undef": "warn", // 禁用未声明的变量
        "no-unused-vars": ["error", {args: "none"}], //禁止未使用过的变量
        "camelcase": "warn", // 强制驼峰法命名
        "use-isnan": "error", // 只能使用 isNaN() 进行比较
        // "indent": ["error", 4], // 缩进强制使用 4 个空格
        "semi": ["error", "always"], // 强制使用双引号
        "eqeqeq": ["error", "smart"], // 全等于, 使用智能模式 0==1 不报错
        "quotes": ["error", "double"],
        "linebreak-style": "off",
        "no-var": "error", // 对var警告
        "no-console": "off", // 对检测到使用 console 打印日志 报错
        "no-dupe-keys": "error", // 在创建对象字面量时不允许键重复
        "no-dupe-args": "error", // 函数参数不能重复
        "no-redeclare": "error", // 禁止重复声明变量
        "no-const-assign": "error", // 禁止修改const声明的变量
        "no-useless-concat": "off", // 没有必要将两个字符串连接在一起
        "no-duplicate-case": "error", // switch中的case标签不能重复
        "no-trailing-spaces": "warn", // 一行结束后面有空格就发出警告
        "no-underscore-dangle": "off", // 标识符不能以_开头或结尾
        "no-use-before-define": "off", // 未定义前不能使用
        "no-mixed-spaces-and-tabs": ["error", "smart-tabs"], // 禁止混用tab和空格
        "comma-dangle": ["off", "never"], // 定义数组或对象最后多余的逗号
        "jsx-quotes": ["error", "prefer-double"], // 强制在JSX属性（jsx-quotes）中一致使用双引号
        "react/jsx-closing-bracket-location": ["off", "line-aligned"], // 在JSX中验证右括号位置
        "react/jsx-props-no-multi-spaces": ["error"], // 属性只能有一个空格
        "react-hooks/rules-of-hooks": "error", // 检查 Hook 的规则
        "react-hooks/exhaustive-deps": "warn", // 检查 effect 的依赖
        "jsx-a11y/click-events-have-key-events": "off",
        "jsx-a11y/no-noninteractive-element-interactions": "off",
        "jsx-a11y/no-autofocus": "off",
        "jsx-a11y/anchor-is-valid": "off",
        "jsx-a11y/no-static-element-interactions": "off"
    },
    settings: {
        "import/ignore": ["node_modules"],
        "react": {
            version: "detect" // 设置兼容的版本自适应而非指定版本
        }
    }
};
