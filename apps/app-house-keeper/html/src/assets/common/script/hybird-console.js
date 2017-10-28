import '../style/hybird-console.css'

(function () {
    var menu;
    var menu_clear;
    var element;
    var isShow = false;

    function createElement(cls, content, name) {
        var elem = document.createElement(name || 'div');
        elem.id = name
        if (name === 'img') {
            elem.src = content
        }
        if (content) {
            elem.innerHTML = content;
        }
        elem.classList.add('mobile-console__' + cls);
        return elem;
    }

    function scrollToBottom() {
        element.scrollTop = element.scrollHeight;
    }

    function escapeHTML(html) {
        return String(html)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#x27;')
            .replace(/\//g, '&#x2F;');
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
        let dateStr = hours + seperator2
            + minutes + seperator2
            + seconds + " "
            + milliseconds;
        return dateStr;
    }

    function inspect(obj, key, enumerable) {
        var content = createElement('log');
        var content_top = createElement('top');
        var node = createElement('node', '', 'span');

        var elemsCreated = false;
        var keyNode;
        var text;
        var props;

        if (arguments.length === 2) {
            enumerable = true;
        }

        if (key) {
            keyNode = createElement(enumerable ? 'enumerable-key' : 'not-enumerable-key', key + ':', 'span');
            content_top.appendChild(keyNode);
        }

        content.appendChild(content_top);
        content_top.appendChild(node);

        if (typeof obj === 'number') {
            node.innerHTML = obj;
            node.classList.add('number');
        } else if (typeof obj === 'string') {
            node.innerHTML = '"' + escapeHTML(obj) + '"';
            node.classList.add('string');
        } else if (obj === undefined) {
            node.innerHTML = 'undefined';
            node.classList.add('null');
        } else if (obj === null) {
            node.innerHTML = 'null';
            node.classList.add('null');
        } else if (obj === false || obj === true) {
            node.innerHTML = '' + obj;
        } else if (obj instanceof Function) {
            text = obj.toString();
            if (text.indexOf('[native code]') !== -1) {
                node.innerHTML = text;
            } else {
                node.innerHTML = text.split(/(\{)/).slice(0, 2).join('');
            }
        } else {
            node.innerHTML = obj.constructor.name || obj.constructor.toString().replace(/\[|\]|object\s/g, '');
            content.classList.add('inspect');
            if (Array.isArray(obj)) {
                node.innerHTML = '[' + obj.length + ']';
            }
            props = createElement('props');
            content_top.addEventListener('click', function () {
                var keys = [], elem, key;
                if (content.classList.contains('inspect')) {
                    if (!elemsCreated) {
                        for (key in obj) {
                            keys.push(key);
                        }
                        Object.getOwnPropertyNames(obj)
                            .concat(keys)
                            .filter(function (key, index, arr) {
                                return arr.indexOf(key) === index;
                            })
                            .sort()
                            .concat('__proto__')
                            .forEach(function (key) {
                                var enumerable = Object.getOwnPropertyDescriptor(obj, key);
                                enumerable = enumerable ? enumerable.enumerable : false;
                                elem = inspect(obj[key], key, enumerable);
                                props.appendChild(elem);
                            });
                        elemsCreated = true;
                    }
                    content.classList.remove('inspect');
                    content.classList.add('opened');
                    props.classList.add('visible');
                } else {
                    content.classList.add('inspect');
                    content.classList.remove('opened');
                    props.classList.remove('visible');
                }
            }, false);
            content.appendChild(props);
        }
        return content;
    }

    function createConsoleBlock(force) {
        if (force)
            element = undefined;
        if (!element) {
            element = createElement('holder', undefined, "console");
            element.classList.add("dialog-wrapper")
            document.body.appendChild(element);
        }
    }

    function createConsoleMenu() {
        if (!menu) {
            menu = createElement('menu', isShow ? require('../image/hybird-console-menu-toggleon.svg') : require('../image/hybird-console-menu-toggleoff.svg'), 'img');
            document.body.appendChild(menu);
            menu.onclick = function () {
                toggleDialog(!isShow)
            }
            menu.style.opacity = 0.4;
        }
    }

    function createConsoleClear() {
        if (!menu_clear) {
            menu_clear = createElement('menu_clear', require('../image/hybird-console-menu-clear.svg'), 'img');
            menu_clear.hidden = !isShow
            document.body.appendChild(menu_clear);
            menu_clear.onclick = function () {
                document.body.removeChild(element)
                createConsoleBlock(true);
            }
            menu_clear.style.opacity = 0.4;
        }
    }

    function toggleDialog(show) {
        isShow = show;
        createConsoleBlock();
        createConsoleClear();
        menu_clear.hidden = !isShow
        menu_clear.style.display = show ? "" : "none";
        menu.src = show ? require('../image/hybird-console-menu-toggleon.svg') : require('../image/hybird-console-menu-toggleoff.svg')
        element.hidden = !show
    }

    (function () {
        var log = console.log;
        console.log = function () {
            //log.call(this, 'My Console!!!');
            log.apply(this, Array.prototype.slice.call(arguments));

            if (/android|webos|iphone|ipad|ipod|blackberry|window\sphone/i.test(navigator.userAgent)) {
                createConsoleMenu();
                if (element) {
                    var message = ""
                    message = "[" + getNowFormatDate() + "] " + message
                    for (var i = 0; i < arguments.length; i++) {
                        message += arguments[i];

                        element.appendChild(inspect(arguments[i]));
                    }
                    scrollToBottom();
                }
                if (window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.native)
                    window.webkit.messageHandlers.native.postMessage(message);
            }

        };
    }());
})()
