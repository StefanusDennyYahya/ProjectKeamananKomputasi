import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.lang.*;
import javax.swing.*;
import java.awt.event.*;

public class Client extends JFrame implements ActionListener, Runnable{
	
	JButton EnterText 	= new JButton("Enter");
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
	
	
	//Setting Chat Process
	public Socket s;
	public int port 	= 5000;
	public String addr 	= "localhost";
	Thread t;
	BufferedReader inB;
	PrintWriter pW;
	public String inS = "";
	public static String Name;
	
	public Vector<String>connectedN = new Vector<String>();
	
	
		
	public static void main(String[]args){
		try{
			Register();
			new Client();
		}catch(Exception e){}
	}
	
	//Register Name
	public static void Register(){

			do{
				Name = JOptionPane.showInputDialog(null, "Input Your Name To Continue :");
			}while(Name.equals(""));

	}

				public void quiz(){
					
					///Not Yet
					
				}
				
				public void quizScore(){
					
					///Not Yet
					
				}

				
	public Client(){

		// GUI
			setTitle("Chat Room (CLIENT) : " + Name);
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
			container.add(Exit);
				EnterText.addActionListener(this);
				Exit.addActionListener(this);
			
		//GUI Settings
			textArea.setEnabled(false);
			conList.setEnabled(false);
				conList.setText(listLine);
				textArea.setText(textLine);
		
		//Accept Incoming Server
			try{	t = new Thread(this);	
					s = new Socket(addr, port);
					sendData(Name);
					t.start();
			}catch(Exception e){
			}	
			
	}
	
		//Update Connected List
		public void UpdateConnList(){
			String value = "";
				for(int i=0; i<connectedN.size(); i++){
					connectedN.remove(i);
				}
			
			inS = inS.replace("./C", "");	
			
			connectedN.add("Server");
				for(int i=0; i<inS.length(); i++){
					if(inS.charAt(i) == ' '){
						connectedN.add(value);
						value = "";
					}else{
						value += inS.charAt(i);
					}
					
				}
					//Remove The Own Name
					for(int i=0; i<connectedN.size(); i++)
						if(connectedN.get(i).equals(Name))
							connectedN.remove(i);
			
					//Re-update The GUI Connected List
			listLine 		= "Connected List :\n" + borderLine;
				for(int i=0; i<connectedN.size(); i++)
					listLine += connectedN.get(i) + "\n";
			conList.setText(listLine);	
		}
	
		//Removed Connected List
		public void RemoveList(){
			for(int i=0; i<connectedN.size(); i++)
				if(inS.equals(connectedN.get(i)))
					connectedN.remove(i);
			listLine 		= "Connected List :\n" + borderLine;
				for(int i=0; i<connectedN.size(); i++)
					listLine += connectedN.get(i) + "\n";
			conList.setText(listLine);				
		}
		
		//Client Processing
			public void run(){
				try{		

					//Get Chat From Another
						if(Thread.currentThread() == t){
							do{
							inB = new BufferedReader(new InputStreamReader(s.getInputStream()));
							inS = inB.readLine();

									if(inS.charAt(0) == '.' && inS.charAt(1) == '/' && inS.charAt(2) == 'C'){
										UpdateConnList();
													
									}else if(inS.charAt(0) == '.' && inS.charAt(1) == '/' && inS.charAt(2) == 'R'){
										inS = inS.replace("./R", "");
										if(Name.equals(inS)){
											JOptionPane.showMessageDialog(null, "You Are Removed By Server", "Kicked", JOptionPane.ERROR_MESSAGE);
											s.close();
											System.exit(0);
										}
									}else if(inS.charAt(0) == '.' && inS.charAt(1) == '/' && inS.charAt(2) == 'E'){
										inS = inS.replace("./E", "");
										RemoveList();
										
									}else if(inS.charAt(0) == '.' && inS.charAt(1) == '/' && inS.charAt(2) == 'Q'){
											JOptionPane.showMessageDialog(null, "Connection To Server Disconnected", "Quit", JOptionPane.ERROR_MESSAGE);
											s.close();
											System.exit(0);										
									}
									else{
							
										textLine += inS + "\n" + borderLine;
										textArea.setText(textLine);
									}
									
									
							}while(!inS.equals("END"));
							
						}
								
				}catch(Exception e){}
				
			}	
					
			
		//Send Chat To Another
			public void sendData(String dataSend){
				try{
							pW = new PrintWriter(s.getOutputStream(), true);
							pW.println(dataSend);				
				}catch(Exception e){}
			}
			
			
		//Action Listener
			public void actionPerformed(ActionEvent e){
				
				//Enter Text
				if(e.getSource() == EnterText && (textFill.getText().length() != 0)){
					sendData(textFill.getText());
					textFill.setText("");
				}else if(e.getSource() == Exit){	
				
				//Exit Chat
					if(JOptionPane.showConfirmDialog(null, "Do You Want To Exit ?") == 0){
						sendData("./exit");
						System.exit(0);	
					}
				}
				
			}
}