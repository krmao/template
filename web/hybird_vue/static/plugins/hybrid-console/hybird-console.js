window.hybirdConsole = {};
module.export = (function(bindObj = null) {
    let _bind = bindObj && bindObj instanceof Object ? bindObj : {};

    var btn_menu;
    var btn_menu_off;
    var btn_clear;
    var container;
    var container_background;
    var isContentShow = false;

    function createElement(styleClass, content, name) {
        var elem = document.createElement(name || "div");
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
        var date = new Date();
        var seperator1 = "/";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        var hours = date.getHours();
        var minutes = date.getMinutes();
        var seconds = date.getSeconds();
        var milliseconds = date.getMilliseconds();
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
        // noinspection UnnecessaryLocalVariableJS
        let dateStr = hours + seperator2 + minutes + seperator2 + seconds + " " + milliseconds;
        return dateStr;
    }

    function inspect(obj, key, enumerable) {
        var content = createElement("log");
        var content_top = createElement("top");
        var node = createElement("node", "", "span");

        var elemsCreated = false;
        var keyNode;
        var text;
        var props;

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
                    var keys = [],
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
                                    var enumerable = Object.getOwnPropertyDescriptor(obj, key);
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

    function initMenus(hostAddress) {
        if (!btn_menu_off) {
            btn_menu_off = createElement("menu", hostAddress + "./hybird-console-menu-toggleoff.svg", "img");
            document.body.appendChild(btn_menu_off);
            btn_menu_off.onclick = function() {
                toggleConsole(true);
            };
            setVisible(btn_menu_off, !isContentShow);
        }
        if (!btn_menu) {
            btn_menu = createElement("menu", hostAddress + "./hybird-console-menu-toggleon.svg", "img");
            document.body.appendChild(btn_menu);
            btn_menu.onclick = function() {
                toggleConsole(false);
            };
            setVisible(btn_menu, isContentShow);
        }
        if (!btn_clear) {
            btn_clear = createElement("menu_clear", hostAddress + "./hybird-console-menu-clear.svg", "img");
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
            var message = "";
            message = "[" + getNowFormatDate() + "] " + message;
            for (var i = 0; i < params.length; i++) {
                message += params[i];
                container.appendChild(inspect(params[i]));
            }
            scrollToBottom();
        }
        if (window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.native) window.webkit.messageHandlers.native.postMessage(message);
    }

    bindObj.init = function(hostAddress) {
        if (navigator && /android|webos|iphone|ipad|ipod|blackberry|window\sphone/i.test(navigator.userAgent)) {
            initMenus(hostAddress);
            toggleConsole(false);
            var log = console.log;
            console.log = function() {
                log.apply(this, Array.prototype.slice.call(arguments));
                insepctLog(arguments);
            };
        }
    };
})(window.hybirdConsole);
