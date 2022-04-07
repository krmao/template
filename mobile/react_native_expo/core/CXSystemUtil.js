import {Platform, StatusBar} from "react-native";

var Dimensions = require('Dimensions');

export default class CXSystemUtil {
    constructor() {
    }
}

CXSystemUtil.IPHONE_X_WIDTH = 375;
CXSystemUtil.IPHONE_X_HEIGHT = 812;

CXSystemUtil.IS_IPHONE_X = Platform.OS === 'ios' &&
    ((CXSystemUtil.SCREEN_HEIGHT === CXSystemUtil.IPHONE_X_HEIGHT && CXSystemUtil.SCREEN_WIDTH === CXSystemUtil.IPHONE_X_WIDTH)
    || (CXSystemUtil.SCREEN_HEIGHT === CXSystemUtil.IPHONE_X_WIDTH && CXSystemUtil.SCREEN_WIDTH === CXSystemUtil.IPHONE_X_HEIGHT));

CXSystemUtil.IS_IOS = Platform.OS === 'ios';
CXSystemUtil.IS_ANDROID = Platform.OS === 'android';
CXSystemUtil.STATES_BAR_HEIGHT = CXSystemUtil.IS_ANDROID ? StatusBar.currentHeight : (CXSystemUtil.IS_IPHONE_X ? 44 : 20);
CXSystemUtil.SCREEN_WIDTH = Dimensions.get('window').width;
CXSystemUtil.SCREEN_HEIGHT = Dimensions.get('window').height;
CXSystemUtil.SCREEN_SCALE = Dimensions.get('window').scale;
