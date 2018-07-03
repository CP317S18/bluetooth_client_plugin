#import "BluetoothClientPlugin.h"
#import <bluetooth_client/bluetooth_client-Swift.h>

@implementation BluetoothClientPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftBluetoothClientPlugin registerWithRegistrar:registrar];
}
@end
