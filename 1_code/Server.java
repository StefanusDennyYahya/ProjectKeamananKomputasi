import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.lang.*;
import javax.swing.*;
import java.awt.event.*;

public class Server extends JFrame implements ActionListener{
	
	JButton EnterText 	= new JButton("Enter");
	JButton KickF		= new JButton("Remove");
	JButton Exit		= new JButton("Exit");
	JTextArea textArea  = new JTextArea(15, 20);
	JTextArea conList   = new JTextArea(15, 10);
	JScrollPane textA   = new JScrollPane(textArea);
	JScrollPane conL    = new JScrollPane(conList);
	JTextField textFill = new JTextField(30);
	Container container;
		
	public String borderLine 	= "=========\n";
	public String listLine 		= "Connected List :\n" + borderLine;
	public String textLine		= "Chat Room :\n" + borderLine;
	public static String Name;
	String tempS;
	
		//Socket	
			Vector<Socket>connectedS = new Vector<Socket>();
			Vector<String>connectedN = new Vector<String>();
				PrintWriter pW;

	
	public static void main(String[]args){
		try{
			Register();
			new Server();
		}catch(Exception e){}
	}
	
	
	public Server(){
		// GUI
			setTitle("Chat Room (SERVER) : " + Name);
			setSize(400, 350);
			setResizable(false);
			setVisible(true);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		//GUI Placement
			container = getContentPane();
			container.setLayout(new FlowLayout());
			container.add(textA);
			container.add(conL);
			container.add(textFill);
			container.add(EnterText);
			container.add(KickF);
			container.add(Exit);
				EnterText.addActionListener(this);
				KickF.addActionListener(this);
				Exit.addActionListener(this);
			
		//GUI Settings
			textArea.setEnabled(false);
			conList.setEnabled(false);
			KickF.setEnabled(false);
				conList.setText(listLine);
				textArea.setText(textLine);
		
		//Accept Incoming Client
			try{
				ServerSocket 	ss = new ServerSocket(5000);	
				
				//Just 2 Clients Can Connect
				while(connectedS.size()<2){
					Socket s = ss.accept();	
					connectedS.add(s);	
					socketThread st = new socketThread(s, connectedS, connectedN);							
					Thread t = new Thread(st);
					
					t.start();
				}
			}catch(Exception e){
			}	
			
	}

	
	//Register Name
	public static void Register(){

			do{
				Name = JOptionPane.showInputDialog(null, "Input Your Name To Continue :");
			}while(Name.equals(""));

	}
	
