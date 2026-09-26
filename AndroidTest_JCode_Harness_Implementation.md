# Android Test App — JCode Harness Implementation Specification

## 1. Objective

Create a minimal Android application entirely through JCode Harness to verify that the Android development environment is correctly configured and capable of:

- Creating an Android project
- Compiling Kotlin
- Building a Jetpack Compose UI
- Resolving Gradle dependencies
- Generating a debug APK
- Detecting an Android device through ADB
- Installing the APK
- Launching the application
- Interacting with the application
- Running basic automated verification

This is an environment-validation project, not the final component-inventory application.

---

# 2. Project Information

## Project name

`TestProj`

## Application name

`Android Environment Test`

## Package name

`com.onkar.androidtest`

## Language

Kotlin

## UI framework

Jetpack Compose

## Build system

Gradle with Kotlin DSL

## Minimum Android version

Use a currently supported minimum SDK appropriate for the installed Android SDK. Prefer:

```text
minSdk = 26
```

## Target/compile SDK

Use the latest stable SDK already installed on the machine. If no suitable SDK is installed, identify the missing SDK and install/configure it using the standard Android SDK tooling.

Do not hard-code an obsolete SDK version merely to make the project compile.

---

# 3. Important JCode Harness Requirement

JCode Harness is responsible for the complete implementation.

Do not merely describe what needs to be done.

JCode must:

1. Inspect the current environment.
2. Determine which Android development tools are installed.
3. Create the complete project.
4. Create all required source/configuration files.
5. Resolve Gradle dependencies.
6. Build the project.
7. Locate the generated APK.
8. Check ADB/device availability.
9. Install the APK when a device/emulator is available.
10. Launch the application.
11. Verify the application starts successfully.
12. Report exactly what succeeded and what failed.

Avoid unnecessary manual steps.

---

# 4. Environment Inspection

Before modifying anything, inspect the environment.

Check:

```bash
java -version
javac -version
adb version
which adb
```

Check Android SDK environment variables:

```bash
echo "$ANDROID_HOME"
echo "$ANDROID_SDK_ROOT"
```

Check common SDK locations if necessary:

```bash
ls -la "$ANDROID_HOME" 2>/dev/null
ls -la "$ANDROID_SDK_ROOT" 2>/dev/null
```

Check Gradle availability:

```bash
gradle --version
```

If the project uses Gradle Wrapper, prefer the wrapper:

```bash
./gradlew --version
```

Check available Android devices:

```bash
adb devices
```

Do not install duplicate tools if the required tools are already available.

---

# 5. Project Structure

Create a standard Android project with approximately this structure:

```text
AndroidTest/
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       ├── androidTest/
│       │   └── java/
│       │       └── com/
│       │           └── onkar/
│       │               └── androidtest/
│       │                   └── MainActivityTest.kt
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/
│           │   └── com/
│           │       └── onkar/
│           │           └── androidtest/
│           │               └── MainActivity.kt
│           └── res/
│               └── values/
│                   └── strings.xml
├── gradle/
│   └── wrapper/
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
└── README.md
```

Use the current stable Android/Compose project conventions rather than blindly copying obsolete templates.

---

# 6. Application UI

Create a single-screen Compose application.

The screen should clearly indicate that the Android environment is working.

Required UI:

```text
Android Environment Test

✓ Android application started
✓ Kotlin compiled successfully
✓ Jetpack Compose loaded

Counter: 0

[ TEST BUTTON ]

Status:
Ready
```

The exact visual design is flexible, but it should be clean, simple, and easy to verify.

---

# 7. Counter Functionality

Implement a simple counter.

Initial value:

```text
0
```

When the user presses:

```text
TEST BUTTON
```

increment the counter by one.

Example:

```text
Counter: 0
        ↓
TEST BUTTON
        ↓
Counter: 1
```

The UI must update immediately.

---

# 8. Test Status

Display a status message.

Initial state:

```text
Status: Ready
```

After pressing the button:

```text
Status: Test button works!
```

This gives us an obvious way to verify that:

- the app received the click,
- Compose state works,
- recomposition works.

---

# 9. Android Manifest

Create a valid Android manifest for the application.

The application must contain a launcher activity.

