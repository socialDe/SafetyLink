package com.controller;

import com.serial.SendAndReceiveSerial;
import com.serial.SendAndReceiveSerialCan;

public class MainController {

	public static void main(String[] args) {
		SendAndReceiveSerial mcu = new SendAndReceiveSerial("COM5", true);
		SendAndReceiveSerialCan can = new SendAndReceiveSerialCan("COM9", true);
		can.setMcu(mcu);
		mcu.setCan(can);
	}

}
