# SmartHospital
The Smart Hospital is an IoT classes scenario created during the IoT classes held at the AGH University of Science and Technology, Cracow.
  
A documentation of this project is available in Polish and included in this repository.

## Setup
### Prerequisites
It is obligatory to install the following Python packages before: `zope.interface-4.1.1 or later`, `Twisted-14.0.2 or later`, `txThings`. It is also necessary to have a `Java 1.8` distribution installed (or an equivalent one with the `JavaFX` library configured) to run the client application.
### Device
This project is created for the [AGH Copernicus device](http://home.agh.edu.pl/~tszydlo/copernicus/).
  
The `serial` module is available directly on the Copernicus device. A simple mock of this module is included in the repository. If you are using the Copernicus device, please remove the `Device/serial.py` file.
### Running
In order to run the CoAP server, type:
```
python server.py
```
After a few seconds the server will start. Then, run the Java client as you wish (a Maven build script included) and connect with the server by typing it's IPv4 address in a text-field at the bottom of the client's GUI window.

## API
This project uses a [Copernicus API](https://github.com/atryda12/copernicus_api) created by @atryda12. Usage of this API is described in a README file of the aforementioned project.

## Californium Plugin
A client-side implementation of the CoAP uses the [org.eclipse.californium plugin](http://www.eclipse.org/californium/). We encourage you also to install the Californium plugin in your web browser (tested on the Firefox) if you would like to send raw CoAP packets (browser with plugin works as a client then).