		//Action Listener
			public void actionPerformed(ActionEvent e){
				
				//Enter Text
				if(e.getSource() == EnterText && (textFill.getText().length() != 0)){
					textLine += Name + " Said : " + textFill.getText() + "\n" + borderLine;
				
					for(int i=0; i<connectedS.size(); i++)
					sendData( Name + " Said : " + textFill.getText(), connectedS.get(i));
				
					textArea.setText(textLine);
					textFill.setText("");
				}else if(e.getSource() == Exit){	
				
				//Exit Chat
				if(JOptionPane.showConfirmDialog(null, "Do You Want To Exit ?") == 0){ 

						for(int i=0; i<connectedS.size(); i++)
							sendData("./Q", connectedS.get(i));						
								
						System.exit(0);	
					}
				}else if(e.getSource() == KickF){

				
				//Kick Friends
				try{
					do{
						tempS = JOptionPane.showInputDialog(null, "Choose Friend To Be Removed :");
						if(tempS.equals(Name)){
							JOptionPane.showMessageDialog(null, "Cannot Kick Your Self.", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}while(tempS.equals("") || tempS.equals(Name));
					
						for(int i=0; i<connectedN.size(); i++)
							if(tempS.equals(connectedN.get(i)))
								removeList(tempS);	
													
	
				}catch(Exception eE){}

				
				
				}
			}
				//Remove Connected List && Remove Friends (Only Server Can Do This)
				public void removeList(String theName){
					
													for(int i=0; i<connectedS.size(); i++)
													sendData("./R" + theName ,connectedS.get(i));					
					
								textLine += theName + " removed from the chat.\n" + borderLine;
								for(int i=0; i<connectedS.size(); i++)
									sendData(theName + " removed from the chat.\n" + borderLine, connectedS.get(i));
								textArea.setText(textLine);
					
					for(int i=0; i<connectedN.size(); i++)
						if(theName.equals(connectedN.get(i))){
							connectedN.remove(i);
							connectedS.remove(i);
						}
					
					updateList();
				}		

				//Update Connected List
				public void updateList(){
					listLine = "Connected List :\n" + borderLine; 
					
					for(int i=0; i<connectedN.size(); i++)
						listLine += connectedN.get(i) + "\n";
					conList.setText(listLine);
					
					if(connectedN.size() == 0)
						KickF.setEnabled(false);
					else
						KickF.setEnabled(true);
					
													for(int i=0; i<connectedS.size(); i++){
														sendData("./E" + tempS, connectedS.get(i));	
																		System.out.print("");
													}
				}
				
				
		//Socket Thread
			public class socketThread implements Runnable{
				
				private String name;
				private Vector<String>connectedN;
				private Vector<Socket>S;
				private Socket s;
				private Thread t;
				BufferedReader inB;
				String inS = "";
				boolean regis = false;
				PrintWriter pW;
				String temp;
								
				public socketThread(Socket s, Vector<Socket> S, Vector<String>connectedN){
					t 			= new Thread(this);
					this.connectedN = connectedN;
					this.s 		= s;
					this.S		= S;
					t.start();
				}
				
				
				public void quiz(){
					
					///Not Yet
					
				}
				
				public void quizScore(){
					
					///Not Yet
					
				}
				
				public boolean whoCanSpeak(){
					for(int i=0; i<connectedN.size(); i++){
						if(connectedN.get(i).equals(name))
							return true;
					}
					return false;
				}
				
				//Update Connected List
				public void updateList(){
					listLine = "Connected List :\n" + borderLine; 
					

					
					for(int i=0; i<connectedN.size(); i++)
						listLine += connectedN.get(i) + "\n";
					conList.setText(listLine);
					
					if(connectedN.size() == 0)
						KickF.setEnabled(false);
					else
						KickF.setEnabled(true);
				}
				
				//Remove Connected List && Remove Friends (Only Server Can Do This)
				public void removeList(String theName){
															
					for(int i=0; i<connectedN.size(); i++)
						if(theName.equals(connectedN.get(i))){
							connectedN.remove(i);
							S.remove(i);	
						}
					updateList();
				}
				
				//Send Connected List
				public void sendList(){
					String data = "./C";
						for(int i=0; i<connectedN.size(); i++)
							data += connectedN.get(i) + " ";
						
						for(int i=0; i<connectedS.size(); i++)
							sendData(data, connectedS.get(i));
					
				}
				

				
				
				public void run(){
					
					try{
					
						if(Thread.currentThread() == t){
							//Get Chat From Another
								do{
								inB = new BufferedReader(new InputStreamReader(s.getInputStream()));
								inS = inB.readLine();
									if(!inS.equals("./exit") && !inS.equals("./R")){
											if(regis == true && whoCanSpeak()){
												textLine += name + " Said :" + inS + "\n" + borderLine;
													for(int i=0; i<S.size(); i++)
														sendData(name + " Said :" + inS + "\n" + borderLine, S.get(i));												
												textArea.setText(textLine);
											}else{
												name 	= inS;
												regis 	= true;
												connectedN.add(name);
												sendList();
												updateList(); 
												notifNewComer();
											}
									}else{
													for(int i=0; i<S.size(); i++)
														sendData("./E" + name, S.get(i));
										notifLeftChat();
										removeList(name);	
									}
								}while(!inS.equals("END"));
												
							
						}
					}catch(Exception e){};
					
				}
			
					//Notification New Comer
						public void notifNewComer(){
								textLine += name + " joined the chat.\n" + borderLine;
								for(int i=0; i<S.size(); i++){
									sendData(name + " joined the chat.\n" + borderLine, S.get(i));
									}
								textArea.setText(textLine);
						}
					
					//Notification Left The Chat	
						public void notifLeftChat(){
								textLine += name + " left the chat.\n" + borderLine;
								for(int i=0; i<S.size(); i++)
									sendData(name + " left the chat.\n" + borderLine, S.get(i));
								textArea.setText(textLine);		

						}
						
					//Send Chat To Another
						public void sendData(String dataSend, Socket s){
							try{
										pW = new PrintWriter(s.getOutputStream(), true);
										pW.println(dataSend);				
							}catch(Exception e){}
						}
						
			
			}
	
			//Send Chat To Another
				public void sendData(String dataSend, Socket s){
					try{
								pW = new PrintWriter(s.getOutputStream(), true);
								pW.println(dataSend);				
					}catch(Exception e){}
				}
}