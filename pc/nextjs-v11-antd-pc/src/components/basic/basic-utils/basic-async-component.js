import React, {Component} from "react";

/**
 * 解决不支持服务端渲染的 组件 import 问题
 * https://zhuanlan.zhihu.com/p/68218860
 *
 * 注意:
 *      1. 使用该组件需要在类的外部(文件级作用域)异步导入
 *          1.1 const ChartsArea = AsyncComponent(() => import("../../../components/ChartsArea"));
 *      2. 不想使用该组件也可以通过强制客户端渲染的方式达到同样的效果(csr!)
 *          2.1 import { Line } from 'csr!@ant-design/charts';
 *          2.2 return typeof window !== "undefined" ? <Line {...config} /> : null;
 * 何时使用:
 *      1. import @ant-design/charts 时报错: Error: Unable to parse color from object [247,249.7,255]
 */
const BasicAsyncComponent = (component) => {
    return class _ extends Component {
        state = {
            component: null
        };

        componentDidMount() {
            component().then((cmp) => {
                this.setState({component: cmp.default});
            });
        }

        render() {
            const C = this.state.component;
            return C ? <C {...this.props} /> : null;
        }
    };
};

export default BasicAsyncComponent;
