class Native {

    static showToast(message) {
        this.showToastWithDuration(message, null)
    }

    static showToastWithDuration(message, duration) {
        this.invoke("showToast", [message, duration].filter(it => it !== undefined && it !== null))
    }

    static test(message, duration, callback) {
        return this.invoke(callback, "showToast", "Hello Native ,Mao", 2000)
    }

    static invoke(callback, ...args) {
        let className = "native"
        let methodName = args[0]
        var patamString = ""

        args.slice(1, args.length).forEach(
            function (value, index, array) {
                patamString += encodeURIComponent(array[index]) + (index < array.length - 1 ? "," : "")
            }
        )

        let hashcode = new Date().getTime()
        let url = "hybird://hybird:1234/" + className + "/" + methodName + "?params=" + patamString + "&hashcode=" + hashcode
        this.goTo(url)
        callback()
        console.log("callback")
    }

    static goTo(url) {
        console.log("[Native:goTo] url: " + url)

        var div = document.createElement("div");
        div.innerHTML = '<iframe style="display: none;" src="' + url + '"/>';
        document.querySelector("body").appendChild(div);
        setTimeout(function () {
            document.querySelector("body").removeChild(div);
        }, 1000);
    }
}

export default Native
