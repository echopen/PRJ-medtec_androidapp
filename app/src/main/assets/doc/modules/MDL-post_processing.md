# Module
![]()

## Name
[`MDL-post_processing`]()

## Title
Post Processing Algorithm

## About
BoofCV provides a family of algorithms.

* These need a specific image format ImageType : ImageUInt8, ImageFloat32, ImageFloat32, each of
 which inherit from ImageSingleBand or more parently form some ImageBase (but it is not used not confuse between naming convention,
here BaseImage - not a good but a convenient reason)

* Proper algorithms are called from classes that inherit of BaseProcess. BaseProcess implements some routine methods to check
 image types.
    * Algorithms classes are named accordingly to the algo they implement,
    and within each of them is found an explicit method. For the example, we created `WaveletDenoise` class with its `denoising()` method
    and the `ImageEnhancement` with its `enhancing()` method

This modules takes as inputs
- Android's `ImageView` instance
and outputs
- A Bitmap

**Remark**
All informations about BoofCV tyoe format are available here : http://boofcv.org/index.php?title=Tutorial_Images

## Uses
* [`ITF-post_processing_images`]()

## Functions

