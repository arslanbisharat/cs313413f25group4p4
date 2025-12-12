# Background

The goal of this project is to convert a simple Java stopwatch to an
Android application.  The original java code can be found 
[here](https://github.com/concurrency-cs-luc-edu/simplestopwatch-java).

# Learning Objectives

## Modeling

* Modeling state-dependent behavior with state machine diagrams
  (see also [here](/loyolachicagocs_comp313/stopwatch-android-java/src/default/doc))
* Distinguishing between view states and (behavioral) model states

## Semantics

* Event-driven/asynchronous program execution
* User-triggered input events
* Internal events from background timers
* Concurrency issues: single-thread rule of accessing/updating the view in the GUI thread

## Architecture and Design

* Key architectural issues and patterns
    * Simple dependency injection (DI)
    * Model-view-adapter (MVA) architectural pattern
    * Mapping MVA to Android
    * Difference between MVA and model-view-controller (MVC)
    * Distinguishing among dumb, reactive, and autonomous model components
* Key design patterns
    * Implementing event-driven behavior using the Observer pattern
    * Implementing state-dependent behavior using the State pattern
    * Command pattern for representing tasks as objects
    * Façade pattern for hiding complexity in the model from the adapter
* Relevant class-level design principles
    * Dependency Inversion Principle (DIP)
    * Single Responsibility Principle (SRP)
    * Interface Segregation Principle (ISP)
* Package-level architecture and relevant principles
    * Dependency graph
      (see also [here](/loyolachicagocs_comp313/stopwatch-android-java/src/default/doc))
    * Stable Dependencies Principle (SDP)
    * Acyclic Dependencies Principle (ADP)
* [Architectural journey](/stopwatch-android-java/commits)

## Testing

* Different types of testing
    * Component-level unit testing
    * System testing
    * Instrumentation testing
* Mock-based testing
* Testcase Superclass pattern (uses Template Method pattern)
* Test coverage

# Setting up the Environment

Check out the project using IntelliJ IDEA. This creates the `local.properties` file
with the required line

    sdk.dir=<root folder of Android Studio's Android SDK installation>

# Running the Application

## Option 1: Using IntelliJ IDEA or Android Studio

In IntelliJ: `Run > Run app`

## Option 2: Using Command Line

1. **Build the app:**
   ```
   ./gradlew assembleDebug
   ```

2. **Start an Android emulator** (if not already running):

   First, list available AVDs:
   ```
   <path-to-android-sdk>/emulator/emulator -list-avds
   ```

   Then start an emulator:
   ```
   <path-to-android-sdk>/emulator/emulator -avd <avd-name> &
   ```

   Example:
   ```
   ~/Android/Sdk/emulator/emulator -avd Medium_Phone_API_36.1 &
   ```

3. **Install the app on the emulator:**
   ```
   ./gradlew installDebug
   ```

4. **Launch the app:**
   ```
   adb shell am start -n edu.luc.etl.cs313.android.stopwatch/edu.luc.etl.cs313.android.simplestopwatch.android.StopwatchAdapter
   ```

Alternatively, you can combine steps 1 and 3:
```
./gradlew installDebug
```

This will build and install the app in one command.

# Running the Tests

## Unit tests including out-of-emulator system tests using Robolectric

In Android Studio, use `Tools > SDK Manager` to install Android 6.0 (API level 23) for the Robolectric tests to work (skip this step if API level 23 is already installed).
Then, in the *Android* view, right-click on `edu.luc.etl.cs313...test (test)` and choose `Run 'Tests in 'test''`.

You can also use Gradle:

    $ ./gradlew testDebug

You can view the resulting test reports in HTML by opening this file in your browser:

    app/build/reports/tests/testDebugUnitTest/index.html

## Unit test code coverage

In Gradle:

    $ ./gradlew jacocoTestDebugUnitTestReport

You can view the resulting test reports in HTML by opening this file in your browser:

    app/build/reports/jacoco/jacocoTestDebugUnitTestReport/html/index.html

## Android instrumentation tests (in-emulator/device system tests)

In Android Studio:

* In the *Android* view, right-click on `edu.luc.etl.cs313...android (androidTest)`, then choose `Run 'Tests in 'edu.luc.et...'`

You can also use Gradle:

    $ ./gradlew connectedDebugAndroidTest
