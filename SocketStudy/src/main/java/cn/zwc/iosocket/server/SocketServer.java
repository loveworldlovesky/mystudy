package cn.zwc.iosocket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	
	public static void main(String[] args) {
		
		try {
			ServerSocket server = new ServerSocket(8081);
			//×èÈû,µÈ´ýÇëÇó
			Socket socket = server.accept();
			InputStream is = socket.getInputStream();
			BufferedReader br=new BufferedReader(new InputStreamReader(is));  
			PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
			while(true){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				pw.println("that is ok");
				String readLine = br.readLine();
				System.out.println("receved from client:"+readLine);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
