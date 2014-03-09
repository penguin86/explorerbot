/*
    Copyright 2014 Daniele Verducci
    This file is part of ArduinoExplorerbotSketch.

    ArduinoExplorerbotSketch is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ArduinoExplorerbotSketch is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ArduinoExplorerbotSketch.  If not, see <http://www.gnu.org/licenses/>.
*/


/*
 * PIN
 *  7  Steering servo yellow cable
 *  3  to H-Bridge ENABLE
 *  9  to H-Bridge logic pin 1
 * 10  to H-Bridge logic pin 2
 *
 * SERIAL (9600)
 * 2 bits: the first is the steering from 1 to 127,
 * the second the acceleration.
 */

const int servoPin = 7; 
const int hBridgeEnable = 3;
const int hBridge1pin = 9;
const int hBridge2pin = 10;
 
#include <Servo.h>
Servo steeringServo;
byte gamepadPosition[2];
byte missedUpdates=0;

void setup(){
  steeringServo.attach(servoPin);  //steering servo to pin 7
  pinMode(hBridgeEnable, OUTPUT);
  pinMode(hBridge1pin, OUTPUT);
  pinMode(hBridge2pin, OUTPUT);
  Serial.begin(9600);
}

void loop(){
  if(readGamepadPositionFromSerial()){
    missedUpdates=0;
    //If data ready, drive motors
    steerTo(gamepadPosition[0]);
    accelerateTo(gamepadPosition[1]);
  } else {
    //If data not ready for 5 times (500ms), stop the robot (for security reasons, e.g. the connection may be dropped)
    missedUpdates++;
    if(missedUpdates>5){
      accelerateTo(63);
    }
  }
  delay(100);
}

boolean readGamepadPositionFromSerial(){
  if(Serial.available()>1){   //Bytes received in pairs
    gamepadPosition[0] = Serial.read();
    gamepadPosition[1] = Serial.read();
    return true;
  } 
  return false;
}

void steerTo(byte steeringByte){
  steeringServo.write(steeringByte);  //Angles from 0° to 127°
}

void accelerateTo(byte speedByte){
  if(speedByte<64){
    //Go forward
    digitalWrite(hBridge1pin, HIGH);
    digitalWrite(hBridge2pin, LOW);
    //speedByte = 1/speedByte;
  } else {
    //Go backward
    digitalWrite(hBridge2pin, HIGH);
    digitalWrite(hBridge1pin, LOW);
    //speedByte = speedByte-64;    
  }
  //analogWrite(hBridgeEnable, speedByte*2);
  if(speedByte<47 || speedByte>79) digitalWrite(hBridgeEnable, HIGH);
  else digitalWrite(hBridgeEnable, LOW);
}
