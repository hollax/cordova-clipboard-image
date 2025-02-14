describe("ClipboardImagePlugin", function () {
    it("should be defined", function () {
        expect(ClipboardImagePlugin).toBeDefined();
    });

    it("should have a getClipboardImage function", function () {
        expect(typeof ClipboardImagePlugin.getClipboardImage).toBe("function");
    });

    it("should have a previewClipboardImage function", function () {
        expect(typeof ClipboardImagePlugin.previewClipboardImage).toBe("function");
    });

    it("should fetch a clipboard image and return a Base64 string", function (done) {
        ClipboardImagePlugin.getClipboardImage(
            function (base64Image) {
                expect(base64Image).toMatch(/^data:image\/(png|jpeg);base64,/);
                done();
            },
            function (error) {
                fail("Failed to fetch clipboard image: " + error);
                done();
            }
        );
    });
});
