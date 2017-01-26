# Interface

## Name
[`ITF-ultrasound_constants`]()

## Title
Ultrasound Constants

## About
The reader should take care of the constants being different according to the ScanConversion algorithms in use.

## Format
Constants format are critical for the algorithms, mainly the [`MDL-algo_scanconversion.md`](), ScanConversion algorithm,

* Either it is the native OpenCV ScanConversion implementation then the critical constants are

** largest number of pixels in x-direction
** largest number of pixels in z-direction
** the number of lines scaned by the rotating probe (each line correspond to specific position of the transducer)
** number of data sample per line

* Either it is the echOpen's handmade ScanConversion implementation then the critical constants are

** largest number of pixels in x-direction
** largest number of pixels in z-direction
** speed of sound in m.s^{—1}
** sampling frequency in Hz
** the number of lines parsed by the rotating probe
** image size
** image width in rad
** number of data sample per line
** angle drawn between two lines, in rad
** distance between two data sample on each line, in m (meters)
** depth for start of data in m
