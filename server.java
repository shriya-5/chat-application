
import java.io.*;
import static java.lang.Character.isDigit;
import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;
import java.util.*;
import java.net.*;
import java.io.EOFException;
public class server
{
    static Vector<ClientHandler> ar = new Vector<>();
     
   
    static int i = 0;
   
    public static void main(String[] args) throws IOException
    {
        
        ServerSocket ss = new ServerSocket(2223);
         Scanner in=new Scanner(System.in);
        Socket s;
         
      
        while (true)
        {
            
            s = ss.accept();
 
            System.out.println("New client request received : " + s);
             String name;
             System.out.println("Enter username:");
             name=in.nextLine();
            
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
             
            System.out.println("Creating a new handler for this client...");
 
           
            ClientHandler mtch = new ClientHandler(s,name, dis, dos);
 
            
            Thread t = new Thread(mtch);
             
            System.out.println("Adding this client to active client list");
 
            
            ar.add(mtch);
 
            t.start();
 
            
            i++;
 
        }
    }
}
 
// ClientHandler class
class ClientHandler implements Runnable {

   static String key1 = "awevde";
    static String key2 = "984315";
        public static String encrypt(String str)
    {
        
         int l = str.length();
        String encr = "";
        String key11 = "", key22 = "";
        char ch;
        int x, y, i;
        x = l / key1.length();
        y = l % key1.length();
        for (i = 0; i < x; i++) {
            key11 = key11 + key1;
            key22 = key22 + key2;
        }
        for (i = 0; i < y; i++) {
            key11 = key11 + key1.charAt(i);
            key22 = key22 + key2.charAt(i);
        }
        for (i = 0; i < l; i++) {
            ch = str.charAt(i);
            if (isDigit(ch))
                encr = encr + (char) ((ch - '0' + key22.charAt(i) - '0') % 10 + '0');
            else if (isUpperCase(ch))
                encr = encr + (char) ((ch - 'A' + (key11.charAt(i)) - 'a') % 26 + 'A');
            else if (isLowerCase(ch))
                encr = encr + (char) ((ch - 'a' + (key11.charAt(i)) - 'a') % 26 + 'a');
            else
                encr = encr + ch;
        }
        return encr;
    }

    public static String decrypt(String str) {
        int l = str.length();
        String decr = "";
        String key11 = "", key22 = "";
        char ch;
        int x, y, i;
        x = l / key1.length();
        y = l % key1.length();
        for (i = 0; i < x; i++) {
            key11 = key11 + key1;
            key22 = key22 + key2;
        }
        for (i = 0; i < y; i++) {
            key11 = key11 + key1.charAt(i);
            key22 = key22 + key2.charAt(i);
        }
        for (i = 0; i < l; i++) {
            ch = str.charAt(i);
            if (isDigit(ch))
                decr = decr + (char) ((ch - '0' - key22.charAt(i) + '0' + 10) % 10 + '0');
            else if (isUpperCase(ch))
                decr = decr + (char) ((ch - 'A' - (key11.charAt(i)) + 'a' + 26) % 26 + 'A');
            else if (isLowerCase(ch))
                decr = decr + (char) ((ch - 'a' - (key11.charAt(i)) + 'a' + 26) % 26 + 'a');
            else
                decr = decr + ch;
        }
        return decr;
    }
     
 
    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;
     
    // constructor
    public ClientHandler (Socket s, String name,
                            DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin=true;
    }
 
    @Override
    public void run() {
 
        String received;
        
        while (true)
        {
            try
            {
                // receive the string
                received = dis.readUTF();
               
                System.out.println(received);
                 try
                 {
                if(received.equals("logout")){
      
                    this.isloggedin=false;
                    this.s.close();
                    this.dos.close();
                    this.scn.close();
                    this.dis.close();
                    break;
                }
                 }catch(EOFException e)
                 {
                     System.out.println("User logged out");
                 }
                 
                StringTokenizer st = new StringTokenizer(received, "@");
                String MsgToSend = st.nextToken();
                String recipient = st.nextToken();
                String encryptedMsg = encrypt(MsgToSend);
                      System.out.println("ENCRYPTED MSG :"+encryptedMsg);
 
                // search for the recipient in the connected devices list.
                // ar is the vector storing client of active users
                for (ClientHandler mc : server.ar)
                {
                    // if the recipient is found, write on its
                    // output stream
                    if (mc.name.equals(recipient) && mc.isloggedin==true)
                    {
                       // mc.dos.writeUTF(this.name+" : "+ encryptedMsg );
                        mc.dos.writeUTF(encryptedMsg);
                        mc.dos.writeUTF(this.name);
                        break;
                    }
                }
            } catch (IOException e) {
                 
                e.printStackTrace();
            }
             
        }
        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();
             
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}

