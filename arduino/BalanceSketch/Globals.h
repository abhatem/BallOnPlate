
#ifndef GLOBALS_H
#define GLOBALS_H
#include <Arduino.h>
#include <Servo.h>

namespace Globals {
  extern void setup();
  extern void loop();
  
  extern int srvYPin;
  extern int srvXPin;
  extern int srvXVal;
  extern int srvYVal;
  extern int ballX;
  extern int ballY;
  extern Servo srvX;
  extern Servo srvY;
  extern bool inputRead;
}


//class _GlobalsDEF {
//  public:
//  int srvYPin = 5;
//  int srvXPin = 6;
//  int srvXVal = 0;
//  int srvYVal = 0;
//  int ballX = 0;
//  int ballY = 0;
//  Servo srvX;
//  Servo srvY;
//
//  void setup() {
//    srvX.attach(srvXPin);
//    srvY.attach(srvYPin);
//  }
//
//  void loop(){}
//}Globals ;

#endif //GLOBALS_H
