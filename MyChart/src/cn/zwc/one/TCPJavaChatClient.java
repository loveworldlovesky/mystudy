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
		f.setTitle("�ͻ���");
		tf=new TextField();
		ta=new TextArea();
		Panel p=new Panel(new BorderLayout());
		bn=new Button("����");
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
			//����һ��new Socket�ɹ��󣬱��Զ������������ӣ�����ΪĿ��IP��ַ��Ŀ��˿ں�
			s=new Socket("127.0.0.1",8888);
			
			//������Socket�������getInputStream������ȡ�������ݣ�Ȼ����DataInputStream��Ķ���������
			dis=new DataInputStream(s.getInputStream());
			//����Socket�������getOutputStream������д�����ݣ�Ȼ����DataOutputStream��Ķ���������
			dos=new DataOutputStream(s.getOutputStream());
		}
		catch(Exception e)
		{
			System.out.println("��������ʧ��");
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
					ta.append("�Է�˵��"+str+"\n");
					if(str.equalsIgnoreCase("�ټ�")||str.equalsIgnoreCase("88"))
					{
						System.exit(-1);
					}
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(f, "�Ѿ��Ͽ�����");
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
					String str=tf.getText();//��ȡ�Ӽ������뵽�ı���Text������ݷŵ�str
					tf.setText("");//�����ı�����Ϊ��������Ϊ�գ�û����һ��ѭ���ͽ��ı��������������գ������ٴ�����
					ta.append("�ظ���"+str+"\n");//�������ı��������append�����������ı���str������ӽ�ȥ������ʾ����
					dos.writeUTF(str);
					if(str.equalsIgnoreCase("�ټ�")||str.equalsIgnoreCase("88"))
					{
						System.exit(-1);
					}
				}
				catch(Exception e2)
				{
					JOptionPane.showMessageDialog(f, "�Ѿ��Ͽ�����");
					return;
				}
		}
		
	}
	
	
}


class actionA implements WindowListener //�����Ƕ���һ��ʵ�ִ����¼��������༴add��������Ϊ�¼�������
{ 
	//�����ǶԴ��ڼ������з�������д,��Ϊ����еķ������ǳ���ģ�����ʹ��ʱ����Ҫ��д��������java api�ĵ��д����¼�������WindowListener�е���ط���
	
	//����ķ�������Ӧ���¼�����������������API�ĵ��з���
	public void windowClosing(WindowEvent e)//�ر� 
	{ 
		System.exit(0); 
	} 
	public void windowOpened(WindowEvent e){}; 
	public void windowIconified(WindowEvent e){};//��С�� 
	public void windowDeiconified(WindowEvent e){}; //���
	public void windowClosed(WindowEvent e){}; 
	public void windowActivated(WindowEvent e){}; 
	public void windowDeactivated(WindowEvent e){};   
}