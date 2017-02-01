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
		// �½�NIOͨ��
		ServerSocketChannel server = ServerSocketChannel.open();
		// ʹͨ��Ϊ������
		server.configureBlocking(false);
		// ��������NIOͨ����socket����
		ServerSocket ss = server.socket();
		// �½�socketͨ���Ķ˿�
		ss.bind(new InetSocketAddress(9000));
		// ��NIOͨ���󶨵�ѡ����
		Selector selector = Selector.open();
		server.register(selector, SelectionKey.OP_ACCEPT);

		while (true) {
			// ��ȡͨ�����Ƿ���ѡ�����Ĺ����¼�
			int num = selector.select();
			// ���С��1,ֹͣ�˴�ѭ��,������һ��ѭ��
			if (num < 1) {
				continue;
			}
			// ��ȡͨ���ڹ����¼��ļ���
			Set selectedKeys = selector.selectedKeys();
			Iterator iterator = selectedKeys.iterator();
			while (iterator.hasNext()) {
				SelectionKey key = (SelectionKey) iterator.next();
				// ���ߴ˴��¼�
				iterator.remove();

				if (key.isAcceptable()) {
					// ��ȡ��Ӧ��SocketChannel
					SocketChannel client = server.accept();
					System.out.println("Accepted connection from " + client);
					// ʹ��ͨ��Ϊ������
					client.configureBlocking(false);
					// ������data�Ĵ�С����ΪByteBuffer�������Ĵ�С
					ByteBuffer source = ByteBuffer.wrap(data);

					// �ڴ�ͨ����ע���¼�
					SelectionKey key2 = client.register(selector,
							SelectionKey.OP_WRITE);
					// ͨ��ִ���¼�
					key2.attach(source);
				} else if (key.isWritable()) {
					// ��ȡ��ͨ����SocketChannel
					SocketChannel client = (SocketChannel) key.channel();
					ByteBuffer output = (ByteBuffer) key.attachment();
					// ���������û��,����һ��
					if (!output.hasRemaining()) {
						output.rewind();
					}
					// �ڴ�ͨ����д����
					client.write(output);
				}
				key.channel().close();
			}

		}

	}

}
