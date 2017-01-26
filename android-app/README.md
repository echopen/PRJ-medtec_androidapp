[![Build Status](https://travis-ci.org/benchoufi/android-app.svg?branch=master)](https://travis-ci.org/benchoufi/android-app)

# Project
![](viewme.jpg)

## Name
[`PRJ-medtec_androidapp`]()

## Title
echOpen Android application

## About
This android app aims to show medical image from an echopen (or other ?)
ultrasound device. See more about this project on [echopen's
wiki](http://echopen.org)

## Topic @needed
[`TPC-medtec`]()

## Licence
All work in this repository is under the licence bsd available [here](https://github.com/echopen/android-app/blob/master/LICENSE.md)

## Documentation
**Please see [README-OPENCV.md](README-OPENCV.md) for additional information on this branch.**


## Installation (for Developers)

As an android app, you need to have a java JDK installed.
We use [Gradle](http://gradle.org/) as builder tools. You need to install it
too.
Before being able to build the app, you need to specify, in a local.properties file, in the root folder, the Android SDK and NDK location, with sdk.dir and ndk.dir, example:

	sdk.dir=/Path/To/Your/.../Android/sdk
	ndk.dir=/Path/To/Your/.../android-ndk-r10e

After, you can launch the Gradle wrapper from the project directory, example:

Mac/Linux
	
	./gradlew assembleDebug
	
Windows

	gradlew.bat assembleDebug
	

To install on a device:

* Switch the Android device to dev mode (generally, tap 7 times on "Version number" in parameters)
* Enable USB debugging
* Connect the device to your computer (with screen unlocked to see the prompt)
* Accept the prompt on your device, to authorize your computer
* Launch gradlew with `installDebug`

Note: It seems that we use android-sdk 19 but current android-sdk version is 24
    now...

## How to contribute

Report a bug, ask for features, for all of this, we use [Github issues](https://github.com/echopen/android-app/issues)

You can clone the projet, add something or fix a bug and make a pull request. We
try to read them quick as possible.

## Authors

* [@nowami](http://github.com/benchoufi)
* [Loïc Fejoz](http://github.com/loic-fejoz)
* [Yannick](http://github.com/yaf)

## Contact

* Twitter [@echopenorg](http://twitter.com/echopenorg)
* Facebook [Echopen](https://www.facebook.com/groups/599174686826294/)
* Email [contact@echopen.org](mailto:contact@echopen.org)


## Challenges and To Dos

### Mockups 
  To drive your code, here's the MOCKUP that we have set with medical doctors, engineers and designers.  

  ![alt tag](http://wiki.echopen.org/images/a/ab/Echopen_MockUp_1.png)
  ![alt tag](http://wiki.echopen.org/images/0/07/Echopen_MockUp_1.2.png)
  ![alt tag](http://wiki.echopen.org/images/e/e2/Echopen_MockUp_1_2.png)

### Testing Data

  A lots of tools enable you to test and play with the data : 
  
  
  *Getting real data*
  
  The app let you get data via `TCP` or `UDP` protocol. By default, the system works with `TCP` protocol. To test `UDP` data you can switch to `redpitaya_udp_data` branch. 
  @todo : create a developer mode, first screen app will then let you change the protocol and set up the rights hardware constants (number of lines, number of samples...).
  Constants are set in the `Constants` class in the `utils` package. The critical ones are : `PreProcParam.NUM_LINES` and `PreProcParam.NUM_SAMPLES`, that corresponds to the number of lines and the number of samples the hardware is acquiring.
  
  If you want to simulate TCP data transfer, you can put in the `doInBackground` method of the `ProcessTCPTask` some lines as : 
  
  `while(true){
	          int num_lines = Constants.PreProcParam.NUM_LINES;
			  int num_samples = Constants.PreProcParam.NUM_SAMPLES;
              byte[] message = new byte[num_lines * num_samples];
              
			  for (int i = 0; i < num_lines; i++) {
                  for (int j = 0; j < num_samples; j++) {
                          message[i*rows +j] = (byte) /* the pixel data you want from à to 255 */;
                  }
              }
              ScanConversion scnConv = ScanConversion.getInstance(message);
              scnConv0.setTcpData();
              refreshUI(scnConv);
          }` 
	don't forget to comment the piece of code dealing with sockets ! 	  
  
  *Simulating real data*
  
  You'll find in the [kit-soft](https://github.com/echopen/kit-soft/) repo, you'll find some real data file [raw_data.txt](https://github.com/echopen/kit-soft/tree/master/data) that was output by our hardware suite.
  
  In this repo, you'll find all the tools to install in local a `UDP` server that can simulate a real time data coming from our hardware.    
  
  *Simulating fake but medical data*

  You'll find in `phantom/img_kydney.txt` and `phantom/img_obs.txt` the data that is output from the Hardware. The goal is to build and obtain from each of these double arrays the images stored respectively as `phantom/img_kydney.bmp` and `phantom/img_obs.bmp`.

  The image `img_kydney.bmp` is more realistic than `img_obs.bmp`.

  ![alt tag](http://echopen.org/images/e/e1/Image_kydney.png)
  ![alt tag](http://echopen.org/images/0/0a/Image_obs.png)
    
   *Simulating with a plain `JAVA` app*
   
   In the `phantom/test_data` directory, you'll find a plain `JAVA` app that lets you test and taste the data without the hassle of bothering with `Android` configuration and other heaviness. 


  `phantom/img_kydney.txt` and `phantom/img_obs.txt` corresponds to the raw signal that needs to be filtered. They serve as basis for the envelope detection from which pixels intensity are deduced. ** The resulting pixel file sits in `data_kydney.csv`**

   In the phantom folder, there are some java classes that are meant to ran outside of android. This is useful for testing and improving the , without the hassle of running an android emulator.  
   *remark* the `PostProcessing` class relies on `BoofCV` library. The `jars` can be found [here](http://sourceforge.net/projects/boofcv/?source=typ_redirect). 



### Image Processing *this is exploratory*

  EchOpen will rely on `BoofCV` library as main image processing tool. It is pure `JAVA` libraries, contrary to the well-known `OpenCV`, which is `c/c++` library. 

  As specified in the `BoofCV` doc, one have to turn the images - say for instance bitmap image - in `ImageUInt8` or `ImageFloat32` class in order to use simply `BoofCV`

  ```
  File image = new File($ultrasound_file);
  Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
  ImageUInt8 boofcv_image = ConvertBitmap.bitmapToGray(bitmap, (ImageUInt8) null, null);
  ```

  for those interested, here's a [link](http://boofcv.org/index.php?title=Performance:OpenCV:BoofCV) to find OpenCV vs. BoofCV performance benchmark. As said there, `OpenCV` seems better on low level operations but `BoofCV` seems better on most of high level operations. 

  The client does not worry about `BootCV` Images class, they are instanciated from `BaseImage`. Then client throws to image processor class an `ImageView` and gets backs a `BitMap` class. Any filter must inherit from `BaseProcess`. For instance, the app implements this kind of code corresponding to a wavelette denoising treatment. 

  ```
  WaveletDenoise waveletDenoise = new WaveletDenoise(image);
  waveletDenoise.denoise();
  bitmap = waveletDenoise.getBitmap();
  ```

  Processing can take a long time. It is recommended to wrap it in an `AsyncTask` !  
  
## Documentation and Issues

  Documentation and issue about echopen -- Perhaps some duplication with http://echopen.org/

  See [issues](https://github.com/echopenorg/project/issues)
  
# DevBox 

In order to ease the developement and to prevent the contributor from the hassle of smecindg days configuring. We set up a [Virtual Machine](https://drive.google.com/open?id=0B0V8htWBLPWBVEh6ZEJPcFpmTEU), with all the tools needed. You can find all the informations [here](https://echopen.gitbooks.io/android-app/content/echopens_virtual_machine.html).

# More informations

You can get more detailed information on our [GitBook](https://echopen.gitbooks.io/android-app/content/)