The launcher activity must be exported as required by the Android version being targeted.

Do not request unnecessary permissions.

This test application should require **no dangerous permissions**.

---

# 10. MainActivity Requirements

Use a standard Android `ComponentActivity`.

Use:

```kotlin
setContent {
    ...
}
```

Use Jetpack Compose.

Use state management appropriate for a simple Compose application, such as:

```kotlin
remember { mutableStateOf(...) }
```

or:

```kotlin
remember { mutableIntStateOf(...) }
```

Keep the implementation simple.

Do not introduce:

- databases
- networking
- authentication
- Firebase
- unnecessary third-party libraries
- dependency injection frameworks
- background services

The purpose is environment validation.

---

# 11. Automated UI Test

Create at least one Android instrumentation/UI test.

The test should verify:

1. The application launches.
2. The main screen contains the expected title/text.
3. The counter initially displays `0`.
4. The test button can be clicked.
5. The counter changes to `1`.
6. The status changes to the expected success message.

Use the currently supported Compose testing APIs.

Do not use deprecated testing APIs when an available stable replacement exists.

---

# 12. Unit Test

If practical, also create a small JVM unit test for any simple logic extracted from the UI.

This is optional.

Do not create unnecessary architecture solely to justify a unit test.

The primary automated test should be the Android UI/instrumentation test.

---

# 13. Gradle Requirements

Use Gradle Kotlin DSL.

Use the Gradle Wrapper.

The project must be buildable using:

```bash
./gradlew assembleDebug
```

and, where an emulator/device is available:

```bash
./gradlew connectedDebugAndroidTest
```

If `connectedDebugAndroidTest` cannot run because no Android device/emulator is connected, report that clearly rather than treating it as a project compilation failure.

---

# 14. Build Verification

Run:

```bash
./gradlew clean
```

Then:

```bash
./gradlew assembleDebug
```

The build must complete successfully.

Find the APK:

```text
app/build/outputs/apk/debug/
```

The expected artifact should be similar to:

```text
app-debug.apk
```

Verify that the APK exists.

---

# 15. ADB Verification

Run:

```bash
adb devices
```

Possible results:

### Physical Android device

Example:

```text
List of devices attached
XXXXXXXX    device
```

If a device is listed as `device`, continue with installation.

### Emulator

Example:

```text
List of devices attached
emulator-5554    device
```

Continue with installation.

### No device

If the output contains no usable device:

```text
List of devices attached
```

do not fail the project.

Instead report:

```text
Build verification: PASSED
APK generation: PASSED
ADB installation: NOT RUN — no Android device/emulator detected
```

---

# 16. APK Installation

When a device is available, install the debug APK.

Use:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

If necessary, uninstall an older test installation first:

```bash
adb uninstall com.onkar.androidtest
```

Then reinstall.

Verify installation using:

```bash
adb shell pm list packages | grep com.onkar.androidtest
```

---

# 17. Launch Application

Launch the application using ADB.

Use the package/activity information generated by the project.

The launcher activity should be:

```text
com.onkar.androidtest.MainActivity
```

Launch with an appropriate ADB command, for example:

```bash
adb shell am start -n com.onkar.androidtest/.MainActivity
```

Verify that Android reports the activity starting successfully.

---

# 18. Runtime Verification

If a physical device or emulator is available, verify:

- App launches.
- No immediate crash occurs.
- Main title is visible.
- Counter is visible.
- Test button is visible.
- Pressing the button increments the counter.
- Status changes after the button is pressed.

If UI automation is available, use it.

Otherwise, use the instrumentation test and ADB-level verification.

---

# 19. Automated Test Command

Attempt:

```bash
./gradlew connectedDebugAndroidTest
```

If a device is available, this should execute the instrumentation tests.

If there is no device/emulator, report:

```text
Instrumentation tests: BLOCKED — no connected Android device/emulator
```

Do not incorrectly classify this as a source-code failure.

---

# 20. Error Handling

If something fails:

1. Identify the exact command that failed.
2. Capture the relevant error.
3. Determine whether it is:
   - project configuration,
   - Gradle,
   - Java/JDK,
   - Android SDK,
   - ADB,
   - emulator,
   - physical-device authorization,
   - application/runtime,
   - test failure.
