import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		runServer();
	}

	private static void runServer() throws Exception {
		ServerSocket ss = new ServerSocket(8081);// ����˼���3574����˿�
		Socket s = ss.accept();// ����˽��ܿͻ��˵�����
		System.out.println(s.getLocalSocketAddress().toString());
		InputStream in = s.getInputStream();// �õ��ͻ��˵���������Ϊ�˵õ��ͻ��˴���������
		OutputStream out = s.getOutputStream();// �õ��ͻ��˵��������Ϊ����ͻ����������
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(in));
		PrintWriter bufferedWriter = new PrintWriter(out, true);

		while (true) {

			Thread.sleep(1000);
			String line = null;
			bufferedWriter.println(new Date() + ",Hello,I'm Server!");// ��ͻ����������
			line = bufferedReader.readLine();// ��ȡ�ͻ��˴���������
//			if (line == null) {
//				break;
//			}
			System.out.println("�ͻ���˵��" + line);// ��ӡ�ͻ��˴���������
		}
	}
}
