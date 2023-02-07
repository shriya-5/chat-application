import java.io.*;
import java.net.*;
import java.util.Scanner;
import static java.lang.Character.*;
import java.util.logging.Level;
import java.util.logging.Logger;
  
public class client 
{
    static String key1 = "awevde";
    static String key2 = "984315";

    public static String encrypt(String str) {
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
    final static int ServerPort = 2223;
  
    public static void main(String args[]) throws UnknownHostException, IOException 
    {
         
        
        Scanner scn = new Scanner(System.in);
          
       
       // InetAddress ip = InetAddress.getByName("localhost");
          

        Socket s = new Socket("192.168.88.39", ServerPort);
          
       
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
  
      
String msg;
        Thread sendMessage = new Thread(new Runnable() 
        {
            @Override
            public void run() {
                while (true) {
  
                    
                    String msg = scn.nextLine();
if(msg.equals("logout"))
{
break;

}
                   
                     
                    try {
                       
                        dos.writeUTF(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
         
          
       
        Thread readMessage;
      readMessage = new Thread(new Runnable() 
      {
          @Override
          public void run() {
              
              while (true) {
                  try {
                      // read the message sent to this client
                      String msg = dis.readUTF();
                      String r=dis.readUTF();
                      String rec=  decrypt(msg);
                      System.out.println(r +" : "+rec);
                      if(r.equalsIgnoreCase("logout"))
                      {
                          
                          sendMessage.interrupt();
                          
                          break;
                      }
                    //   String decom[]=rec.split(":");
                   
                    //for(int i=1;i<decom.length-1;i++)
                    //    System.out.print(decom[i]+":");
                    //System.out.println(r+":"+decom[decom.length-1]);
                   // if(decom[decom.length-1].equals("logout"))
                   // {
                   //     break;
                   // }
                
                  } catch (IOException e) {
                      
                      e.printStackTrace();
                  }
                  
                  
              }
          }
      });
  
        sendMessage.start();
        readMessage.start();
  
    }
}