# Module
![](viewme.jpg)

## Name
[`MDL-algo_scanconversion.md`]()

## Title
ScanConversion Algorithm

## Description
This modules is triggered through
- the `getDataFromInterpolation()` method

the main ScanConversion algorihtms are
- `opencv_interpolation()`
- `compute_interpolation()`

ScanConversion class exposes two algorithms whether

- the data protocol is set to `UDP` or `CP`
 -- then `etDataFromInterpolation()` runs `opencv_interpolation()` method
 -- then `getDataFromInterpolation()` runs `compute_interpolation()` method



and outputs
- a pixels int array -> thrown by opencv_interpolation() or by compute_interpolation()

## Uses
[`ITF-mockups`](../interfaces/ITF-ultrasound_constants.md)
[`ITF-psd_templates`](../interfaces/ITF-ultrasound_data.md)

## Functions

