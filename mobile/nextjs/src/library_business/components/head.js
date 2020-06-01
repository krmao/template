import React from "react";
import {withRouter} from "next/router";

import BannerAnim from "rc-banner-anim";
// import QueueAnim from "rc-queue-anim";
import TweenOne from "rc-tween-one";
import ComponentMenu from "./menu";

import "rc-banner-anim/dist/rc-banner-anim.css";
import "./head-banner.css";

import css from "./head.scss";

const {Element, Thumb} = BannerAnim;
const BgElement = Element.BgElement;

export default withRouter(
    class extends React.Component {
        constructor(props) {
            super(props);
            this.imgArray = ["https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556182550962&di=98e1b2558af75341de8ee360c282f5cf&imgtype=0&src=http%3A%2F%2F08.imgmini.eastday.com%2Fmobile%2F20180409%2F20180409101726_a01f0701e3bce0ebb09afb7a44756bac_1.jpeg", "http://www.hkdyfr.com/wp-content/uploads/2017/08/20170824155138500.jpg"];
            this.state = {
                enter: false
            };
            this.path = this.props.router.asPath;

            this.onMouseEnter = () => {
                this.setState({
                    enter: true
                });
            };

            this.onMouseLeave = () => {
                this.setState({
                    enter: false
                });
            };
        }

        render() {
            // console.log("path=" + this.path);

            const elementChildren = this.imgArray.map((item, index) => (
                <Element key={index} prefixCls="banner-user-elem">
                    <BgElement
                        key="bg"
                        className="bg"
                        style={{
                            backgroundImage: `url(${item})`,
                            backgroundSize: "cover",
                            backgroundPosition: "center"
                        }}
                    />
                    {/*<QueueAnim key={index} name="QueueAnim">
                    <h1 key="h1">Ant Motion Demo</h1>
                    <p key="p">Ant Motion Demo.Ant Motion Demo.Ant Motion Demo.Ant Motion Demo</p>
                </QueueAnim>
                <TweenOne
                    animation={{y: 50, opacity: 0, type: "from", delay: 200}}
                    key={index}
                    name={index}
                >
                    Ant Motion Demo.Ant Motion Demo
                </TweenOne>*/}
                </Element>
            ));

            const thumbChildren = this.imgArray.map((img, i) => (
                <span key={i}>
                    <i style={{backgroundImage: `url(${img})`}} />
                </span>
            ));
            return (
                <div className={css.root}>
                    <div className={css.header}>
                        <img className={css.logo} src="../../static/logo.png" alt="logo" />
                        <div className={css.title}>TT 婚纱摄影</div>
                        <div className={css.tip}>
                            诚信经营 品质保证
                            <br />
                            客户至上 尊贵典雅
                        </div>

                        <div className={css.tel}>热线: 18217758888</div>
                    </div>
                    <ComponentMenu />
                    <div className={css.banner}>
                        <BannerAnim onMouseEnter={this.onMouseEnter} onMouseLeave={this.onMouseLeave}>
                            {elementChildren}
                            <Thumb prefixCls="user-thumb" key="thumb" component={TweenOne} animation={{bottom: this.state.enter ? 0 : -70}}>
                                {thumbChildren}
                            </Thumb>
                        </BannerAnim>
                    </div>
                </div>
            );
        }
    }
);
