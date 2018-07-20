#include "BluetoothCom.h"
#include "Globals.h"
#define dbg(a) Serial.print(String(#a) + ": ");Serial.println(a);
BluetoothCom::BluetoothCom(HardwareSerial &serialObj, uint16_t baudRate){
  
  m_serialObj = &serialObj;
  m_baudRate = baudRate;
  m_inString = "";
  m_millisOld = 0;
}

void BluetoothCom::setup() {
  m_serialObj->begin(m_baudRate);
}

void BluetoothCom::loop() {
  while(m_serialObj->available()) {
    handleCom(m_serialObj->read());
  }
  if(millis() - m_millisOld > 100) {
    m_serialObj->println("g");
    m_millisOld = millis();
//    dbg("sending g");
  }
}

void BluetoothCom::handleCom(char in) {

//  dbg("hello");
  if(in != '\n')
    m_inString += (char)in;
    
  if (in == '\n') {
    String strX = m_inString.substring(0, m_inString.indexOf(' '));
    String strY = m_inString.substring(m_inString.indexOf(' ')+1);
    int valX = strX.toInt();
    int valY = strY.toInt();
    Globals::ballY = valX;
    Globals::ballX = valY;
    m_inString = "";
    Globals::inputRead = true;
//    dbg("read input");
    
  }
}

