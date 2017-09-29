var jumpUrl="https://www.chexiang.com/";
(function (_jumpUrl) {
    console.log("11111");
    var c = document.createElement("div");
    c.innerHTML = '<iframe style="display: none;" src="' + _jumpUrl + '"/>';
    document.querySelector("body").appendChild(c);
    setTimeout(function () {
        console.log("22222");
        document.querySelector("body").removeChild(c);
    }, 3000);

    console.log("33333");
})(jumpUrl);
