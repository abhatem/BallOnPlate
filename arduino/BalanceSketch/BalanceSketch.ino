
// Implemented on Arduino Mega 2560
// Generally requires boards with support for multiple hardware serial interfaces.

#include "Globals.h"
#include "BluetoothCom.h"
#include "SerialCom.h"
#include <AutoPID.h>

#define OUTPUT_MIN -40
#define OUTPUT_MAX 40


#define KPX 0.12
#define KIX 0
#define KDX 120000
#define KPY 0.10
#define KIY 0
#define KDY 130000



double ballX, setPointX, outputValX;
double ballY, setPointY, outputValY;
AutoPID XPID(&ballX, &setPointX, &outputValX, OUTPUT_MIN, OUTPUT_MAX, KPX, KIX, KDX);
AutoPID YPID(&ballY, &setPointY, &outputValY, OUTPUT_MIN, OUTPUT_MAX, KPY, KIY, KDY);
#define dbg(a) Serial.print(String(#a) + ": ");Serial.println(a);
BluetoothCom bluecom(Serial1, 9600);
SerialCom sercom(Serial, 9600);
void setup() {
  Globals::setup();
  bluecom.setup();
  sercom.setup();
  XPID.setTimeStep(20);
  YPID.setTimeStep(20);
  setPointX = 150;
  setPointY = 130;
}

void loop() {
  Globals::loop();
  bluecom.loop();
  sercom.loop();
  
  if(Globals::inputRead) {
    XPID.run();
    YPID.run();
    Globals::inputRead = false;
  }
  
  Globals::srvX.write(100-outputValX);
  Globals::srvY.write(90+outputValY);
  ballX = Globals::ballX;
  ballY = Globals::ballY;
}
