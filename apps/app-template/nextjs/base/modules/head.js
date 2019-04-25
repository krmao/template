import React from "react";

import BannerAnim from "rc-banner-anim";
import QueueAnim from "rc-queue-anim";
import TweenOne from "rc-tween-one";

import "rc-banner-anim/dist/rc-banner-anim.css";
import "./head-banner.css";

import css from "./head.scss";

const {Element, Thumb} = BannerAnim;
const BgElement = Element.BgElement;

export default class extends React.Component {
    constructor(props) {
        super(props);
        this.imgArray = [
            "https://os.alipayobjects.com/rmsportal/IhCNTqPpLeTNnwr.jpg",
            "https://os.alipayobjects.com/rmsportal/uaQVvDrCwryVlbb.jpg"
        ];
        this.state = {
            enter: false
        };
    }

    onMouseEnter = () => {
        this.setState({
            enter: true
        });
    };

    onMouseLeave = () => {
        this.setState({
            enter: false
        });
    };

    render() {
        const thumbChildren = this.imgArray.map((img, i) =>
            <span key={i}><i style={{backgroundImage: `url(${img})`}}/></span>
        );
        return (
            <div className={css.root}>
                <div className={css.header}>
                    <img className={css.logo} src="../../static/logo.jpg" width={"80px"} height={"80px"} alt="logo"/>
                    <div className={css.title}>
                        XXX-TITLE
                    </div>
                    <div className={css.tip}>
                        *****************<br/>
                        ==========
                    </div>

                    <div className={css.tel}>
                        XXXXXXXXXX
                    </div>
                </div>
                <div className={css.menu}>
                    <table className={css.menuTable} width="100%" border="0" align="center" cellPadding={0} cellSpacing={0}>
                        <tbody>
                        <tr>
                            <td align="center" valign="middle">
                                HOME
                            </td>
                            <td align="center" valign="middle">
                                HOME
                            </td>
                            <td align="center" valign="middle">
                                HOME
                            </td>
                            <td align="center" valign="middle">
                                HOME
                            </td>
                            <td align="center" valign="middle">
                                HOME
                            </td>
                            <td align="center" valign="middle">
                                HOME
                            </td>
                            <td align="center" valign="middle">
                                HOME
                            </td>
                            <td align="center" valign="middle">
                                HOME
                            </td>
                        </tr>
                        </tbody>

                    </table>
                </div>
                <div className={css.banner}>
                    <BannerAnim onMouseEnter={this.onMouseEnter} onMouseLeave={this.onMouseLeave}>
                        <Element key="aaa" prefixCls="banner-user-elem">
                            <BgElement
                                key="bg"
                                className="bg"
                                style={{
                                    backgroundImage: `url(${this.imgArray[0]})`,
                                    backgroundSize: "cover",
                                    backgroundPosition: "center"
                                }}
                            />
                            <QueueAnim key="1" name="QueueAnim">
                                <h1 key="h1">Ant Motion Demo</h1>
                                <p key="p">Ant Motion Demo.Ant Motion Demo.Ant Motion Demo.Ant Motion Demo</p>
                            </QueueAnim>
                            <TweenOne
                                animation={{y: 50, opacity: 0, type: "from", delay: 200}}
                                key="2"
                                name="TweenOne"
                            >
                                Ant Motion Demo.Ant Motion Demo
                            </TweenOne>
                        </Element>
                        <Element key="bbb" prefixCls="banner-user-elem">
                            <BgElement
                                key="bg"
                                className="bg"
                                style={{
                                    backgroundImage: `url(${this.imgArray[1]})`,
                                    backgroundSize: "cover",
                                    backgroundPosition: "center"
                                }}
                            />
                            <QueueAnim key="1" name="QueueAnim">
                                <h1 key="h1">Ant Motion Demo</h1>
                                <p key="p">Ant Motion Demo.Ant Motion Demo.Ant Motion Demo.Ant Motion Demo</p>
                            </QueueAnim>
                            <TweenOne
                                animation={{y: 50, opacity: 0, type: "from", delay: 200}}
                                key="2"
                                name="TweenOne"
                            >
                                Ant Motion Demo.Ant Motion Demo
                            </TweenOne>
                        </Element>
                        <Thumb prefixCls="user-thumb" key="thumb" component={TweenOne} animation={{bottom: this.state.enter ? 0 : -70}}>
                            {thumbChildren}
                        </Thumb>
                    </BannerAnim>
                </div>
            </div>
        );
    }
}
