const wdio = require("webdriverio");

const opts = {
    path: "/wd/hub",
    port: 4723,
    capabilities: {
        platformName: "Android", // or "iOS"
        platformVersion: "12.0", // Change according to your device
        deviceName: "emulator-5554",
        appPackage: "com.example.clipboardimage",
        appActivity: ".MainActivity",
        automationName: "UiAutomator2",
    },
};

describe("Clipboard Image Plugin E2E Tests", function () {
    let driver;

    beforeAll(async function () {
        driver = await wdio.remote(opts);
    });

    afterAll(async function () {
        await driver.deleteSession();
    });

    it("should open the app", async function () {
        const appTitle = await driver.$("android.widget.TextView");
        expect(await appTitle.getText()).toBe("Clipboard Image Plugin");
    });

    it("should copy an image to clipboard and retrieve it", async function () {
        // Copy image to clipboard using shell command
        await driver.execute("mobile: shell", {
            command: "input keyevent 29", // Simulates a clipboard copy event
        });

        // Click button that calls ClipboardImagePlugin.getClipboardImage
        const getImageButton = await driver.$("id=com.example.clipboardimage:id/get_image");
        await getImageButton.click();

        // Verify image is retrieved
        const previewImage = await driver.$("id=com.example.clipboardimage:id/image_preview");
        expect(await previewImage.isDisplayed()).toBe(true);
    });

    it("should save the clipboard image to storage", async function () {
        const saveButton = await driver.$("id=com.example.clipboardimage:id/save_image");
        await saveButton.click();

        // Verify image is saved
        const toastMessage = await driver.$("//android.widget.Toast");
        expect(await toastMessage.getText()).toContain("Image saved:");
    });
});
