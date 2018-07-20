#ifndef SERIALCOM_H
#define SERIALCOM_H
#include <HardwareSerial.h>
#include <Arduino.h>
class SerialCom {
public:
  SerialCom(HardwareSerial &serialObj, uint16_t baudRate);
  void setup();
  void loop();
private:
  HardwareSerial *m_serialObj;
  uint16_t m_baudRate;
  void handleChar(char in);
  void handleCommand(String cmd);
  String m_inString;
};

#endif //SERIALCOM_H
