[![Build Status](https://travis-ci.org/benchoufi/android-app.svg?branch=master)](https://travis-ci.org/benchoufi/android-app)

# Project
![](viewme.jpg)

## Name
[`PRJ-medtec_androidapp`]()

## Title
echOpen Android mobile application

## About
This android app aims to show medical image from an echOpen
ultrasound device. See more about this project on [echopen's
wiki](http://echopen.org)

## Topic @needed
[`TPC-medtec`]()

## Licence
All work in this repository is under the licence bsd available [here](https://github.com/echopen/android-app/blob/master/LICENSE.md)

## How to contribute

First you have to follow [Installation Guide](##Installation-(for-Developers)) to build the project.

We references all in-progress or future developpements on cards in [Github Project](https://github.com/echopen/PRJ-medtec_androidapp/projects/1).

You can **pick one card** you are interested to work on and you can also contact [authors](###Authors) to discuss best way to contribute on a task. 
You can also report a bug or ask for new features on [Github issues](https://github.com/echopen/android-app/issues)

We follow the described integration process:

* fork the project on your local profile
* implement the new feature
* create a pull requests on *echopen/master* branch
* pull request is reviewed by contributors
* commit is integrated to echOpen project !

### Authors

* [@nowami](http://github.com/benchoufi)
* [Loïc Fejoz](http://github.com/loic-fejoz)
* [Clement Le Couedic](http://github.com/clecoued)

### Contact

* Twitter [@echopenorg](http://twitter.com/echopenorg)
* Facebook [Echopen](https://www.facebook.com/groups/599174686826294/)
* Email [contact@echopen.org](mailto:contact@echopen.org)

## Installation (for Developers)

### Setup environment
In order to ease the developement and to prevent the contributor from the hassle of spending days configuring. We set up a [Virtual Machine](https://drive.google.com/open?id=0B0V8htWBLPWBVEh6ZEJPcFpmTEU), with all the tools needed. You can find all the informations [here](https://echopen.gitbooks.io/android-app/content/echopens_virtual_machine.html).

You can also configure manually your development environement. 

As an android app, you need to install the following dependencies:

* [java JDK](http://openjdk.java.net/projects/jdk8/).
* [Gradle](http://gradle.org/) as builder tools.
* [Android Studio](https://developer.android.com/studio/index.html) as development IDE.
* **Android SDK** and tools installed via Android Studio

### Setup echOpen mobile application
First you need to clone echOpen android mobile application project.

Before being able to build the app, you have to configure your local android project settings, in *local.properties* file: 
```
	sdk.dir=/Path/To/Your/.../Android/sdk
	ndk.dir=/Path/To/Your/.../android-ndk-r10e
```
Then you can build the project via **Android Studio**, or
you can build the project by executing the Gradle wrapper from the project directory, example:

Mac/Linux
```
	./gradlew assembleDebug
```
Windows
```
	gradlew.bat assembleDebug
```


To debug the mobile application on a device:

* Switch the Android device to dev mode (generally, tap 7 times on "Version number" in parameters)
* Enable USB debugging
* Connect the device to your computer (with screen unlocked to see the prompt)
* Accept the prompt on your device, to authorize your computer
* Launch gradlew with `installDebug`

### Probe emulator

In the file ./probe_emulator, one can find source code and executable of a programe that send data such as the echopen prototype. This program can run on a RedPitaya or on your computer (for linux OS).

For compiling the C code, use the bash file probe_emulator.sh provided. To choose the device on which the program will run, use -m option, where option is RP for RedPitaya and PC for your PC such as:

	sh probe_emulator.sh -m RP

If you chose PC option, the executable file will be located in the ./probe_emulator folder. If you chose RP option, the executable will be send in the RedPitaya (you must connect to Red Pitaya wireless network first). This executable need an option in: void, plate, hand and film:

	./probe_emulator plate

* void option will send on a loop an image fill with only value 8192
* plate option will send on a loop an image of a plate
* hand option will send on a loop an image of the cross-section of a hand
* film will send on a loop an echographic film of the cross-sections of a hand an harm

To execute the program on the RedPitaya you first need to connect to the RedPitaya *via* ssh:

	ssh root@192.168.128.3

pass is root. At connection you will be situated in /root folder where is located the program. Now you can just lanch it (with correct option).

## Documentation 

You can get high-level documentation on our [GitBook](https://echopen.gitbooks.io/android-app/content/) mobile application section.

You have also access to a detailed documentation in the *projectRootDirectory/doc* folder and to a [doxygen](https://en.wikipedia.org/wiki/Doxygen) documentation in *projectRootDirectory/doc/javadoc_app*

## Challenges and To Dos

### Mockups 
  To drive your code, here's the MOCKUP that we have set with medical doctors, engineers and designers.  

  ![alt tag](http://wiki.echopen.org/images/a/ab/Echopen_MockUp_1.png)
  ![alt tag](http://wiki.echopen.org/images/0/07/Echopen_MockUp_1.2.png)
  ![alt tag](http://wiki.echopen.org/images/e/e2/Echopen_MockUp_1_2.png)



