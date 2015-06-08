OpenVPN-Android-Lib
===========================

## Pre-Installation

1. Make sure you have Java JDK **8** version installed
2. Install Android Studio
3. Configure Android Studio to use JDK 8

    3.1.  Android Studio -> File -> Other Settings.. -> Default Project Structure..

4. Install Android SDK and [NDK](https://developer.android.com/tools/sdk/ndk/index.html#Installing) on your

## Installation

### 1. Import the git to your project
1. Add the **openvpn-android-library**'s repository to the remotes list:
    ```
    git remote add -f openvpn-lib https://bitbucket.org/rimoto/openvpn-android-lib.git
    ```
1. Add the last version:
    ```
    git subtree add --prefix openvpn-lib openvpn-lib master --squash
    ```

### 2. Embed the library in the gradle
1. Import the **openvpn-android-library** in your **project**'s `settings.gradle` file:
    ```
    include ':openvpn-lib:openvpn' // OpenVPN Library
    ```
1. Import the **openvpn** library in your **module**'s gradle:
    ```
    dependencies {
        ...
        compile project(':openvpn-lib:openvpn')
    }
    ```
### 3. Build the native libs

1. [Install Android NDK](https://developer.android.com/tools/sdk/ndk/index.html#Installing)
1. Enter to the `openvpn-lib` module dir: `cd openvpn-lib/openvpn`
1. Run `./misc/build-native.sh`

### Updating the library
Just update the git's subtree:
```
git subtree pull --prefix openvpn-lib openvpn-lib master --squash
```
**Note:** We use the `--squash` flag in order to not include the repo's git history.

## Usage

**See usage example in `example` module**
In general, in order to work with this library you should follow this flow:
1. Import a VPN profile (the `importConfig` method already do the steps detailed below)
    1. Convert a `*.ovpn` config file to a VPN Profile
    1. Save the Profile
1. Start the VpnManager (The VPN Manager will automatically start the VPN when the policy allows it)

### API
Most of the interactions should be done with VpnManager.

1. **VpnManager.importConfig(Context context, Reader reader[, String name])**

    Import VPN profile from an `.ovpn` file

    * param `context` ***Context***
    * param `reader` ***Reader*** of the file
    * param `name` ***String*** of the profile name *[optional]*
    * return ***VpnProfile***

1. **saveProfile(Context context, VpnProfile profile)**

    Save ***VpnProfile*** to the profile manager

    * param `profile` ***VpnProfile*** object

1. **isActive(Context context)**

    Is *VpnManager* have active profile
    **Active profile** means that the VpnManager will connect automatically when the profile's
    policy allows it.

    * param `context` ***Context***
    * return ***Boolean***

1. **setActiveProfile(Context context, VpnProfile vpnProfile)**

    Set an active VPN profile

    * param `context` ***Context***
    * param `vpnProfile` ***VpnProfile***

1. **setActiveProfileDisconnected(Context context)**

    Set the "active VPN profile" as disconnected

    * param `context` ***Context***

1. **getActiveProfile(Context context)**

    Get the active VPN profile
    * param `context` ***Context***
    * return ***VpnProfile***

1. **getCurrentNetworkInfo(Context context)**

    Helper: get current NetworkInfo
    * param `context` ***Context***
    * return ***NetworkInfo***

### Intents - Starting and Stopping

#### Start
1. In order to start call the service.
1. Set action as `VpnManager.ACTION_CONNECT`
1. Sent extra data `VpnManager.EXTRA_PROFILE_UUID` as the selected VpnProfile's **UUID**

```
Intent intent = new Intent(this, VpnManager.class);
intent.setAction(VpnManager.ACTION_CONNECT);

intent.putExtra(VpnManager.EXTRA_PROFILE_UUID, mProfile.getUUIDString());
startService(intent);
```

#### Stop
1. In order to stop call the service.
1. Set action as `VpnManager.ACTION_DISCONNECT`

```
Intent intent = new Intent(this, VpnManager.class);
intent.setAction(VpnManager.ACTION_DISCONNECT);

startService(intent);
```


## Enable VPN debugging logs
In order to enable debugging from the VpnStatus service, change the class static `logcat_enabled` to `true`.
