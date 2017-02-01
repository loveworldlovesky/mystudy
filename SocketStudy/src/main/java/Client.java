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
		Socket socket = new Socket(HOST, 8081);// ����һ���ͻ�������
		OutputStream out = socket.getOutputStream();// ��ȡ����˵��������Ϊ���������������
		InputStream in = socket.getInputStream();// ��ȡ����˵���������Ϊ�˻�ȡ��������������

		PrintWriter bufw = new PrintWriter(out, true);
		BufferedReader bufr = new BufferedReader(new InputStreamReader(in));
		while (true) {
			Thread.sleep(1000);
			String line = null;
			line = bufr.readLine();// ��ȡ����˴���������
//			if (line == null) {
//				break;
//			}
			System.out.println("�����˵:" + line);// ��ӡ����˴���������
			bufw.println((new Date()) + ",Hello,I am Client!");// �������ݸ������
		}
	}

}