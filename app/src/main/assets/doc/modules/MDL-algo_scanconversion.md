# Module
![]()

## Name
[`MDL-algo_scanconversion`]()

## Title
ScanConversion Algorithm

## About
This modules is triggered through
- the `getDataFromInterpolation()` method

the main ScanConversion algorihtms are
- `opencv_interpolation()`
- `compute_interpolation()`

ScanConversion class exposes two algorithms whether

- the data protocol is set to `UDP` or `CP`
 -- then `getDataFromInterpolation()` runs `opencv_interpolation()` method
 -- then `getDataFromInterpolation()` runs `compute_interpolation()` method



and outputs
- a pixels int array -> thrown by opencv_interpolation() or by compute_interpolation()

## Uses
* [`ITF-ultrasound_constants`]()
* [`ITF-ultrasound_data`]()

## Functions
* [`FCT-`]()
