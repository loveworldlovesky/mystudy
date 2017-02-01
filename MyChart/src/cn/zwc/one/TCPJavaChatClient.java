package cn.zwc.one;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.*;
import javax.swing.*;


public class TCPJavaChatClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			new TCPChatClient().launch();
	}
}

class TCPChatClient {
	private Frame f=null;
	private TextArea ta=null;
	private TextField tf=null;
	private Button bn=null;
	private DataInputStream dis=null;
	private DataOutputStream dos=null;
	private Socket s=null;
	
	public void launch(){
		creatUI();
		connection();
		new TCPClientReader().start();
		new TCPClientWriter().start();
	}
	public void creatUI(){
		f=new Frame();
		f.setTitle("客户端");
		tf=new TextField();
		ta=new TextArea();
		Panel p=new Panel(new BorderLayout());
		bn=new Button("发送");
		p.add(tf,BorderLayout.CENTER);
		p.add(bn,BorderLayout.EAST);
		f.add(ta,BorderLayout.CENTER);
		f.add(p,BorderLayout.SOUTH);
		
		f.addWindowListener(new actionA());
		f.setSize(250,250);
		f.setVisible(true);
	}
	public void close()
	{
		try{
			s.close();
			dis.close();
			dos.close();
		}
		catch(Exception e)
		{
			System.exit(-1);
		}
	}
	public void connection()
	{
		try{
			//这里一旦new Socket成功后，便自动的请求建立连接，参数为目标IP地址和目标端口号
			s=new Socket("127.0.0.1",8888);
			
			//这里是Socket对象调用getInputStream方法获取输入数据，然后用DataInputStream类的对象来接收
			dis=new DataInputStream(s.getInputStream());
			//这里Socket对象调用getOutputStream方法来写入数据，然后用DataOutputStream类的对象来接收
			dos=new DataOutputStream(s.getOutputStream());
		}
		catch(Exception e)
		{
			System.out.println("建立连接失败");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	class TCPClientReader extends Thread{
		public void run()
		{
			while(true)
			{
				try{
					String str=dis.readUTF();
					tf.setText("");
					ta.append("对方说："+str+"\n");
					if(str.equalsIgnoreCase("再见")||str.equalsIgnoreCase("88"))
					{
						System.exit(-1);
					}
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(f, "已经断开连接");
					return;
				}
			}
		}
	}
	class TCPClientWriter extends Thread{
		public void run()
		{
			tf.addActionListener(new TCPClientListener());
			bn.addActionListener(new TCPClientListener());
		}
	}
	
	class TCPClientListener implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
				try{
					String str=tf.getText();//获取从键盘输入到文本框Text里的内容放到str
					tf.setText("");//设置文本内容为“”，即为空，没进入一次循环就将文本框里面的内容清空，可以再次输入
					ta.append("回复："+str+"\n");//这里是文本区域调用append方法将输入文本框str内容添加进去，即显示出来
					dos.writeUTF(str);
					if(str.equalsIgnoreCase("再见")||str.equalsIgnoreCase("88"))
					{
						System.exit(-1);
					}
				}
				catch(Exception e2)
				{
					JOptionPane.showMessageDialog(f, "已经断开连接");
					return;
				}
		}
		
	}
	
	
}


class actionA implements WindowListener //这里是定义一个实现窗口事件监听的类即add后面类名为事件监听器
{ 
	//下面是对窗口监听类中方法的重写,因为借口中的方法都是抽象的，所以使用时都需要重写，下面是java api文档中窗口事件监听器WindowListener中的相关方法
	
	//下面的方法是相应的事件操作，方法名可在API文档中发现
	public void windowClosing(WindowEvent e)//关闭 
	{ 
		System.exit(0); 
	} 
	public void windowOpened(WindowEvent e){}; 
	public void windowIconified(WindowEvent e){};//最小化 
	public void windowDeiconified(WindowEvent e){}; //最大化
	public void windowClosed(WindowEvent e){}; 
	public void windowActivated(WindowEvent e){}; 
	public void windowDeactivated(WindowEvent e){};   
}