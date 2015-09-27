# Echopen Android App

This android app aims to show medical image from an echopen (or other ?)
ultrasound device. See more about this project on [echopen's
wiki](http://echopen.org)


## Licence


## Documentation


## Installation (for Developers)


## How to contribute

Report a bug, ask for features, for all of this, we use [Github issues](https://github.com/echopen/android-app/issues)

You can clone the projet, add something or fix a bug and make a pull request. We
try to read them quick as possible.

## Authors

* benchoufi33
* loic-fejoz

## Contact


## Challenges and To Dos

### Mockups 
  To drive your code, here's the MOCKUP that we have set with medical doctors, engineers and designers.  

  ![alt tag](http://echopen.org/images/a/ab/Echopen_MockUp_1.png)
  ![alt tag](http://echopen.org/images/0/07/Echopen_MockUp_1.2.png)
  ![alt tag](http://echopen.org/images/e/e2/Echopen_MockUp_1_2.png)

### Simulation

  You'll find in `phantom/img_kydney.txt` and `phantom/img_obs.txt` the data that is output from the Hardware. The goal is to build and obtain from each of these double arrays the images stored respectively as `phantom/img_kydney.bmp` and `phantom/img_obs.bmp`.

  The image `img_kydney.bmp` is more realistic than `img_obs.bmp`.

  ![alt tag](http://echopen.org/images/e/e1/Image_kydney.png)
  ![alt tag](http://echopen.org/images/0/0a/Image_obs.png)


  `phantom/img_kydney.txt` and `phantom/img_obs.txt` corresponds to the raw signal that needs to be filtered. They serve as basis for the envelope detection from which pixels intensity are deduced. ** The resulting pixel file sits in `data_kydney.csv`**


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


