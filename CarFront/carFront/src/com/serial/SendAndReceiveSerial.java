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
		String receiveID = data.substring(0, 4);
		String sensorID = data.substring(4, 8);
		String sensorValue = data.substring(8);
		String sensorData = "";

		// sensorValue: Arduino에서 보낸 값
		// 센서값을 데이터프레임 문자열에 맞게 변환
		if (sensorID.equals("0001")) {
			// 온도 센서
			double value = Double.parseDouble(sensorValue);
			if (value < 10) {
				// 정수가 한자리
				sensorData = "00000" + (int) (value * 100);
			} else if (value < 100) {
				// 정수가 두자리
				sensorData = "0000" + (int) (value * 100);
			} else {
				// 정수가 세자리
				sensorData = "000" + (int) (value * 100);
			}
		} else if (sensorID.equals("0002")) {
			// 충돌 센서
			// 기본값 1023, 충돌시 숫자가 작게, 큰 충격의 경우 0
			int value = Integer.parseInt(sensorValue);
			if (value < 10) {
				// 정수가 한자리
				sensorData = "0000000" + value;
			} else if (value < 100) {
				// 정수가 두자리
				sensorData = "0000" + (int) (value * 100);
			}
		} else if (sensorID.equals("0003")) {
			// 진동 센서

		} else if (sensorID.equals("0004")) {
			// 적외선 센서

		} else if (sensorID.equals("0005")) {
			// 무게 센서

		} else if (sensorID.equals("0006")) {
			// 가상데이터 - 심박 센서

		} else if (sensorID.equals("0007")) {
			// 가상데이터 - 연료

		} else if (sensorID.equals("0011")) {
			// 버튼 (주행)

		} else if (sensorID.equals("0012")) {
			// 버튼 (시동)

		} else if (sensorID.equals("0021")) {
			// 서보모터 (에어컨)

		} else if (sensorID.equals("0022")) {
			// 서보모터 (히터)

		} else if (sensorID.equals("0031")) {
			// LED (시동 상태)
			// 00000000: LED off(LOW)
			// 00000001: LED on(HIGH)
			if (sensorValue.equals("0")) {
				sensorData = "00000000";
			} else if (sensorValue.equals("1")) {
				sensorData = "00000001";
			}
		} else if (sensorID.equals("0032")) {
			// LED (도어 상태)
			// 00000000: LED off(LOW)
			// 00000001: LED on(HIGH)
			if (sensorValue.equals("0")) {
				sensorData = "00000000";
			} else if (sensorValue.equals("1")) {
				sensorData = "00000001";
			}
		} else if (sensorID.equals("0033")) {
			// LED (주행 상태)
			// 00000000: LED off(LOW)
			// 00000001: LED on(HIGH)
			if (sensorValue.equals("0")) {
				sensorData = "00000000";
			} else if (sensorValue.equals("1")) {
				sensorData = "00000001";
			}
		}

		System.out.println("send can: " + receiveID + sensorID + sensorData);
		can.sendSerial("W280420CA02" + receiveID + sensorID + sensorData, "0420CA02");
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

	public void sendArduino(String cmd) {
		Thread t1 = new Thread(new sendArduino(cmd));
		t1.start();
	}

	class sendArduino implements Runnable {

		String cmd;

		public sendArduino(String cmd) {
			this.cmd = cmd;
		}

		@Override
		public void run() {
			byte[] datas = cmd.getBytes();
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
		while (true) {
			String str = scan.nextLine();
			ss.sendArduino(str);
		}
		// ss.sendSerial("W2810003B010000000000005011", "10"+ "003B01");
		// ss.sendArduino("s");
		// ss.close();
	}
}
