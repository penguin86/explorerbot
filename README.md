Explorerbot
=========

Explorerbot is a simple robot driven by Arduino through Java and an analog gamepad

Setup
---
  - Write the Arduino firmware to an Arduino board
  - Connect a servomotor to pin 7
  - Connect an H-Bridge-driven motor to pins 7(hbridge enable), 10(motor+) and 9(motor-)

Use
---
  - Start the server in a computer (ExplorerBot java application) or android mobile phone (AndroidExplorerbotServer app) with an Arduino connected via USB (USB-OTG for the phone).
```sh
java -jar explorerbot.jar server
```
  - Start the client in a computer with an analog USB gamepad connected, providing the server's IP address
```sh
java -jar explorerbot.jar client 192.168.0.23
```
  - Drive the robot through your gamepad!


Version
----

1.0

Thanks
-----------

Thanks to Games-Core for the [JInput gamepad polling library](https://java.net/projects/jinput)

Thanks scream3r for the [jssc java serial library](https://code.google.com/p/java-simple-serial-connector/)

Thanks mik3y for the [usb-serial-for-android library](https://github.com/mik3y/usb-serial-for-android)


License
----

ExplorerBot and AndroidExplorerbotServer are released under GPL v3 Library

The JInput library is released under the BSD Licence and cannot be considered part of this project, it is an indipendent library with an indipendent licence. The JInput sources are not included in this project, the library is included in binary form.

The jssc library is released under the LGPL Licence and cannot be considered part of this project.

The usb-serial-for-android library is released under the LGPL Licence and cannot be considered part of this project.

