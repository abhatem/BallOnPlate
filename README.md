# Ball on Plate
A low cost ball and plate system that is built from scratch, a cellphone's camera is used for detecting the position of the ball. Video at https://youtu.be/eSXcqw_qibo

I used an An Arduino Mega 2560 board for controlling 2 Servos (MG996R), one for each axis.

The position of the ball is detected using an android app that takes the center of the bounding box of any green object in the image.

The position is then sent to the arduino using bluetooth.

The system is controlled using 2 PID controllers implemented on the arduino, one for each axis.

The Android code depends on OpenCV for Android and the Bluetooth Library from Omar Aflak
https://github.com/OmarAflak/Bluetooth-Library

The Arduino code depends on the AutoPID library you can install it using the Library Manager inside the Arduino IDE (Sketch -> Include Library -> Manage Libraries. Then type AutoPID)
