import Router from 'next/router'

function push(url, mode) {
    if (mode === 'spa') {
        // noinspection JSIgnoredPromiseFromCall
        Router.push(url);
    } else {
        window.location.href = `${window.location.origin}/${url}`;
    }
}

function back(url, mode) {
    if (mode === 'spa') {
        Router.back();
    } else {
        window.history.back();
    }
}

export default {
    push,
    back
}
