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


//��������ͻ����������������ǽ����������������һ��
public abstract class TestJavaChatServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChatServer().launch();
	}

}

//���ﶨ��һ��������������еĴ���(���ڻ�����Ľ�������Ӧ�ĳ�ʼ������Ӧ�ķ��������֮��)��ʹ�������������ļ��
class ChatServer{
	
	//���ｫ��Ҫ�õ����ı��򣬰�ť����Ķ��󶼶�������ԣ����Լ��ٺ������룻
	private TextArea ta=null;
	private TextField tf=null;
	private Button bn=null;
	private DataOutputStream dos=null;
	private DataInputStream dis=null;
	private	ServerSocket ss=null;
	private Socket s=null;
	private Frame f=null;
	
	
	public void launch(){
		//�÷�������ĸ�����������ͼ�����UI��������ͻ������ӣ������������˵Ķ�ȡ�̣߳������������˵�д���߳�
		creatUI();
		connection();
		//���ﶨ�������̣߳�����Ϊ��д�������߳̽��в����õ��߳����
		new TCPServerReader().start();
		new TCPServerWriter().start();
	}
	public void creatUI(){
		f=new Frame();
		f.setTitle("��������");
		ta =new TextArea();//���Ϊ��ʾ���ı�����
		tf=new TextField();//���Ϊ������ı���
		
		//�ȶ���һ��Panel����P���Ҳ��ֹ�����ΪBorderLayout�������ϱ�����
		Panel P=new Panel(new BorderLayout());
		bn=new Button("����");//���ﶨ��һ�����Ͱ�ť
		
		P.add(tf,BorderLayout.CENTER);//��P����������ı���tf�������м�
		P.add(bn,BorderLayout.EAST);//������ť���ڶ���
		
		f.add(ta,BorderLayout.CENTER);//��f�����һ���ı�����ta�����м�
		f.add(P,BorderLayout.SOUTH);//���ϱ߷�P���������ı���ͷ��Ͱ�ť���ŵ���ױ�
		
		f.addWindowListener(new actionB());//���Ӵ��ڿ����¼�������
		
		f.setSize(250, 250);//���ô��ڴ�С
		f.setVisible(true);
		
	}
	
	//�����ǹرյķ�����������������ͽ��ܷ��Ͷ˵Ĺر��쳣����
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
	
	//�������ӷ���,����Ҫ�ľ��Ǵ���ServerSocket����Ȼ�����accept�������ظ�һ��Socket���󣬺��������ȷSocket����s���÷���getInputStream��getOutputStream�����ķ���ֵ�ֱ���Ϊnew DataInputStream��DataOutstream����Ĳ���
	public void connection(){
		try{
			//������������
			ss=new ServerSocket(8888);
			
			s=ss.accept();//����accept������ſ�ʼ���ϼ����ͻ����Ƿ��з�����������,���ҷ���һ��Socket����������Ҫ��һ��Socket����s������
			
			//������Ҫ�õ�DataInputStream��DataOutputStream���еĶ�ȡ��д��������͵ķ���
			dis=new DataInputStream(s.getInputStream());
			dos=new DataOutputStream(s.getOutputStream()); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("��������ʧ��");
			System.exit(-1);
		}
		
	} 
	class TCPServerReader extends Thread{
		public void run(){
			//while1����ѭ��ȷ�������ض������±���ͨ���Ľ���
			while(true)
			{
				try{
					String str=dis.readUTF();//����һ��String����str��������dis.readUTF�������ص�����
					tf.setText("");//setText���ı�����ʾ��Ϣ
					ta.append("�Է�˵��"+str+"\n");
					if(str.equalsIgnoreCase("�ټ�")||str.equalsIgnoreCase("88"))
					{
						close();//�����ǵ���close������Socket,ServerSocket,DataInputStream��DataOutputStream�Ķ���close��
						System.exit(-1);
					}
					
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(f, "�Ѿ��Ͽ�����");//������swing������෽��
					return;
				}
			}
		}
	}
	
	//�������ݵ��̣߳�������Ҫ�¼������¼�����������ΪҪ�漰��д�����ݲ��ҵ㷢�ͼ����ͳ�ȥ
	class TCPServerWriter extends Thread{
		public void run(){
			//��һ���¼��������Ǽ���ı�������¼�
			tf.addActionListener(new TCPListener());
			
			//�ڶ��¼��������Ǽ�ⰴť���͵��¼�
			bn.addActionListener(new TCPListener());
			
		}
	}
	
	class TCPListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			try{
				String str=tf.getText();//����һ��String�������������ı���tfʹ��getText������ȡ�������������
				tf.setText("");//�����ٴΰ��ı�������Ϊ�գ�����գ��Ա����´�����
				ta.append("�ظ�:"+str+"\n");//�ı��������append������ȡstr���ݲ���ʾ����
				dos.writeUTF(str);//�������dos����WriteUTF��strд��ȥ�����ڴ���
				
				//������˫�������ټ�����88��Ȼ�����String��equalsIgnoreCase�������жԱȣ��öԽϷ���ֻ�Ƚ�����
				if(str.equalsIgnoreCase("�ټ�")||str.equalsIgnoreCase("88")){
					close();
					System.exit(-1);//��������رնԻ���
				}
			}
			catch(Exception e2){
				JOptionPane.showMessageDialog(f, "�Ѿ��Ͽ�����");//f�ǶԻ�������Ϊ�Ͽ�����
				return;
			}
		}
	}
}


class actionB implements WindowListener //�����Ƕ���һ��ʵ�ִ����¼��������༴add��������Ϊ�¼�������
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

