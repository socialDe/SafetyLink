package com.tcpip;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.HashMap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


import com.df.DataFrame;

public class Server {
	ServerSocket serverSocket;

    Sender sender;
    HashMap<String, ObjectOutputStream> maps = new HashMap<>();
    int serverPort;
    
    // 현재 시간 표시 설정
    SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
    Date time = new Date();
    String timeNow = format.format(time);
    
	public Server() {
		
	}
	public Server(int port) {
		this.serverPort = port;
	}
	
    
    public void startServer() throws Exception{
        serverSocket = new ServerSocket(serverPort);
        System.out.println("Start Server...");



		Runnable r = new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Socket socket = null;
						System.out.println("Server Ready..");
						socket = serverSocket.accept();
						System.out.println("Connected:" + socket.getInetAddress() + " " + timeNow); // 연결된 IP표시
						new Receiver(socket).start();

					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		};
		new Thread(r).start();
	}

	class Receiver extends Thread {
		Socket socket;
		ObjectInputStream oi;
		

        public Receiver(Socket socket) throws IOException {
            this.socket = socket;
            ObjectOutputStream oo;
            oi = new ObjectInputStream(this.socket.getInputStream());
            oo = new ObjectOutputStream(this.socket.getOutputStream());
            
            maps.put(socket.getInetAddress().toString(), oo);
            System.out.println("[Server]"+socket.getInetAddress()+"연결되었습니다.");
        }


		@Override
		public void run() {
			while (oi != null) {
				try {
					DataFrame input = (DataFrame) oi.readObject();
					System.out.println("[DataFrame 수신] " + input.getSender() + ": " + input.getContents());
						
					sendDataFrame(input);
										

				} catch (Exception e) {
					maps.remove(socket.getInetAddress().toString());
                	System.out.println(socket.getInetAddress()+" Exit..."+timeNow);
                	e.printStackTrace();
                	System.out.println(socket.getInetAddress() +" :Receiver 객체 수신 실패 ");

					break;
				}
			} // end while

			try {
				if (oi != null) {
					System.out.println("ObjectInputStream Closed ..");
					oi.close();
				}
				if (socket != null) {
					System.out.println("Socket Closed ..");
					socket.close();
				}
			} catch (Exception e) {
				System.out.println("객체 수신 실패 후 InputStream, socket 닫기 실패");
			}

		}
	}// End Receiver

	public void sendDataFrame(DataFrame df) {
		try {
			sender = new Sender();
			System.out.println("setDataFrame 실행");
			sender.setDataFrame(df);
			System.out.println("객체 송신 Thread 호출");
			sender.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	class Sender extends Thread {
		DataFrame dataFrame;
		Socket socket;
		
		public Sender() {

		}

		public void setDataFrame(DataFrame df) {
			this.dataFrame = df;
			System.out.println("setDataFrame 완료");
		}

		@Override
		public void run() {
			try {
				System.out.println("Sender Thread 실행");
				// dataFrame.setIp("192.168.35.149");
				// dataFrame.setSender("[TabletServer]");
				// Log.d("[Server]", "테스트 목적 Client로 목적지 재설정");

				maps.get("/"+dataFrame.getIp()).writeObject(dataFrame);
                System.out.println("Sender 객체 전송.. "+dataFrame.getIp()+"주소로 "+dataFrame.getContents());
                System.out.println( "Sender 객체 전송 성공");

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}



	public static void main(String[] args) {
		Server server = new Server(5558);
		try {
			server.startServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
