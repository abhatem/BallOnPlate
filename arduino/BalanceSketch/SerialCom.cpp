#include "SerialCom.h"
#include "Globals.h"

SerialCom::SerialCom(HardwareSerial &serialObj, uint16_t baudRate) {
  m_serialObj = &serialObj;
  m_baudRate = baudRate;
  m_inString = "";
}

void SerialCom::setup() {
  m_serialObj->begin(m_baudRate);
  
}

void SerialCom::loop() {
  while(m_serialObj->available()) {
    handleChar(m_serialObj->read());
  }
}


void SerialCom::handleChar(char in) {

  if(in != '\n')
    m_inString += (char)in;

  if (in == '\n') {
//    strX = inString.substring(0, inString.indexOf(' '));
//    strY = inString.substring(inString.indexOf(' ')+1);
//    int valX = strX.toInt();
//    int valY = strY.toInt();
//    Globals.ballX = valX;
//    Globals.ballY = valY;
    
    handleCommand(m_inString);
    m_inString = "";
//    m_serialObj.println("");
    
    
  }
}

void SerialCom::handleCommand(String cmd) {
//  Serial.println("hello world");
  if(cmd == "ballX") {
    m_serialObj->println(Globals::ballY);
  }
  else if(cmd == "ballY") {
    m_serialObj->println(Globals::ballX);
  }
  else if(cmd.startsWith("srvX=")) {
    String strVal = cmd.substring(cmd.indexOf('=')+1);
    Globals::srvXVal = strVal.toInt();
    Globals::srvX.write(Globals::srvXVal);
  }
  else if(cmd.startsWith("srvY=")) {
    String strVal = cmd.substring(cmd.indexOf('=')+1);
    Globals::srvYVal = strVal.toInt();
    Globals::srvY.write(Globals::srvYVal);
  }
}

