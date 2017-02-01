package cn.zwc.niosocket.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOSocketServer {

	private static byte[] data = new byte[255];

	public static void main(String[] args) throws IOException {
		for (int i = 0; i < data.length; i++) {
			data[i] = (byte) i;
		}
		// 新建NIO通道
		ServerSocketChannel server = ServerSocketChannel.open();
		// 使通道为非阻塞
		server.configureBlocking(false);
		// 创建基于NIO通道的socket连接
		ServerSocket ss = server.socket();
		// 新建socket通道的端口
		ss.bind(new InetSocketAddress(9000));
		// 将NIO通道绑定到选择器
		Selector selector = Selector.open();
		server.register(selector, SelectionKey.OP_ACCEPT);

		while (true) {
			// 获取通道内是否有选择器的关心事件
			int num = selector.select();
			// 如果小于1,停止此次循环,进行下一个循环
			if (num < 1) {
				continue;
			}
			// 获取通道内关心事件的集合
			Set selectedKeys = selector.selectedKeys();
			Iterator iterator = selectedKeys.iterator();
			while (iterator.hasNext()) {
				SelectionKey key = (SelectionKey) iterator.next();
				// 移走此次事件
				iterator.remove();

				if (key.isAcceptable()) {
					// 获取对应的SocketChannel
					SocketChannel client = server.accept();
					System.out.println("Accepted connection from " + client);
					// 使此通道为非阻塞
					client.configureBlocking(false);
					// 将数组data的大小定义为ByteBuffer缓冲区的大小
					ByteBuffer source = ByteBuffer.wrap(data);

					// 在此通道上注册事件
					SelectionKey key2 = client.register(selector,
							SelectionKey.OP_WRITE);
					// 通道执行事件
					key2.attach(source);
				} else if (key.isWritable()) {
					// 获取此通道的SocketChannel
					SocketChannel client = (SocketChannel) key.channel();
					ByteBuffer output = (ByteBuffer) key.attachment();
					// 如果缓存区没了,重置一下
					if (!output.hasRemaining()) {
						output.rewind();
					}
					// 在此通道内写东西
					client.write(output);
				}
				key.channel().close();
			}

		}

	}

}
