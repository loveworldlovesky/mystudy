package cn.zwc.niosocket.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketClient {
	public static void main(String[] args) throws Exception {
		//����������˵�����
		  SocketAddress address = new InetSocketAddress("127.0.0.1", 9000);
		  SocketChannel client = SocketChannel.open(address);
		  //������̬�Ļ�����
		  ByteBuffer buffer = ByteBuffer.allocate(255);

		  //��ȡ����,��buffer��
		  client.read(buffer);
		  //��position������Ϊ0
		  buffer.clear();
		  //���������������
		  for (int i = 0; i < buffer.array().length; i++) {
		   System.out.println(buffer.array()[i]);
		  }
	}
	
}
