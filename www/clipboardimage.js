var exec = require('cordova/exec');

var ClipboardImage = {
    getClipboardImage: function (successCallback, errorCallback) {
        exec(successCallback, errorCallback, "ClipboardImage", "getClipboardImage", []);
    }
};

module.exports = ClipboardImage;
