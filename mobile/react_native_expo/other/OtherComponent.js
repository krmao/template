import React, {Component} from "react";
import LottieView from "lottie-react-native";
import {View} from "react-native";

export default class OtherComponent extends Component {
    componentDidMount() {
        this.animation.play();
        // Or set a specific startFrame and endFrame with:
        this.animation.play(30, 120);
    }

    render() {
        return (
            <View style={{backgroundColor: 'pink', flex:1}}>

                <LottieView
                    ref={animation => {
                        this.animation = animation;
                    }}
                    source={require('../animations/Watermelon.json')}
                />

            </View>
        );
    }

}
