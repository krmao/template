import '../style/hybird-console.css'
import '../style/dialog.css'

(function () {
    var menu;
    var element;

    function createElement(cls, content, name) {
        var elem = document.createElement(name || 'div');
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

    function inspect(obj, key, enumerable) {
        var content = createElement('log'),
            top = createElement('top'),
            node = createElement('node', '', 'span'),
            elemsCreated = false,
            keyNode, text, props;

        if (arguments.length === 2) {
            enumerable = true;
        }

        if (key) {
            keyNode = createElement(enumerable ? 'enumerable-key' : 'not-enumerable-key', key + ':', 'span');
            top.appendChild(keyNode);
        }

        content.appendChild(top);
        top.appendChild(node);
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
            top.addEventListener('click', function () {
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

    function createConsoleBlock() {
        if (!element) {
            element = createElement('holder');
            element.classList.add("dialog-wrapper")
            document.body.appendChild(element);
        }
    }

    function createConsoleMenu() {
        if (!menu) {
            menu = createElement('menu', require('../image/hybird-console-menu.svg'), 'img');
            document.body.appendChild(menu);
            menu.onclick = function () {
                // element.hidden = !element.hidden
                toggleDialog(!isShow)
            }
        }
    }

    var isShow = true;

    function toggleDialog(show) {
        isShow = show
        var animation = function () {
            element.classList.remove(show ? "slipBottom" : "slipUp")
            element.classList.add(show ? "slipUp" : "slipBottom")
        };
        setTimeout(animation, 100);
    }

    if (/android|webos|iphone|ipad|ipod|blackberry|window\sphone/i.test(navigator.userAgent)) {
        console.error = console.log = function (message) {
            createConsoleMenu();
            createConsoleBlock();
            element.appendChild(inspect(message));
            scrollToBottom();
        };
    }
})()
