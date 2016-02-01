## OpenCV 3.1 tests

This is a test branch which integrates OpenCV 3.1 into echOpen's Android application in order to 1) speed up the polar conversion of the ultrasound image received from the probe 2) integrate with networking code running on the probe('s single-board computer) which sends data over TCP.

The differences from the `master` branch are discussed below (in order to facilitate a merge):

### OpenCV 3.1 installation

Note: OpenCV versions 3 and later should be used as OpenCV 2.4.11 for Android lacks the polar interpolation functions.

The integration of OpenCV into the Android project (being developed in Android Studio) is a bit error-prone, and differs from one version of OpenCV and from one version of Android Studio to another. Here are the steps that worked for OpenCV 3.1 & Android Studio 1.5.1, based on [these instructions](http://stackoverflow.com/a/17368359/426790) (which seem to work for both OpenCV 2 & 3):

- Download the OpenCV 3.1 for Android from [opencv.org](http://opencv.org)
- Import the folder in Android Studio as an existing project
- Build everything in the project. Close the project.
- In the android-app project, choose Import Module, then select the <opencv directory>/sdk/java/sdk.iml file.
- Right-click the "app" in the project pane, choose "Open Module Settings"; under "Dependencies", click the "+" button, then choose "Module dependency", and then choose OpenCV
- from the OpenCV folder, copy the sdk/native/libs directory into your project's app/src/main, then rename it `jniLibs`

### OpenCV notes

The image interpolation is done in `ScanConversion`'s`opencv_interpolation` and basically consists of passing the `envelope_data` to `Imgproc.linearPolar`. A few steps are done before and after: the source `envelope_data` is copied into a single channel 8-bit `Mat` (e.g., OpenCV image) object of a larger dimension, while the other `Mat` objects used in the conversion are cleared. After the conversion, the resulting data is converted to a 32-bit single-channel `Mat` object before its content is copied into the destination Java `int[]` buffer.

It might be interesting to explore if the number of conversions (from one image size to another, and from one OpenCV `Mat` & Java `int[]` format to another) could be reduced as these happen once every new frame.

All OpenCV `Mat` objects are allocated once in a class block initializer in `ScanConversion`.

### TCP networking

A hastily-written `ProcessTCPTask` class was added to the project, based on the existing `ProcessUPDTask`. The basic TCP communication protocol (as of now) consists of sending the length of the message over 4 bytes (in little-endian order), followed by the message. The method `bytesToHex` in `ProcessTCPTask` was added to help debugging (to print out the network-transfered data); it can safely be removed.

**Note**: as of now, the project expects to connect to a server whose address & port are defined in the `Constants` class. To use a debugging image instead, modify `MainActivity`'s `onCreate` method (where `bitmapDisplayer.readDataFromTCP()` is called). The OpenCV code was written with a single-channel 8-bit original image source (coming from the probe) in mind; it has not been tested with an `envelope_data` coming from an image file.

### Misc

Most of the UI elements, except for the main `ImageView`, were removed from the `activity_main` screen. All of the related touch event handlers were commented out or removed.

5 `SeekBar` elements were added to help debugging the OpenCV interpolation function values; the `SeekBar` values transit through a `UIParams` class whose only purpose is to pass the UI values to the `ScanConversion` class.

---

This branch was developed over a few days on-site at echOpen's Hotel Dieu headquarters.

Thanks to all for the warm welcome and outstanding collaboration!
