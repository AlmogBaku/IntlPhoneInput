Migration with upstream `ics-openvpn`
-------------------------------------


Changes:

1. `<project>/openvpn/src/main/java/de/blinkt/openvpn/core/ConfigParser.java`
    1. improving meta-parsing (PR #338)
    1. adding rimoto-meta-data
        1.
            ~ln 34:
            ```
            private HashMap<String, Vector<String>> metaRimoto = new HashMap<String, Vector<String>>();
            ```
            ~ln 61:
            ```
                // Check for Rimoto Meta information
                if(line.matches("^#(\\s+)?OVPN_RIMOTO_.*")) {
                    Vector<String> metaArg = parseMetaRimoto(line);
                    metaRimoto.put(metaArg.get(0), metaArg);
                    continue;
                }
            ```

            ~ln 103:
            ```
            	private Vector<String> parseMetaRimoto(String line) {
            		String meta = line.split("OVPN_RIMOTO_", 2)[1];
            		String[] parts = meta.split("=",2);

                    //trimming
                    for(int i=0; i<parts.length; i++) {
                        parts[i] = parts[i].trim();
                    }

            		Vector<String> rval = new Vector<String>();
                    Collections.addAll(rval, parts);
            		return rval;
            	}
            ```

            ~ ln 695 (before `return np;` at `convertProfile()`):
            ```
                attachRimotoMetaData(np);
            ```
            ~ ln 697 (after this method):
            ```
                /**
                 * Attach Rimoto Meta Data
                 * @param np VpnProfile
                 */
                private void attachRimotoMetaData(VpnProfile np) throws ConfigParseError {
                    Vector<String> wifi_policy = metaRimoto.get("WIFI_POLICY");
                    if(wifi_policy != null && wifi_policy.size() > 1) {
                        try {
                            np.mWifiPolicy = VpnProfile.WifiPolicy.valueOf(wifi_policy.get(1).toUpperCase());
                        } catch (IllegalArgumentException e) {
                            throw new ConfigParseError("Invalid mode for meta option `OVPN_RIMOTO_WIFI_POLICY` (valid options: WIFI|MOBILE|BOTH)");
                        }
                    }

                    Vector<String> roaming_policy = metaRimoto.get("ROAMING_POLICY");
                    if(roaming_policy != null && roaming_policy.size() > 1) {
                        try {
                            np.mRoamingPolicy = VpnProfile.RoamingPolicy.valueOf(roaming_policy.get(1).toUpperCase());
                        } catch (IllegalArgumentException e) {
                            throw new ConfigParseError("Invalid mode for meta option `OVPN_RIMOTO_ROAMING_POLICY` (valid options: ONLY|FALSE|BOTH)");
                        }
                    }
                }
            ```
1. `<project>/openvpn/src/main/java/de/blinkt/openvpn/core/OpenVPNService.java`
    1. disable all `getLogPendingIntent()`
    1. disable `showNotification()` contents (?)
1. `<project>/openvpn/src/main/java/de/blinkt/openvpn/core/VpnStatus.java`
    1. Add accessor to lastState:
        ```
            public static String getLaststate() {
                return mLaststate;
            }
        ```
    1. `import net.rimoto.vpnlib.VpnFileLog;
    1. Add to `static{}` block:
        ```
            addLogListener(new VpnFileLog());
        ```
1. `<project>/openvpn/src/main/java/de/blinkt/openvpn/VpnProfile.java`
    1. Add rimoto metadata
        ```
            /* Rimoto Meta Data */
            public static enum WifiPolicy {WIFI, MOBILE, BOTH};
            public WifiPolicy mWifiPolicy = WifiPolicy.BOTH;
            public static enum RoamingPolicy {ONLY, FALSE, BOTH};
            public RoamingPolicy mRoamingPolicy = RoamingPolicy.BOTH;
        ```
1. Delete
    1. `<project>/openvpn/src/main/java/android`
    1. `<project>/openvpn/src/main/java/org`
    1. `<project>/openvpn/src/main/java/de/blinkt/openvpn/fragments/`
    1. `<project>/openvpn/src/main/java/de/blinkt/openvpn/views/`
    1. `<project>/openvpn/src/main/java/de/blinkt/openvpn/activities/`
        1. everything BUT `DisconnectVPN.java`
    1. `<project>/openvpn/src/main/aidl/`
    1. `<project>/openvpn/src/main/java/de/blinkt/openvpn/api/`
    1. `<project>/openvpn/src/main/java/de/blinkt/openvpn/LaunchVPN.java`
    1. `<project>/openvpn/src/main/java/de/blinkt/openvpn/OnBootReceiver.java`
    1. `<project>/openvpn/src/main/res/layout` [?]
1. Entirely different
    1. `<project>/openvpn/src/main/AndroidManifest.xml`
    1. `<project>/openvpn/src/main/res/`
