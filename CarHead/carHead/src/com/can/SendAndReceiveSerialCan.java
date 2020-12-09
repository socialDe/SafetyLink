package com.can;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import com.df.DataFrame;
import com.tcpip.Client;

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
	// private boolean start = false;
	private Client client;
	
	public void setClient(Client client) {
		this.client = client;
	}

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
			// CarHead - CarRear: 캔 통신 포트 5001
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

	// can으로 받은 데이터를 가공하여 Client.sendData로 전달
	public void checkcode(String can) {
		String code = can.substring(0, 1); // 명령 코드
		String type = can.substring(1, 3); // 데이터 특성 코드
		String sendId = can.substring(3, 11); // 송신 ID (can id)
//		String receiveID = can.substring(11, 15); // 수신 ID
		String data = can.substring(11); // 데이터 정보: 수신 ID + SensorID + SensorData
		
		DataFrame df = new DataFrame();
		if(sendId.equals("0420CA01")) {
			df.setSender("CarHead");
		}else if(sendId.equals("0420CA02")) {
			df.setSender("CarFront");
		}else if(sendId.equals("0420CA03")) {
			df.setSender("CarRear");
		}else if(sendId.equals("0420AD01")) {
			df.setSender("CustomerTablet");
		}else if(sendId.equals("0420AD02")) {
			df.setSender("CustomerMobile");
		}
		
		if (code.equals("U")) {
			df.setContents(data);
			client.sendData(df);
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
			// CheckSum Data 생성
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

				result = ss.substring(1, 28);
				checkcode(result);

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
		SendAndReceiveSerialCan can = new SendAndReceiveSerialCan("COM6", true);
		Scanner scan = new Scanner(System.in);
//		can.sendSerial("W2810003B010000000000005011", "10003B01");
		
		while (true) {	
			String str = scan.nextLine();
			
			if (str.equals("q")) {
				break;
			}
			can.sendSerial(str, str.substring(0, 4));
		}

		 scan.close();
	}
}