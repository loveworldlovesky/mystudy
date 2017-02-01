package cn.zwc.one;

import java.awt.*;
import java.awt.Event;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.*;
import javax.swing.*;


//服务器与客户端最根本的区别就是建立连接那里，其他都一样
public abstract class TestJavaChatServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChatServer().launch();
	}

}

//这里定义一个类完成主方法中的代码(窗口化界面的建立，相应的初始化和相应的方法类调用之类)，使得主方法尽量的简洁
class ChatServer{
	
	//这里将需要用到的文本框，按钮，类的对象都定义成属性，可以减少后续代码；
	private TextArea ta=null;
	private TextField tf=null;
	private Button bn=null;
	private DataOutputStream dos=null;
	private DataInputStream dis=null;
	private	ServerSocket ss=null;
	private Socket s=null;
	private Frame f=null;
	
	
	public void launch(){
		//该方法完成四个工作：创建图像界面UI；创建与客户端连接；启动服务器端的读取线程；启动服务器端的写入线程
		creatUI();
		connection();
		//这里定义两个线程，是因为读写是两个线程进行不能用单线程完成
		new TCPServerReader().start();
		new TCPServerWriter().start();
	}
	public void creatUI(){
		f=new Frame();
		f.setTitle("服务器端");
		ta =new TextArea();//这个为显示的文本区域
		tf=new TextField();//这个为输入的文本框
		
		//先定义一个Panel对象P而且布局管理器为BorderLayout即东西南北布局
		Panel P=new Panel(new BorderLayout());
		bn=new Button("发送");//这里定义一个发送按钮
		
		P.add(tf,BorderLayout.CENTER);//往P中添加输入文本框tf，放在中间
		P.add(bn,BorderLayout.EAST);//发生按钮放在东边
		
		f.add(ta,BorderLayout.CENTER);//在f中添加一个文本区域ta放在中间
		f.add(P,BorderLayout.SOUTH);//而南边放P，即发送文本框和发送按钮都放到最底边
		
		f.addWindowListener(new actionB());//增加窗口控制事件监听器
		
		f.setSize(250, 250);//设置窗口大小
		f.setVisible(true);
		
	}
	
	//这里是关闭的方法，对输入输出流和接受发送端的关闭异常捕获
	public void close()
	{
		try{
			dos.close();
			dis.close();
			s.close();
			ss.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
	
	//建立连接方法,最主要的就是创建ServerSocket对象，然后调用accept方法返回给一个Socket对象，后面就是明确Socket对象s调用方法getInputStream和getOutputStream方法的返回值分别作为new DataInputStream和DataOutstream对象的参数
	public void connection(){
		try{
			//创建服务器端
			ss=new ServerSocket(8888);
			
			s=ss.accept();//调用accept方法后才开始不断监听客户端是否有发送连接请求,并且返回一个Socket对象，所以需要用一个Socket对象s来接收
			
			//这里主要用到DataInputStream和DataOutputStream类中的读取和写入基本类型的方法
			dis=new DataInputStream(s.getInputStream());
			dos=new DataOutputStream(s.getOutputStream()); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("建立连接失败");
			System.exit(-1);
		}
		
	} 
	class TCPServerReader extends Thread{
		public void run(){
			//while1的死循环确保不在特定条件下保持通话的进行
			while(true)
			{
				try{
					String str=dis.readUTF();//定义一个String对象str用来接收dis.readUTF方法返回的内容
					tf.setText("");//setText让文本框显示信息
					ta.append("对方说："+str+"\n");
					if(str.equalsIgnoreCase("再见")||str.equalsIgnoreCase("88"))
					{
						close();//这里是调用close方法将Socket,ServerSocket,DataInputStream和DataOutputStream的对象都close了
						System.exit(-1);
					}
					
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(f, "已经断开连接");//这里是swing里面的类方法
					return;
				}
			}
		}
	}
	
	//发送数据的线程，这里需要事件处理即事件监听器，因为要涉及到写入数据并且点发送键发送出去
	class TCPServerWriter extends Thread{
		public void run(){
			//第一个事件监听器是检测文本输入的事件
			tf.addActionListener(new TCPListener());
			
			//第二事件监听器是检测按钮发送的事件
			bn.addActionListener(new TCPListener());
			
		}
	}
	
	class TCPListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			try{
				String str=tf.getText();//定义一个String类型用来接受文本框tf使用getText方法获取键盘输入的内容
				tf.setText("");//这里再次把文本框设置为空，即清空，以便于下次输入
				ta.append("回复:"+str+"\n");//文本区域调用append方法获取str内容并显示出来
				dos.writeUTF(str);//这里就是dos调用WriteUTF把str写进去，便于传输
				
				//这里若双方输入再见或者88，然后调用String的equalsIgnoreCase方法进行对比，该对较方法只比较内容
				if(str.equalsIgnoreCase("再见")||str.equalsIgnoreCase("88")){
					close();
					System.exit(-1);//若满足则关闭对话框
				}
			}
			catch(Exception e2){
				JOptionPane.showMessageDialog(f, "已经断开连接");//f是对话框，内容为断开连接
				return;
			}
		}
	}
}


class actionB implements WindowListener //这里是定义一个实现窗口事件监听的类即add后面类名为事件监听器
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

