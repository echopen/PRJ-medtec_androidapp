# Interface

## Name
[`ITF-ultrasound_data`]()

## Title
Ultrasound Data format

## About
Data are available for the algorithm processes from 3 incoming ways

- either they are transmitted through `TCP` protocol
- either they are transmitted through `UDP` protocol
- either they are transmitted from a `CSV` file from inside the app

At the current state, data are transmitted image by image. So that, each time an int array of
`number_of_pixels_of_an_image` is filled, then it is transmitted to the algorithms pipeline, here : [`MDL-algo_scanconversion.md`]()

## Format
* First part is classical CSV __note that the first item contains the position of the line, in arbitrary unit__
* Second part is comments (starting with #).

In comments can be found meta data, storin for example the following items

* `#timestamp:XX` for the timestamp when the image was taken
* `#lines:XX` contains the number of lines in the image
* `#length:XX` contains the number of pixels per line
* `#frequency:XX` is the frequency of acquisition
* `#piezofrequency:XX` is the piezo frequency
* `#angle:XX` is the angle of the acquisition
* `#anglestep:XX` is the multiplicator between the first item of each line to obtain the position in degree.

For more informations, we refer the reader to this wiki ![page](http://wiki.echopen.org/index.php/Challenge:_Data_format#Solution:_Common_rules_for_structuring_raw_data)

## Description
Data are available for the algorithm processes from 3 incoming ways

- either they are transmitted through `TCP` protocol
- either they are transmitted through `UDP` protocol
- either they are transmitted from a `CSV` file from inside the app

At the current state, data are transmitted image by image. So that, each time an int array of
`number_of_pixels_of_an_image` is filled, then it is transmitted to the algorithms pipeline, here : [`MDL-algo_scanconversion.md`]()