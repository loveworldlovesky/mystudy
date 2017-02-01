package cn.zwc.iosocket.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class SocketClient {
	public static void main(String[] args) {
		
		int i = 0;
		try {
			Socket socket = new Socket("127.0.0.1", 8081);
			
//			PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
//			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			OutputStream out = socket.getOutputStream();// 获取服务端的输出流，为了向服务端输出数据
			InputStream in = socket.getInputStream();// 获取服务端的输入流，为了获取服务端输入的数据

			PrintWriter pw = new PrintWriter(out, true);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while(true){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String readLine = br.readLine();
				System.out.println("receved from server:"+readLine);
				pw.println("Hello,I am client."+i++ );
//				pw.write("Hello,I am client."+i++ +"\r\n");
				pw.flush();
				
//				String line = br.readLine();// 读取服务端传来的数据
//				System.out.println("服务端说:" + line);// 打印服务端传来的数据
//				pw.println((new Date()) + ",Hello,I am Client!");// 发送数据给服务端
				
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
}
