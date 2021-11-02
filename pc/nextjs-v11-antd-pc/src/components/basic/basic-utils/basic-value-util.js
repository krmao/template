// noinspection JSUnusedGlobalSymbols

export default class BasicValueUtil {
    static isValid = (input) => !!input && typeof input !== undefined;
    static isValidLatLng = (latitude, longitude) => {
        if (!BasicValueUtil.isValid(latitude) || !BasicValueUtil.isValid(longitude)) {
            return false;
        }
        if ((latitude === -1.0 && longitude === -1.0) || (latitude === 0 && longitude === 0)) {
            return false;
        }
        return !(latitude > 90.0 || latitude < -90.0 || longitude > 180.0 || longitude < -180.0);
    };

    static isStringEmpty = (input) => !BasicValueUtil.isValid(input) || input === "";
    static isStringNotEmpty = (input) => !BasicValueUtil.isEmpty(input);
    static isStringBlank = (input) => !BasicValueUtil.isValid(input) || /^\s*$/.test(input);
    static isStringNotBlank = (input) => !BasicValueUtil.isBlank(input);
    static stringTrim = (input) => (!BasicValueUtil.isValid(input) ? "" : input.replace(/^\s+|\s+$/gm, ""));
    static stringStartsWith = (input, prefix) => input.indexOf(prefix) === 0;
    static stringEndsWith = (input, suffix) => input.lastIndexOf(suffix) === 0;
    static stringContains = (input, searchSeq) => input.indexOf(searchSeq) >= 0;
    static stringEquals = (input1, input2) => input1 === input2;
    static stringEqualsIgnoreCaseString = (input1, input2) => input1.toLocaleLowerCase() === input2.toLocaleLowerCase();

    static isStringDecimal = (input) => /^[-+]?(?:0|[1-9]\d*)\.\d+$/.test(input);
    static isStringAllInteger = (input) => /^[-+]?(?:0|[1-9]\d*)$/.test(input);
    static isStringAllLowerCase = (input) => /^[a-z]+$/.test(input);
    static isStringAllUpperCase = (input) => /^[A-Z]+$/.test(input);
    static isStringAllChinese = (input) => /^[\u4E00-\u9FA5]+$/.test(input);
    static stringReverse = (input) =>
        BasicValueUtil.isStringBlank(input) ? input : input.split("").reverse().join("");
    static stringSwapCase = (input) =>
        input.replace(/[a-z]/gi, function (matchStr) {
            if (matchStr >= "A" && matchStr <= "Z") {
                return matchStr.toLocaleLowerCase();
            } else if (matchStr >= "a" && matchStr <= "z") {
                return matchStr.toLocaleUpperCase();
            }
        });
    /**
     * https://stackoverflow.com/a/41458529
     *
     *  true:
     *      isStringNumeric("1"))
     *      isStringNumeric(1e10))
     *      isStringNumeric(1E10))
     *      isStringNumeric(+"6e4"))
     *      isStringNumeric("1.2222"))
     *      isStringNumeric("-1.2222"))
     *      isStringNumeric("-1.222200000000000000"))
     *      isStringNumeric("1.222200000000000000"))
     *      isStringNumeric(1))
     *      isStringNumeric(0))
     *      isStringNumeric(-0))
     *      isStringNumeric(1010010293029))
     *      isStringNumeric(1.100393830000))
     *      isStringNumeric(Math.LN2))
     *      isStringNumeric(Math.PI))
     *      isStringNumeric(5e10))
     *
     *  false:
     *      isStringNumeric(NaN))
     *      isStringNumeric(Infinity))
     *      isStringNumeric(-Infinity))
     *      isStringNumeric())
     *      isStringNumeric(undefined))
     *      isStringNumeric('[1,2,3]'))
     *      isStringNumeric({a:1,b:2}))
     *      isStringNumeric(null))
     *      isStringNumeric([1]))
     *      isStringNumeric(new Date()))
     *      isStringNumeric(new Number(1))
     */
    static isStringNumeric = function (value) {
        let _value = +value;
        return (
            value !== value + 1 && // infinity check
            _value === +value && // cute coercion check
            typeof value !== "object" && // array/object check
            value.replace(/\s/g, "") !== "" && // empty
            value.slice(-1) !== "." // decimal without number
        );
    };
    static isStringNumericSpace = (input) => /^[\d\s]*$/.test(input);
}
