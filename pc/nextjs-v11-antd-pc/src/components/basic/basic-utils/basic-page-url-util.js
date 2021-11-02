/**
 * 所有页面 URL 统一放在这里定义
 */
import BasicValueUtil from "./basic-value-util";

export default class BasicPageUrlUtil {

    /**
     * 查询 url 参数
     *
     * @param queryName 待查的 url 参数名称
     * @param url pageUrl or null
     * @return string or null
     *
     * @example PageUrl.getQueryString("searchType", props.asPath)
     */
    static getQueryString = (queryName, url) => {
        let matcher = new RegExp("(^|&)" + queryName + "=([^&]*)(&|$)");
        let href = url || window.location.href;
        let array = href.substr(href.indexOf("?") + 1).match(matcher);
        if (array != null) {
            return decodeURIComponent(array[2]).split("#")[0];
        }
        return null;
    };

    /**
     * 修改当前页面 url 的参数, 如果目标 value 无效则不再添加到 url 中
     * 不会刷新当前页面, 仅仅是改变 url
     *
     * @param paramKey 必须要有效非空
     * @param paramValue 为空或者无效或者空字符串会被删除, 有效非空值会替换/追加
     */
    static changeHrefParams = (paramKey, paramValue) => {
        console.log("changeHrefParams start", window.location.href);
        if (BasicValueUtil.isStringBlank(paramKey)) return;
        let encodedParamValue = encodeURIComponent(paramValue);

        try {
            let hrefArray = window.location.href.split("?");
            let paramsString;
            if (hrefArray.length === 1 || !hrefArray[1]) {
                paramsString = paramKey + "=" + encodedParamValue;
            } else {
                let paramsArray = hrefArray[1].split("&");
                let oldIndex = -1;
                paramsArray.some((param, index) => {
                    if (param.startsWith(paramKey)) {
                        oldIndex = index;
                        return true;
                    }
                });
                if (oldIndex > -1) paramsArray.splice(oldIndex, 1);
                if (!BasicValueUtil.isStringBlank(encodedParamValue)) paramsArray.push(paramKey + "=" + encodedParamValue);
                paramsString = paramsArray.join("&");
            }
            console.log("changeHrefParams paramsString", paramsString);

            // 使用 history 不能使用 this.goto 方法, 只能使用 window.location.href, 这样返回时 100%刷新
            window.history.replaceState(null, null, `?${paramsString}`);

            // 使用 router.replace, 可以使用 this.goto 方法, 返回时概率刷新, 线上环境不生效, 会多一个历史记录且刷新
            /*let relativePathWithVd = Constants.CONFIG_VD + hrefArray[0].split(Constants.CONFIG_VD)[1];
            router.replace(relativePathWithVd, `${relativePathWithVd}?${paramsString}`, {
                shallow: true,
                scroll: false
            });*/
            console.log("changeHrefParams end", window.location.href);
        } catch (e) {
            console.log("changeHrefParams error", e);
        }
    };
}
