package cn.zwc.niosocket.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketClient {
	public static void main(String[] args) throws Exception {
		//建立到服务端的链接
		  SocketAddress address = new InetSocketAddress("127.0.0.1", 9000);
		  SocketChannel client = SocketChannel.open(address);
		  //创建静态的缓冲区
		  ByteBuffer buffer = ByteBuffer.allocate(255);

		  //读取数据,到buffer中
		  client.read(buffer);
		  //将position重新置为0
		  buffer.clear();
		  //输出缓冲区的数据
		  for (int i = 0; i < buffer.array().length; i++) {
		   System.out.println(buffer.array()[i]);
		  }
	}
	
}
