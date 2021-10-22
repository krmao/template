// noinspection JSUnresolvedFunction

import React, {useEffect, useRef, forwardRef, useImperativeHandle} from "react";
import {Input} from "antd";
import debounce from "lodash/debounce";

/**
 * wrap ant Input with debounce and composition
 *
 * options: PropTypes.object 透传 Input 组件的所有属性
 *
 * @see https://zhuanlan.zhihu.com/p/106805657
 * @see https://www.jianshu.com/p/bf9f66ac3f9c?utm_campaign=haruki
 * @see https://blog.csdn.net/qq_24724109/article/details/103817607
 * @see https://www.cnblogs.com/xiaonian8/p/13705311.html
 */
const BasicInputDC = (props, ref) => {
    // console.log("InputDC props", props);
    const {onChange, debounceWait} = props;

    // 内部 ref
    const innerRef = useRef();
    // 暴露内部 innerRef 的指定部分方法 blur/focus 方法给外部 ref
    useImperativeHandle(
        ref,
        () => {
            return {
                blur: () => innerRef.current.blur(),
                focus: () => innerRef.current.focus(),
                clear: () => innerRef.current.setState({value: ""})
            };
        },
        []
    );

    // 240ms 延时, 只返回最后一次调用执行的值
    const debounceOnChange = useRef(debounce((event) => !!onChange && onChange(event), debounceWait ?? 240));
    let compositionStart = useRef(false);

    useEffect(() => {
        console.log("InputDC useEffect props.value=", props.value, "innerRef=", innerRef);
        innerRef.current.setState({value: props.value ?? ""});
    }, [props.value, innerRef]);

    function _onChange(event) {
        // 中文输入法输入弹窗打开
        if (event.type === "compositionstart") {
            compositionStart.current = true;
            return;
        }

        // 中文输入法输入弹窗关闭
        if (event.type === "compositionend") {
            compositionStart.current = false;
        }

        // 非中文输入法输入弹窗打开时, 回调 onChange
        if (!compositionStart.current) {
            debounceOnChange.current(event);
        }
    }

    return (
        <Input
            {...props.options}
            ref={innerRef}
            onChange={_onChange}
            onCompositionEnd={_onChange}
            onCompositionStart={_onChange}
        />
    );
};

/**
 * 对外暴露 ref
 */
export default forwardRef(BasicInputDC);
