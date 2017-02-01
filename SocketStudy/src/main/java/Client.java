import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class Client {

	public static void main(String[] args) throws Exception {
		runSocket();

	}

	private static void runSocket() throws Exception {
		final String HOST = "127.0.0.1";
		Socket socket = new Socket(HOST, 8081);// 创建一个客户端连接
		OutputStream out = socket.getOutputStream();// 获取服务端的输出流，为了向服务端输出数据
		InputStream in = socket.getInputStream();// 获取服务端的输入流，为了获取服务端输入的数据

		PrintWriter bufw = new PrintWriter(out, true);
		BufferedReader bufr = new BufferedReader(new InputStreamReader(in));
		while (true) {
			Thread.sleep(1000);
			String line = null;
			line = bufr.readLine();// 读取服务端传来的数据
//			if (line == null) {
//				break;
//			}
			System.out.println("服务端说:" + line);// 打印服务端传来的数据
			bufw.println((new Date()) + ",Hello,I am Client!");// 发送数据给服务端
		}
	}

}