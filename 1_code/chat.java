import java.io.*;
import java.net.*;
import java.util.Scanner;

public class chat
{
    public static String encrypt(String input, int key){
    char[] chars = input.toUpperCase().replaceAll("[^A-Z]", "").toCharArray();
    for (int i = 0; i < chars.length; i++){
        chars[i] -= 65;
        chars[i] = (char)((chars[i] + key) % 26);
        chars[i] += 65;
    }
    return String.valueOf(chars);
}

public static String decrypt(String input, int key){
    char[] chars = input.toUpperCase().replaceAll("[^A-Z]", "").toCharArray();
    for (int i = 0; i < chars.length; i++){
        chars[i] -= 65;
        chars[i] = (char)((chars[i] - key) % 26);
        chars[i] += 65;
    }
    return String.valueOf(chars);
}
    static void Client() throws IOException 
    {
        String ip;
        BufferedReader cmb=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Masukkan IP Server : ");
        ip=cmb.readLine();
        Socket client=null;
        client=new Socket(""+ip,8888);
        BufferedReader sin=new BufferedReader(new
        InputStreamReader(client.getInputStream()));
        PrintStream sout=new PrintStream(client.getOutputStream());
        BufferedReader stdin=new BufferedReader(new InputStreamReader(System.in));
        String s;

        while (true)
        {
			int key = 2;
            System.out.print("Client : ");
            s=stdin.readLine();
			//System.out.println(s);
			String encryptedInput = encrypt(s, key);
            System.out.println("Client Encrypted : "+encryptedInput);
			sout.println(encryptedInput);
			
            
			
			
			
			s=sin.readLine();
            System.out.print("\n Server : "+s+"\n");
            if(s.equalsIgnoreCase("Bye"))
            break;
        }
        stdin.close();
        sout.close();
        sin.close();
        client.close();
    }
    
	static void Server() throws IOException {
    ServerSocket server=null;
    Socket client=null;

    try
    {
        server=new ServerSocket(8888);
        System.out.println("Server telah online");
        client=server.accept();
        System.out.println("Client telah masuk dan bisa online");
    }

    catch(IOException e)
    {
        System.out.println(e.getMessage());
        System.exit(-1);
    }

    System.out.println("Silahkan chat");
    InputStream masuk=client.getInputStream();
    OutputStream keluar=client.getOutputStream();
    BufferedReader in=new BufferedReader(new
    InputStreamReader(client.getInputStream()));
    PrintStream out=new PrintStream(client.getOutputStream());
    BufferedReader stdin=new BufferedReader(new InputStreamReader(System.in));
    String pesan;

    while (true)
    {
        pesan=in.readLine();
		int key = 2;
		String decryptedInput = decrypt(pesan, key);
        if(pesan.equalsIgnoreCase("stop"))
        {
            out.println("sampai jumpa");
            break;
        }
    System.out.println("Client chat : "+pesan);
	System.out.println("Client Decrypted : " + decryptedInput);
    System.out.print("\n Server : ");
    pesan=stdin.readLine();
    out.println(pesan);
    }

    server.close();
    client.close();
    in.close();
    out.close();
    stdin.close();
}

public static void main(String args[]) throws IOException 
{
    int pil;
    System.out.println("Masukkan Angka yang akan Anda Pilih");
    System.out.println("1. Server");
    System.out.println("2. Client");
    System.out.println("3. Keluar");
    System.out.println();
    System.out.println("Masukkan Pilihan : ");
    Scanner input = new Scanner(System.in);
    pil = Integer.parseInt(input.next());
    switch(pil)
    {
    case 1:
    Server();
    break;
    case 2:
    Client();
    break;
    case 3:
    System.out.println();
    System.out.println("Terima Kasih Telah Menggunakan Aplikasi Ini");
    break;

    default :
    System.out.println("Maaf Keyword Yang Anda Masukkan Salah");
    System.out.println("Silahkan Ulangi Lagi");
    break;
    }
}}