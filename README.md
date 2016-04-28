# SmartHospital
Smart Hospital is an IoT scenario created during IoT classes at AGH University of Science and Technology in Krakow.

Documentation of this project is available in polish and included in this repository.

# Setup
## Prerequisites
It is obligatory to install the following python packages before: zope.interface-4.1.1 or later, Twisted-14.0.2 or later, txThings. To run client application, it is necessary to have Java 1.8 distribution or equivalent with JavaFX configured.
## Device
This project is created for AGH Copernicus device. More info: http://home.agh.edu.pl/~tszydlo/copernicus/.  
Module serial is available directly on Copernicus device. Simple mock of this module is included in this repository. If you are using Copernicus, please remove Device/serial.py file.
## Running
In order to run CoAP server, type:
```
python server.py
```
  
After a few seconds server will start. Then run Java client as you wish (Maven included) and connect with server by typing it's IPv4 address in a text-field at the bottom of client's GUI window.

# API
This project uses Copernicus API created by atryda12. Source code is available here: https://github.com/atryda12/copernicus_api. Usage of this API is described in a Readme of linked project.

#Californium Plugin
Client-side implementation of CoAP uses org.eclipse.californium plugin. More info: http://www.eclipse.org/californium/. We encourage you also to install Californium plugin in your web browser (tested of Firefox) if you would like to send raw CoAP packets (browser with plugin works as a client).
