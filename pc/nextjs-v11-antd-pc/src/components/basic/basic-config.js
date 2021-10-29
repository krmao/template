// noinspection SpellCheckingInspection

import BasicSvgDataStatistic from "@basic-utils/basic-svg/basic-svg-data-statistic";
import MapSvg from "@public/map.svg";
import BasicEnvUtil from "./basic-env";
import {APP_DOMAIN_UAT, APP_DOMAIN_PROD, APP_REQUEST_PATH_PREFIX} from "./basic-constants";

const _APP_MENU_DATA = [
    {
        title: "数据监控",
        icon: <BasicSvgDataStatistic />,
        key: "/monitor",
        titleAlias: "数据监控"
    },
    {
        title: "filmlist",
        icon: <BasicSvgDataStatistic />,
        key: "/filmlist",
        titleAlias: "电影列表"
    },
    {
        title: "filmdetail",
        icon: <BasicSvgDataStatistic />,
        key: "/filmdetail",
        titleAlias: "电影详情"
    },
    {
        title: "数据概览",
        icon: <MapSvg />,
        key: "/data2",
        titleAlias: "数据概览"
    }
];

module.exports = {
    APP_MENU_DATA: _APP_MENU_DATA,
    APP_REQUEST_BASE_URL: () =>
        `${BasicEnvUtil.getApiEnv() !== BasicEnvUtil.PROD ? APP_DOMAIN_UAT : APP_DOMAIN_PROD}${APP_REQUEST_PATH_PREFIX}`
};
