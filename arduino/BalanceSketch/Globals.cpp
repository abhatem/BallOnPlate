#include "Globals.h"
namespace Globals { 

  int srvYPin = 5;
  int srvXPin = 6;
  int srvXVal = 0;
  int srvYVal = 0;
  int ballX = 0;
  int ballY = 0;
  Servo srvX;
  Servo srvY;
  bool inputRead = false;
  void setup(){
    srvX.attach(srvXPin);
    srvY.attach(srvYPin);
  }
  void loop() {
    
  }
  
 
}
