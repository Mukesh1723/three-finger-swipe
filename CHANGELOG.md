# Changelog


### Added

- Initial scaffold for more features
- Add About screen with Material You header and Licenses screen
- Replace single settings screen with multi-screen navigation
- Add step snapping and state restore support
- Add gesture tuning and capture mode settings
- Add gesture tuning and capture mode pref specs
- Add CaptureMode enum
- Add app icons and splash animation by @hxreborn
- Add settings screen and companion app by @hxreborn
- Add Material 3 Expressive theme by @hxreborn
- Add preference system with remote bridge by @hxreborn
- Add initial crop screenshot capture and delivery by @hxreborn
- Add screenshot trigger and dispatch resolver by @hxreborn
- Add three-finger swipe state machine by @hxreborn
- Add Xposed entry point and system_server hook by @hxreborn

### Fixed

- Simplify aboutLibraries release variant task
- Make action picker dialog scrollable
- Replace swipe toggle with inline action picker dialog
- Derive module status from XposedService binding

### Changed

- Remove SWIPE_ENABLED, use NO_ACTION to disable gesture
- Trim screenshot dispatch helpers
- Enrich SYSRQ dispatch logs
- Replace per-pref setters with generic savePref
- Integrate CaptureMode and GestureConfig into hook chain
- Cache swipe threshold and use readOrDefault
- Simplify screenshot dispatch with multi-path resolution
- Add findAllMethodsUpward and Method.signature helpers
- Expand PrefsState and extract pushToRemote
- Add IntPref range validation and readOrDefault extension
- Simplify gesture illustration
- Remove crop settings
- Remove crop prefs
- Drop crop gateway binding
- Clean up gesture handler
- Simplify screenshot trigger
- Simplify dispatch resolver
- Remove display capture gateway
- Remove screenshot delivery
- Remove crop capture core
