// noinspection JSUnusedGlobalSymbols

export default class BasicValueUtil {
    static isValid = (input) => {
        return !!input && typeof input !== undefined;
    };

    static isValidLatLng = (latitude, longitude) => {
        if (!BasicValueUtil.isValid(latitude) || !BasicValueUtil.isValid(longitude)) {
            return false;
        }
        if ((latitude === -1.0 && longitude === -1.0) || (latitude === 0 && longitude === 0)) {
            return false;
        }
        return !(latitude > 90.0 || latitude < -90.0 || longitude > 180.0 || longitude < -180.0);
    };
}