4. Fix the issue when it is safely and reasonably fixable.
5. Re-run the failed verification.
6. Do not hide errors.
7. Do not claim success without actually verifying it.

---

# 21. Physical Device Handling

If using a physical Android phone:

Check:

```bash
adb devices
```

If the device reports:

```text
unauthorized
```

do not attempt to bypass Android security.

Tell the user to:

1. Unlock the phone.
2. Enable Developer Options if necessary.
3. Enable USB debugging.
4. Accept the RSA debugging authorization prompt.
5. Run:

```bash
adb devices
```

again.

JCode should then continue with installation/testing once the device becomes authorized.

---

# 22. Emulator Handling

If no physical device is connected but an Android emulator is already available, use it.

Do not create multiple unnecessary emulators.

If no emulator exists, do not automatically spend excessive time downloading large system images unless explicitly requested.

Instead report that an emulator is required for device-level verification.

The project itself must still be built and verified independently.

---

# 23. README

Create a `README.md` explaining:

- What the project is
- Required tools
- How to build
- How to install
- How to run
- How to run tests
- Expected package name
- Expected APK location
- How to connect a physical Android phone

Include commands such as:

```bash
./gradlew assembleDebug
```

```bash
./gradlew connectedDebugAndroidTest
```

and:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

# 24. Final Verification Report

At the end, JCode must provide a concise verification report.

Use this structure:

```text
========================================
ANDROID TEST PROJECT VERIFICATION
========================================

Project creation:       PASS/FAIL
Kotlin compilation:     PASS/FAIL
Gradle build:           PASS/FAIL
Compose compilation:    PASS/FAIL
Debug APK generated:    PASS/FAIL
APK path:               <path>

ADB available:          PASS/FAIL
Android device found:   YES/NO
Device authorized:      YES/NO
APK installation:       PASS/FAIL/NOT RUN
Application launch:     PASS/FAIL/NOT RUN
UI instrumentation:     PASS/FAIL/NOT RUN

Overall project status: PASS/FAIL/PARTIAL
========================================
```

If something is blocked by missing hardware, clearly distinguish **BLOCKED/NOT RUN** from **FAIL**.

---

# 25. Completion Criteria

The project is considered successfully completed when all applicable conditions are true:

### Required

- [ ] Android project created
- [ ] Kotlin source compiles
- [ ] Gradle build succeeds
- [ ] Compose UI compiles
- [ ] Debug APK generated
- [ ] Package name is `com.onkar.androidtest`
- [ ] Launcher activity works
- [ ] Counter functionality implemented
- [ ] Test status functionality implemented
- [ ] README created
- [ ] At least one Android UI/instrumentation test created

### Device-dependent

- [ ] ADB detects device
- [ ] APK installs
- [ ] Application launches
- [ ] UI test executes successfully

Device-dependent items should only be marked as blocked when the required device/emulator is genuinely unavailable.

---

# 26. Constraints

Keep the project intentionally small.

Do NOT add:

- Firebase
- Room
- Retrofit
- Ktor
- Hilt
- Navigation libraries unless genuinely required
- Authentication
- Internet permissions
- Backend servers
- Databases
- Cloud services
- unnecessary animations
- unnecessary dependencies

The goal is to validate the Android development pipeline before beginning a larger application.

---

# 27. Future Project

This project is a disposable technical validation project.

After successful verification, the next project will be the actual Android component-inventory application.

Do not mix future component-inventory functionality into this test application.

---

# 28. JCode Execution Directive

Execute this specification from start to finish.

Do not stop after generating the files.

The expected workflow is:

```text
Inspect environment
       ↓
Create Android project
       ↓
Implement Compose UI
       ↓
Implement counter
       ↓
Implement status
       ↓
Create UI test
       ↓
Build project
       ↓
Generate APK
       ↓
Check ADB
       ↓
Detect device/emulator
       ↓
Install APK if possible
       ↓
Launch APK if possible
       ↓
Run instrumentation tests if possible
       ↓
Fix discovered project issues
       ↓
Re-run verification
       ↓
Generate final verification report
```

Only report the project as fully successful when the corresponding checks have actually been performed.

