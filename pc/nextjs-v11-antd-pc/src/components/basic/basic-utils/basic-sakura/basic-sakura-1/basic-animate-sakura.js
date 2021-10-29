"use strict";

import React, {Component} from "react";
import Animated from "animated/lib/targets/react-dom";

const DEFAULT_SAKURA_IMAGE_HEIGHT = 33;
const DEFAULT_SAKURA_IMAGE_WIDTH = 33;

const SCREEN_WIDTH = 400;
const SCREEN_HEIGHT = 700;

const sakuraDowningSpeedArray = [4500, 4500, 4800, 5000, 6000, 6300, 6500, 6800, 7000, 8000, 8300, 8500, 8800, 9000, 10000];
const sakuraScaleArray = [0.7, 0.7, 0.9, 0.9, 1.1];

/***
 * 依赖参数如下
 * data:{
 *     availableWidth       屏幕可用宽度, 樱花单个水平位移范围
 *     availableHeight      屏幕可用高度, 樱花单个垂直位移范围
 *     downSpeed            樱花单个下落速度,
 *     sakuraScaleValue     樱花单个缩放数值,
 *     sakuraImageHeight    樱花单个图片高度
 *     sakuraImageWidth     樱花单个图片宽度
 *     sakuraImageArray     樱花随机的图片地址数组
 * }
 */
export default class BasicAnimateSakura extends Component {

    constructor(props) {
        super(props);
        this.state = {
            sakuraScaleValue: new Animated.Value(1),
            sakuraRotateValue: new Animated.Value(0),
            sakuraTranslateValue: new Animated.Value(0) 	//垂直位移时间与水平位移时间必须保证为同一个时间，因为必须在相同时间内完成水平位移和垂直位移
        };
        this.animatedEnabled = true;
        this.rotateAnimatedTimer = null;
        this.rotateAnimatedEnabled = true;
    }

    componentDidMount() {
        this._sakuraTranslateAnimate();
        this._sakuraRotateAnimate();
    }

    shouldComponentUpdate(nextProps) {
        if (this.props.animated !== nextProps.animated) {
            this.animatedEnabled = nextProps.animated;
        }
        return false;
    }

    /**
     * 樱花下落动画
     * @private
     */
    _sakuraTranslateAnimate() {
        let {
            sakuraDowningSpeed = sakuraDowningSpeedArray[parseInt(String(Math.random() * sakuraDowningSpeedArray.length))]
        } = this.props.data;

        this.state.sakuraTranslateValue.setValue(0);
        Animated.timing(this.state.sakuraTranslateValue, {
            toValue: 1,
            duration: sakuraDowningSpeed,
            isInteraction: false
        }).start(() => {
            if (this.animatedEnabled) {
                if (this.rotateAnimatedTimer) {
                    clearTimeout(this.rotateAnimatedTimer);
                }
                this.rotateAnimatedTimer = setTimeout(() => {
                    this.rotateAnimatedEnabled = false;
                }, 11000);
                this._sakuraTranslateAnimate();
            }
        });
    }

    /**
     * 樱花旋转动画
     * @private
     */
    _sakuraRotateAnimate() {
        this.state.sakuraRotateValue.setValue(0);
        Animated.timing(this.state.sakuraRotateValue, {
            toValue: 1,
            duration: 6000,
            isInteraction: false
        }).start(() => {
            if (this.rotateAnimatedEnabled) {
                this._sakuraRotateAnimate();
            }
        });
    }

    /**
     * 樱花有旋转动画, 花瓣没有旋转动画
     * @private
     */
    _sakuraRotateTransform(sakuraImageType) {
        if (sakuraImageType === "SAKURA") { //the whole flower; do 360 rotate;
            return {
                rotate: this.state.sakuraRotateValue.interpolate({
                    inputRange: [0, 1],
                    outputRange: ["0deg", "360deg"]
                })
            };
        } else {
            return {
                rotate: `${parseInt(String(Math.random() * 360))}deg` // 0~360deg
            };
        }
    }

    render() {
        let {
            sakuraScaleValue = sakuraScaleArray[parseInt(String(Math.random() * sakuraScaleArray.length))],
            availableWidth = SCREEN_WIDTH - DEFAULT_SAKURA_IMAGE_WIDTH,
            availableHeight = SCREEN_HEIGHT - DEFAULT_SAKURA_IMAGE_HEIGHT,
            sakuraImageHeight = DEFAULT_SAKURA_IMAGE_HEIGHT,
            sakuraImageWidth = DEFAULT_SAKURA_IMAGE_WIDTH,
            sakuraImageArray
        } = this.props.data;

        let sakuraImageIndex = parseInt(String(Math.random() * sakuraImageArray.length));
        let sakuraImage = sakuraImageArray[sakuraImageIndex];
        let sakuraImageType = "SINGLE_SAKURA";
        if (sakuraImageIndex === 0) sakuraImageType = "SAKURA";

        // noinspection DuplicatedCode
        return (
            <Animated.div
                style={{
                    position: "absolute",
                    zIndex: 1,
                    // 渐变动画
                    opacity: this.state.sakuraTranslateValue.interpolate({
                        inputRange: [0, 0.7, 1],
                        outputRange: [1, 1, 0]
                    }),
                    // 位移动画
                    transform: [{
                        translateX: this.state.sakuraTranslateValue.interpolate({
                            inputRange: [0, 1],
                            outputRange: [parseInt(String(Math.random() * availableWidth)), parseInt(String(Math.random() * availableWidth))]
                        })
                    }, {
                        translateY: this.state.sakuraTranslateValue.interpolate({
                            inputRange: [0, 1],
                            outputRange: [-60, availableHeight + 60]
                        })
                    }]
                }}
            >
                <Animated.img
                    src={sakuraImage}
                    alt={"sakura"}
                    height={sakuraImageHeight}
                    width={sakuraImageWidth}
                    onClick={() => {
                        console.warn("you clicked");
                    }}
                    style={{
                        // 旋转动画
                        transform: [
                            this._sakuraRotateTransform(sakuraImageType),
                            {
                                scale: sakuraScaleValue
                            }
                        ]
                    }}
                />
            </Animated.div>
        );
    }
}
