// noinspection JSUnusedGlobalSymbols,JSUnusedLocalSymbols,JSUnresolvedVariable

export default class BasicScrollContainerUtil {
    static currentScrollTop = (id) => document.getElementById(id).scrollTop;

    static scrollToTop = (containerId, smooth = true) => {
        let element = document.getElementById(containerId);
        if (smooth) {
            element.scrollTo({top: 0, behavior: "smooth"});
        } else {
            element.scrollTop = 0;
        }
    };

    static scrollToBottom = (containerId, smooth = true) => {
        let element = document.getElementById(containerId);
        if (smooth) {
            element.scrollTo({top: element.scrollHeight - element.clientHeight, behavior: "smooth"});
        } else {
            element.scrollTop = element.scrollHeight - element.clientHeight;
        }
    };

    static isOnBottom = (id) => {
        let element = document.getElementById(id);
        return element.scrollTop === element.scrollHeight - element.clientHeight;
    };

    static isOnBottomByScrollEvent = (event) => {
        return event.target.scrollTop === event.target.scrollHeight - event.target.clientHeight;
    };
    static isOnTop = (id) => {
        let element = document.getElementById(id);
        return element.scrollTop === 0;
    };

    static isOnTopByScrollEvent = (event) => {
        return event.target.scrollTop === 0;
    };
}
