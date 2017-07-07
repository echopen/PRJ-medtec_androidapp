# Module
![]()

## Name
[`MDL-stream_data`]()

## Title
Streaming Data

## About
The app receives data from the hardware through different protocol

* TCP protocol
The main class is `ProcessTCPTask` which extends an `AsyncTask` class. This class listens to a stream represented by an `InputStream stream`

Then 3 operations take place

** `DeepInsidePacket()` that extracts from the stream a chunk of pixels data array which corresponds to a single image frame
** th `ScanConversion` algorithm transforms the data
** the ScanConverted image is sent by `refreshUI()` to the UI thread to be displayed

* UDP protocol
this case is the speedest one but not the more medically usefull because of the inherent data loss

The main class is `ProcessUDPTask` which extends an `AsyncTask` class. This class opens `DatagramSocket` a socket to listen to
the data.

Then 3 operations take place

** the data wrapped by the `DatagramPacket` instance is chunked to extract from the stream a pixels data array, which corresponds to a single image frame
** the `ScanConversion` algorithm transforms the data
** the ScanConverted image is sent by `refreshUI()` to the UI thread to be displayed

## Uses

* [`ITF-tcp_data`]()
* [`ITF-udp_data`]()

## Functions

