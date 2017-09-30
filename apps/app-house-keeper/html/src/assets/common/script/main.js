function formatStringWidthWithBlank(str, n, char) {
    var buffer = str;
    for (var i = 0; i < n - str.length; i++)
        buffer += char;
    return buffer;
}
