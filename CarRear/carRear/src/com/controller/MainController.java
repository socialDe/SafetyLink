package com.controller;

import com.serial.SendAndReceiveSerialCan;
import com.serial.SendAndReceiveSerial;

public class MainController {

	public static void main(String[] args) {
		SendAndReceiveSerial mcu = new SendAndReceiveSerial("COM5", true);
		SendAndReceiveSerialCan can = new SendAndReceiveSerialCan("COM3", true);
		can.setMcu(mcu);
		mcu.setCan(can);
	}

}
