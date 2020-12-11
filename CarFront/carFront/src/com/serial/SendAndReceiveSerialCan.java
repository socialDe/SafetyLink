package com.serial;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SendAndReceiveSerialCan implements SerialPortEventListener {
	private BufferedInputStream bin;
	private InputStream in;
	private OutputStream out;
	private SerialPort serialPort;
	private CommPortIdentifier portIdentifier;
	private CommPort commPort;
	private String result;
	private String rawCanID, rawTotal;
	SendAndReceiveSerial mcu;
	// private boolean start = false;

	public void setMcu(SendAndReceiveSerial mcu) {
		this.mcu = mcu;
	}

	// Serial-Can 통신
	public SendAndReceiveSerialCan(String portName, boolean mode) {
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
			// CarHead - CarFront - CarRear: 캔 통신 포트 5001
			commPort = portIdentifier.open(this.getClass().getName(), 5001);
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

	public void checkCan(String data) {
		String receiveTotal = data.substring(1, 28);
		String sendID = data.substring(4, 12);
		String receiveID = data.substring(12, 16);
		String sensorID = data.substring(16, 20);
		String sensorData = data.substring(20, 28);

		// can 데이터를 확인, 수신 기기가 차량 전체(CA00)이거나 carFront(CA02)인 경우
		if (receiveID.equals("CA00") || receiveID.equals("CA02") || receiveID.equals("AA00")) {
			mcu.sendIoT(sensorID + sensorData);
		}
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

				checkCan(ss);
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

	public static void main(String args[]) throws IOException {
		SendAndReceiveSerial mcu = new SendAndReceiveSerial("COM5", true);
		SendAndReceiveSerialCan ss = new SendAndReceiveSerialCan("COM9", true);
	}

}
