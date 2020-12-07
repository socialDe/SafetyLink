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
    
    // �쁽�옱 �떆媛� �몴�떆 �꽕�젙
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
						System.out.println(serverSocket.toString());
						socket = serverSocket.accept();
						System.out.println("Connected:" + socket.getInetAddress() + " " + timeNow); // �뿰寃곕맂 IP�몴�떆
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
            System.out.println("[Server]"+socket.getInetAddress()+"�뿰寃곕릺�뿀�뒿�땲�떎.");
        }


		@Override
		public void run() {
			while (oi != null) {
				try {
					DataFrame input = (DataFrame) oi.readObject();
					System.out.println("[DataFrame �닔�떊] " + input.getSender() + ": " + input.getContents());
						
					//sendDataFrame(input);
										

				} catch (Exception e) {
					maps.remove(socket.getInetAddress().toString());
                	System.out.println(socket.getInetAddress()+" Exit..."+timeNow);
                	e.printStackTrace();
                	System.out.println(socket.getInetAddress() +" :Receiver 媛앹껜 �닔�떊 �떎�뙣 ");

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
				System.out.println("媛앹껜 �닔�떊 �떎�뙣 �썑 InputStream, socket �떕湲� �떎�뙣");
			}

		}
	}// End Receiver

	public void sendDataFrame(DataFrame df) {
		try {
			sender = new Sender();
			System.out.println("setDataFrame �떎�뻾");
			sender.setDataFrame(df);
			System.out.println("媛앹껜 �넚�떊 Thread �샇異�");
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
			System.out.println("setDataFrame �셿猷�");
		}

		@Override
		public void run() {
			try {
				System.out.println("Sender Thread �떎�뻾");
				// dataFrame.setIp("192.168.35.149");
				// dataFrame.setSender("[TabletServer]");
				// Log.d("[Server]", "�뀒�뒪�듃 紐⑹쟻 Client濡� 紐⑹쟻吏� �옱�꽕�젙");

				maps.get("/"+dataFrame.getIp()).writeObject(dataFrame);
                System.out.println("Sender 媛앹껜 �쟾�넚.. "+dataFrame.getIp()+"二쇱냼濡� "+dataFrame.getContents());
                System.out.println( "Sender 媛앹껜 �쟾�넚 �꽦怨�");

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
