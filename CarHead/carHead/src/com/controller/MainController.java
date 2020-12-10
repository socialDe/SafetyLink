package com.controller;

import java.io.IOException;

import com.can.SendAndReceiveSerialCan;
import com.tcpip.Client;

public class MainController {

	public static void main(String[] args) {
		SendAndReceiveSerialCan can = new SendAndReceiveSerialCan("COM6", true);
		Client client = new Client("192.168.0.152", 5558);
		can.setClient(client);
		client.setCan(can);
		
		try {
			client.connect();
			//client.sendData();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
