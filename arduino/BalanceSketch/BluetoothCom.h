#ifndef BLUETOOTHCOM_H
#define BLUETOOTHCOM_H
#include <Arduino.h>
#include <HardwareSerial.h>
class BluetoothCom {
public:
  BluetoothCom(HardwareSerial &serialObj, uint16_t baudRate);
  void setup();
  void loop();
  uint16_t getBaudRate() const {return m_baudRate;}
  HardwareSerial* getSerialObj() const {return m_serialObj;}
private:
  uint16_t m_baudRate;
  HardwareSerial* m_serialObj;
  String m_inString;
  void handleCom(char in);
  long long int m_millisOld;
  
};


#endif //BLUETOOTHCOM_H
