/**
 * 在 <head> 里面添加
 * <link rel="stylesheet" href="/static/plugins/hybrid-console/hybird-console.css"/>

 *
 * 在 <body> 之后添加
 * <script type="text/javascript" src="/static/plugins/hybrid-console/hybird-console.js" />
 */
/* eslint-disable */
(function() {
    let btn_menu;
    let btn_menu_off;
    let btn_clear;
    let container;
    let container_background;
    let isContentShow = false;

    function createElement(styleClass, content, name) {
        let elem = document.createElement(name || "div");
        elem.id = name;
        if (name === "img") {
            elem.src = content;
        }
        if (content) {
            elem.innerHTML = content;
        }
        elem.classList.add("hybird_console_" + styleClass);
        return elem;
    }

    function scrollToBottom() {
        container.scrollTop = container.scrollHeight;
    }

    function escapeHTML(html) {
        return String(html)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#x27;")
            .replace(/\//g, "&#x2F;");
    }

    function getNowFormatDate() {
        let date = new Date();
        let seperator1 = "/";
        let seperator2 = ":";
        let month = date.getMonth() + 1;
        let strDate = date.getDate();
        let hours = date.getHours();
        let minutes = date.getMinutes();
        let seconds = date.getSeconds();
        let milliseconds = date.getMilliseconds();
        if (hours >= 1 && hours <= 9) {
            hours = "0" + hours;
        }
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        if (minutes >= 0 && minutes <= 9) {
            minutes = "0" + minutes;
        }
        if (seconds >= 0 && seconds <= 9) {
            seconds = "0" + seconds;
        }
        if (milliseconds >= 0 && milliseconds <= 9) {
            milliseconds = "00" + milliseconds;
        } else if (milliseconds >= 10 && milliseconds <= 99) {
            milliseconds = "0" + milliseconds;
        }
        /*let dateStr = date.getFullYear() + seperator1
                + month + seperator1 + strDate + " "
                + hours + seperator2
                + minutes + seperator2
                + seconds + " "
                + milliseconds;*/
        // noinspection UnnecessaryLocalletiableJS
        let dateStr = hours + seperator2 + minutes + seperator2 + seconds + " " + milliseconds;
        return dateStr;
    }

    function inspect(obj, key, enumerable) {
        let content = createElement("log");
        let content_top = createElement("top");
        let node = createElement("node", "", "span");

        let elemsCreated = false;
        let keyNode;
        let text;
        let props;

        if (arguments.length === 2) {
            enumerable = true;
        }

        if (key) {
            keyNode = createElement(enumerable ? "enumerable-key" : "not-enumerable-key", key + ":", "span");
            content_top.appendChild(keyNode);
        }

        content.appendChild(content_top);
        content_top.appendChild(node);

        if (typeof obj === "number") {
            node.innerHTML = obj;
            node.classList.add("number");
        } else if (typeof obj === "string") {
            node.innerHTML = ">" + escapeHTML(obj) + "　";
            node.classList.add("string");
        } else if (obj === undefined) {
            node.innerHTML = "undefined";
            node.classList.add("null");
        } else if (obj === null) {
            node.innerHTML = "null";
            node.classList.add("null");
        } else if (obj === false || obj === true) {
            node.innerHTML = "" + obj;
        } else if (obj instanceof Function) {
            text = obj.toString();
            if (text.indexOf("[native code]") !== -1) {
                node.innerHTML = text;
            } else {
                node.innerHTML = text
                    .split(/(\{)/)
                    .slice(0, 2)
                    .join("");
            }
        } else {
            node.innerHTML = obj.constructor.name || obj.constructor.toString().replace(/\[|\]|object\s/g, "");
            content.classList.add("inspect");
            if (Array.isArray(obj)) {
                node.innerHTML = "[" + obj.length + "]";
            }
            props = createElement("props");
            content_top.addEventListener(
                "click",
                function() {
                    let keys = [],
                        elem,
                        key;
                    if (content.classList.contains("inspect")) {
                        if (!elemsCreated) {
                            for (key in obj) {
                                keys.push(key);
                            }
                            Object.getOwnPropertyNames(obj)
                                .concat(keys)
                                .filter(function(key, index, arr) {
                                    return arr.indexOf(key) === index;
                                })
                                .sort()
                                .concat("__proto__")
                                .forEach(function(key) {
                                    let enumerable = Object.getOwnPropertyDescriptor(obj, key);
                                    enumerable = enumerable ? enumerable.enumerable : false;
                                    elem = inspect(obj[key], key, enumerable);
                                    props.appendChild(elem);
                                });
                            elemsCreated = true;
                        }
                        content.classList.remove("inspect");
                        content.classList.add("opened");
                        props.classList.add("visible");
                    } else {
                        content.classList.add("inspect");
                        content.classList.remove("opened");
                        props.classList.remove("visible");
                    }
                },
                false
            );
            content.appendChild(props);
        }
        return content;
    }

    function initContainer(reset) {
        if (reset) {
            container = undefined;
        }
        if (!container_background) {
            container_background = createElement("container_background", undefined, "container_background");
            document.body.appendChild(container_background);
        }
        if (!container) {
            container = createElement("container", undefined, "container");
            document.body.appendChild(container);
        }
    }

    function initMenus(prefix) {
        if (!btn_menu_off) {
            btn_menu_off = createElement("menu", prefix + "hybird-console-menu-toggleoff.svg", "img");
            document.body.appendChild(btn_menu_off);
            btn_menu_off.onclick = function() {
                toggleConsole(true);
            };
            setVisible(btn_menu_off, !isContentShow);
        }
        if (!btn_menu) {
            btn_menu = createElement("menu", prefix + "hybird-console-menu-toggleon.svg", "img");
            document.body.appendChild(btn_menu);
            btn_menu.onclick = function() {
                toggleConsole(false);
            };
            setVisible(btn_menu, isContentShow);
        }
        if (!btn_clear) {
            btn_clear = createElement("menu_clear", prefix + "hybird-console-menu-clear.svg", "img");
            btn_clear.hidden = !isContentShow;
            document.body.appendChild(btn_clear);
            btn_clear.onclick = function() {
                document.body.removeChild(container);
                initContainer(true);
            };
            setVisible(btn_clear, isContentShow);
        }
    }

    function toggleConsole(isShow) {
        isContentShow = isShow;
        initContainer();
        setVisible(container, isContentShow);
        setVisible(container_background, isContentShow);
        setVisible(btn_menu_off, !isContentShow);
        setVisible(btn_menu, isContentShow);
        setVisible(btn_clear, isContentShow);
    }

    function setVisible(element, visible) {
        element.style.display = visible ? "block" : "none";
    }

    function insepctLog(params) {
        if (container) {
            let message = "";
            message = "[" + getNowFormatDate() + "] " + message;
            for (let i = 0; i < params.length; i++) {
                message += params[i];
                container.appendChild(inspect(params[i]));
            }
            scrollToBottom();
        }
        if (window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.native) window.webkit.messageHandlers.native.postMessage(message);
    }

    function getAttributeFromScript() {
        let prefix = "";
        let scripts = document.getElementsByTagName("script");
        for (let i = 0; i < scripts.length; i++) {
            let script = scripts[i];
            if (script && script.getAttribute("src") && script.getAttribute("src").indexOf("hybrid-console") > -1 && script.getAttribute("prefix")) {
                prefix = script.getAttribute("prefix");
            }
        }
        return prefix;
    }

    function getPrefix() {
        let prefix = "";
        let scripts = document.getElementsByTagName("script");
        for (let i = 0; i < scripts.length; i++) {
            let src = scripts[i] ? scripts[i].getAttribute("src") : null;
            if (src && src.indexOf("hybird-console.js") > -1) {
                prefix = src.replace("hybird-console.js", "");
            }
        }
        return prefix;
    }

    (function() {
        if (navigator && /android|webos|iphone|ipad|ipod|blackberry|window\sphone/i.test(navigator.userAgent)) {
            initMenus(getPrefix());
            toggleConsole(false);
            let log = console.log;
            console.log = function() {
                log.apply(this, Array.prototype.slice.call(arguments));
                insepctLog(arguments);
            };
        }
    })();
})();
