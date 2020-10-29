"use strict";

import React, {Component} from "react";
import Animated from "animated/lib/targets/react-dom";
import css from "../../library_business/common/head.scss";

const _sakuraBaseStyleHeight = 33;
const _sakuraBaseStyleWidth = 33;


const WIDTH = 400;
const HEIGHT = 700;
const downSpeedArr = [4500, 4500, 4800, 5000, 6000, 6300, 6500, 6800, 7000, 8000, 8300, 8500, 8800, 9000, 10000];
const scareArr = [0.7, 0.7, 0.9, 0.9, 1.1];

export default class AnimateSakura extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isDestroyComponent: false,
            sakuraRotateVal: new Animated.Value(0),
            sakuraScaleVal: new Animated.Value(1),
            sakuraTranslateVal: new Animated.Value(0) 	//垂直位移时间与水平位移时间必须保证为同一个时间，因为必须在相同时间内完成水平位移和垂直位移
        };
        this.animatedFlag = true;
        this.rotateAnimatedFlag = true;
        this.rotateAnimatedTimer = null;
    }

    componentDidMount() {
        this._sakuraTranslateAnimate();
        this._sakuraRotateAnimate();
    }

    shouldComponentUpdate(nextProps) {
        if (this.props.animated !== nextProps.animated) {
            this.animatedFlag = nextProps.animated;
        }
        return false;
    }

    _sakuraTranslateAnimate() {
        let {downSpeed = downSpeedArr[parseInt(Math.random() * downSpeedArr.length)]} = this.props.sakuraDepData;
        this.state.sakuraTranslateVal.setValue(0);
        Animated.timing(this.state.sakuraTranslateVal, {
            toValue: 1,
            duration: downSpeed,
            isInteraction: false
        }).start(() => {
            // this.setState({ isDestroyComponent: true });
            if (this.animatedFlag) {
                if (this.rotateAnimatedTimer) {
                    clearTimeout(this.rotateAnimatedTimer);
                }
                this.rotateAnimatedTimer = setTimeout(() => {
                    this.rotateAnimatedFlag = false;
                }, 11000);
                this._sakuraTranslateAnimate();
            }
        });
    }

    _sakuraRotateAnimate() {
        this.state.sakuraRotateVal.setValue(0);
        Animated.timing(this.state.sakuraRotateVal, {
            toValue: 1,
            duration: 6000,
            isInteraction: false
        }).start(() => {
            if (this.rotateAnimatedFlag) {
                this._sakuraRotateAnimate();
            }
        });
    }

    render() {
        const pageAvailWidth = WIDTH - _sakuraBaseStyleWidth;
        const pageAvailHeight = HEIGHT - _sakuraBaseStyleHeight;
        let {sakuraRotateVal, sakuraTranslateVal, isDestroyComponent} = this.state;
        let {
            scaleVal = scareArr[parseInt(Math.random() * scareArr.length)],
            PAGE_SAVE_WIDTH = pageAvailWidth,
            PAGE_SAVE_HEIGHT = pageAvailHeight,
            sakuraBaseStyleHeight = _sakuraBaseStyleHeight,
            sakuraBaseStyleWidth = _sakuraBaseStyleWidth,
            sakuraImgArr
        } = this.props.sakuraDepData;

        let sakuraImgArrIdx = parseInt(Math.random() * sakuraImgArr.length);
        let sakuraImgSrc = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1603977863546&di=4303e91556fcd20eb9fb49714c78d2e5&imgtype=0&src=http%3A%2F%2Fbpic.588ku.com%2Felement_pic%2F00%2F02%2F30%2F75568369abc4460.jpg";//sakuraImgArr[sakuraImgArrIdx];
        let sakuraImgType = "SINGLE_SAKURA";
        if (sakuraImgArrIdx === 0) sakuraImgType = "SAKURA";

        let generateTransformRule = (sakuraImgType) => {
            if (sakuraImgType === "SAKURA") { //the whole flower; do 360 rotate;
                return {
                    rotate: sakuraRotateVal.interpolate({
                        inputRange: [0, 1],
                        outputRange: ["0deg", "360deg"]
                    })
                };
            } else {
                return {
                    rotate: getRotateAngle()
                };
            }
        };

        if (!isDestroyComponent) {
            return (
                <Animated.div style={{
                    position: "absolute",
                    zIndex: 1,
                    opacity: sakuraTranslateVal.interpolate({
                        inputRange: [0, 0.7, 1],
                        outputRange: [1, 1, 0]
                    }),
                    transform: [{
                        translateX: sakuraTranslateVal.interpolate({
                            inputRange: [0, 1],
                            outputRange: translateXOutputRange(PAGE_SAVE_WIDTH)
                        })
                    }, {
                        translateY: sakuraTranslateVal.interpolate({
                            inputRange: [0, 1],
                            outputRange: [-60, PAGE_SAVE_HEIGHT + 60]
                        })
                    }]
                }}>
                    <img onClick={() => {console.warn("you clicked");}} className={css.logo} src={sakuraImgSrc} alt={"sakura"} height={sakuraBaseStyleHeight} width={sakuraBaseStyleWidth}/>
                    <Animated.img source={{uri: sakuraImgSrc}}
                                  style={[{
                                      height: sakuraBaseStyleHeight,
                                      width: sakuraBaseStyleWidth,
                                      transform: [
                                          generateTransformRule(sakuraImgType),
                                          {
                                              scale: scaleVal
                                          }, {
                                              perspective: 1000  // without this line this Animation will not render on Android while working fine on iOS
                                          }
                                      ]
                                  }]}/>
                </Animated.div>
            );
        } else {
            return null;
        }
    }
}

function getRandomDate(val) {
    return parseInt(Math.random() * val);
}

function getRotateAngle() { //0~360deg
    return `${parseInt(Math.random() * 360)}deg`;
}

function translateXOutputRange(PAGE_SAVE_WIDTH) {
    let initPostion = getRandomDate(PAGE_SAVE_WIDTH);
    let finallyPostion = getRandomDate(PAGE_SAVE_WIDTH);
    return [initPostion, finallyPostion];
}


/***
 * 依赖参数如下
 * sakuraDepData:{
 * PAGE_SAVE_WIDTH  屏幕可用宽度
 * PAGE_SAVE_HEIGHT 屏幕可用高度
 * downSpeed 樱花下落速度,
 * scaleVal  樱花缩放数值,
 * sakuraBaseStyleHeight 樱花图片高度
 * sakuraBaseStyleWidth 樱花图片宽度
 * sakuraImgSrc 樱花图片src
 * sakuraImgType ["SAKURA花瓣"]
 * }
 *
 * 樱花下落轨迹简介：
 *    樱花生成坐标x轴为随机生成,y轴默认为0;
 *  樱花x轴位移坐标为随机值，y轴坐标为0到屏幕可用高度；
 *  樱花下落速度由外部业务开发同学传入，保证每个樱花下落速度的不同；
 *  樱花图标，缩放都由业务开发同学传入；保证各个樱花大小不一致，以及樱花形状的不一致；
 */
