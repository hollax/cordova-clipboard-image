#import "ClipboardImagePlugin.h"
#import <Cordova/CDV.h>

@implementation ClipboardImagePlugin

- (void)getClipboardImage:(CDVInvokedUrlCommand*)command {
    UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
    
    if (pasteboard.image) {
        NSData *imageData = UIImagePNGRepresentation(pasteboard.image);
        NSString *base64String = [imageData base64EncodedStringWithOptions:NSDataBase64Encoding64CharacterLineLength];
        
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:base64String];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    } else {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"No image found in clipboard."];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }
}

@end
