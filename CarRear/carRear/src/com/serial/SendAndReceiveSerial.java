package com.serial;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.Scanner;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SendAndReceiveSerial implements SerialPortEventListener {
	private BufferedInputStream bin;
	private InputStream in;
	private OutputStream out;
	private SerialPort serialPort;
	private CommPortIdentifier portIdentifier;
	private CommPort commPort;
	private String result;
	private String rawCanID, rawTotal;
	SendAndReceiveSerialCan can;
	// private boolean start = false;

	private int isRunOrStop = 0;

	public void setCan(SendAndReceiveSerialCan can) {
		this.can = can;
	}
	
	public SendAndReceiveSerial(String portName, boolean mode) {
		try {
			if (mode == true) {
				portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
				System.out.printf("Port Connect : %s\n", portName);
				connectSerial();
				// Serial Initialization ....
				(new Thread(new SerialWriter())).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void connectSerial() throws Exception {
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			// CarFront - Arduino: 시리얼 통신 포트 5002
			commPort = portIdentifier.open(this.getClass().getName(), 5002);
			if (commPort instanceof SerialPort) {
				serialPort = (SerialPort) commPort;
				serialPort.addEventListener(this);
				serialPort.notifyOnDataAvailable(true);
				serialPort.setSerialPortParams(921600, // 통신속도
						SerialPort.DATABITS_8, // 데이터 비트
						SerialPort.STOPBITS_1, // stop 비트
						SerialPort.PARITY_NONE); // 패리티
				in = serialPort.getInputStream();
				bin = new BufferedInputStream(in);
				out = serialPort.getOutputStream();
			} else {
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	public void sendSerial(String rawTotal, String rawCanID) {
		this.rawTotal = rawTotal;
		this.rawCanID = rawCanID;
		// System.out.println("send: " + rawTotal);
		try {
			// Thread.sleep(50);
			Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Thread sendTread = new Thread(new SerialWriter(rawTotal));
		sendTread.start();
	}
	
	public void checkserial(String data) {
		String receiveID = "AD01";
		String sensorID = data.substring(0, 4);
		String sensorValue = data.substring(4);
		String sensorData = "";
		
		
		
		// data: Arduino에서 보낸 값
		// check data
		if(sensorID.equals("0001")) {
			// 온도센서
			// send data(Sensor+value) can
			double value = Double.parseDouble(sensorValue);
			if(value < 0) {
				sensorData = "1" + String.format("%07d", (int)(value * (-100)));
			}else {
				sensorData = String.format("%08d", (int)(value * 100));
			}
		}else if(sensorID.equals("0005") && Double.parseDouble(sensorValue) == 9999.0) {
			// 주행을 시작함을 알려주는 플래그 셋팅
			System.out.println("주행 전환, Flag Setting");
			isRunOrStop = 0;
		}else if(sensorID.equals("0005")) {
			// 무게 센서

			double value = Double.parseDouble(sensorValue);
			
			// 주행 시작의 경우 반드시 한 번만 시행
			if(isRunOrStop == 0){
				sensorData = String.format("%07d", (int)(value*10));
				sensorData = "9"+sensorData;
				isRunOrStop ++; // 시작 데이터 여부 flag (0 -> 시작데이터, 1 -> Non시작데이터)
			}else{
				sensorData = String.format("%08d", (int)(value * 10));
			}
		}

		System.out.println("sendSerial-can: " + receiveID + sensorID + sensorData);
		can.sendSerial("W280420CA03" + receiveID + sensorID + sensorData, "0420CA03");
	}
	
	private class SerialWriter implements Runnable {
		String data;

		public SerialWriter() {
			// can protocol에 참여, 고정값
			// :canmsg\r
			this.data = ":G11A9\r";
		}

		public SerialWriter(String serialData) {
			// CheckSum Data create
			this.data = sendDataFormat(serialData);
		}

		public String sendDataFormat(String serialData) {
			serialData = serialData.toUpperCase();
			char c[] = serialData.toCharArray();
			int cdata = 0;
			for (char cc : c) {
				cdata += cc;
			}
			cdata = (cdata & 0xFF);

			String returnData = ":";
			returnData += serialData + Integer.toHexString(cdata).toUpperCase();
			returnData += "\r";
			return returnData;
		}

		public void run() {
			try {

				byte[] inputData = data.getBytes();

				out.write(inputData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// Asynchronized Receive Data
	// --------------------------------------------------------
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			byte[] readBuffer = new byte[128];

			try {

				while (bin.available() > 0) {
					int numBytes = bin.read(readBuffer);
				}

				String ss = new String(readBuffer);
				System.out.println("Receive Low Data:" + ss + "||");

				checkserial(ss);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	public void close() throws IOException {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (in != null) {
			in.close();
		}
		if (out != null) {
			out.close();
		}
		if (commPort != null) {
			commPort.close();
		}

	}

	public void sendIoT(String cmd) {
		Thread t1 = new Thread (new sendIoT(cmd));
		t1.start();
	}
	
	class sendIoT implements Runnable{

		String cmd;
		public sendIoT(String cmd) {
			this.cmd = cmd;
		}

		@Override
		public void run() {
		     byte[]datas=cmd.getBytes();
		     try {
				out.write(datas);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public static void main(String args[]) throws IOException {

		SendAndReceiveSerial ss = new SendAndReceiveSerial("COM5", true);
		
		Scanner scan = new Scanner(System.in);
		while(true) {
			String str = scan.nextLine();
			ss.sendIoT(str);
		}
		//ss.sendSerial("W2810003B010000000000005011", "10"+ "003B01");
		//ss.sendIoT("s");
		//ss.close();
	}
}


