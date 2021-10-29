// noinspection JSUnusedGlobalSymbols

module.exports = {
    isValid: function (input) {
        return input !== undefined && typeof input !== undefined && input !== null;
    },
    isEmpty: function (input) {
        return !this.isValid(input) || input === "";
    },
    isNotEmpty: function (input) {
        return !this.isEmpty(input);
    },
    isBlank: function (input) {
        return !this.isValid(input) || /^\s*$/.test(input);
    },
    isNotBlank: function (input) {
        return !this.isBlank(input);
    },
    trim: function (input) {
        return !this.isValid(input) ? "" : input.replace(/^\s+|\s+$/gm, "");
    },
    startsWith: function (input, prefix) {
        return input.indexOf(prefix) === 0;
    },
    endsWith: function (input, suffix) {
        return input.lastIndexOf(suffix) === 0;
    },
    contains: function (input, searchSeq) {
        return input.indexOf(searchSeq) >= 0;
    },
    equals: function (input1, input2) {
        return input1 === input2;
    },
    equalsIgnoreCase: function (input1, input2) {
        return input1.toLocaleLowerCase() === input2.toLocaleLowerCase();
    },
    //生成指定个数的字符
    repeat: function (ch, repeatTimes) {
        let result = "";
        for (let i = 0; i < repeatTimes; i++) {
            result += ch;
        }
        return result;
    },
    //大写转小写，小写转大写
    swapCase: function (input) {
        return input.replace(/[a-z]/gi, function (matchStr) {
            if (matchStr >= "A" && matchStr <= "Z") {
                return matchStr.toLocaleLowerCase();
            } else if (matchStr >= "a" && matchStr <= "z") {
                return matchStr.toLocaleUpperCase();
            }
        });
    },
    //只包含数字和空格
    isNumericSpace: function (input) {
        return /^[\d\s]*$/.test(input);
    },
    //数字
    isNumeric: function (input) {
        return /^(?:[1-9]\d*|0)(?:\.\d+)?$/.test(input);
    },
    /**
     * https://stackoverflow.com/a/41458529
     *
     *  true:
     *      isNumeric("1"))
     *      isNumeric(1e10))
     *      isNumeric(1E10))
     *      isNumeric(+"6e4"))
     *      isNumeric("1.2222"))
     *      isNumeric("-1.2222"))
     *      isNumeric("-1.222200000000000000"))
     *      isNumeric("1.222200000000000000"))
     *      isNumeric(1))
     *      isNumeric(0))
     *      isNumeric(-0))
     *      isNumeric(1010010293029))
     *      isNumeric(1.100393830000))
     *      isNumeric(Math.LN2))
     *      isNumeric(Math.PI))
     *      isNumeric(5e10))
     *
     *  false:
     *      isNumeric(NaN))
     *      isNumeric(Infinity))
     *      isNumeric(-Infinity))
     *      isNumeric())
     *      isNumeric(undefined))
     *      isNumeric('[1,2,3]'))
     *      isNumeric({a:1,b:2}))
     *      isNumeric(null))
     *      isNumeric([1]))
     *      isNumeric(new Date()))
     *      isNumeric(new Number(1))
     */
    isNumeric2: function (value) {
        let _value = +value;
        return (
            value !== value + 1 && // infinity check
            _value === +value && // cute coercion check
            typeof value !== "object" && // array/object check
            value.replace(/\s/g, "") !== "" && // empty
            value.slice(-1) !== "." // decimal without number
        );
    },
    //小数
    isDecimal: function (input) {
        return /^[-+]?(?:0|[1-9]\d*)\.\d+$/.test(input);
    },
    //整数
    isInteger: function (input) {
        return /^[-+]?(?:0|[1-9]\d*)$/.test(input);
    },
    isAllLowerCase: function (input) {
        return /^[a-z]+$/.test(input);
    },
    isAllUpperCase: function (input) {
        return /^[A-Z]+$/.test(input);
    },
    //字符串反转
    reverse: function (input) {
        if (this.isBlank(input)) {
            return input;
        }
        return input.split("").reverse().join("");
    },
    //中文校验
    isChinese: function (input) {
        return /^[\u4E00-\u9FA5]+$/.test(input);
    }
};
