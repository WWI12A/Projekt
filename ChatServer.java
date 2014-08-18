/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package chatserver;
import java.net.*;
import java.io.*;
import java.util.*;




/**
 *
 * @author Heiko
 */
public class ChatServer {

   ServerSocket server;
   ServerThread thread;
   Vector tabelle= new Vector();
   Vector threadname = new Vector();
   DataOutputStream output;
   DataInputStream input;
   Socket client;
   Hashtable Nutzer = new Hashtable();
   String id;
    
public ChatServer(int port) throws IOException{
    server = new ServerSocket(port);
    System.out.println("Server aktiv auf " + server );
    listen();

}

private void listen() throws IOException{
    
while(true){
    
//Server soll Cleint annehmen
        Socket client = server.accept();

        System.out.println("Client "+ client + " hat sich angemeldet");

//Neuen Thread starten, um mehrere Clients bearbeiten zu können
        thread =new ServerThread(this, client);
        thread.start();
        
//Speichern des Thread Namens in einer Vectorliste
        tabelle.addElement(thread.getName());
        threadname.addElement(thread);
        
       
}

} 
//synchronized wird eingesetzt um kritische Abschnitte abzusichern
public synchronized void SendenAlle(String s,Thread thread){
    for(int i=0; i<tabelle.size();i++){
        
     //Nur an andere angemeldete Sockets senden, da der eigene Thread rausfliegt
       if(thread.getName().equals(tabelle.elementAt(i))){
          
       } else {     
            ((ServerThread)threadname.elementAt(i)).senden(s);
       }
    }
        
}



public synchronized void trennen(Socket client, Thread thread) throws IOException{
    System.out.println(client + " wird getrennt");
    tabelle.remove(thread.getName());
    threadname.remove(thread);
   try{
        client.close();
          
    } catch( IOException ie ) {
        System.out.println( "Error closing "+client );
       
    }

}

// Methode die nur 2 Nutzer zulässt und diese verfiziert anhand der MAC

public void Nutzererkennung(){
    for (int i=0; i<Nutzer.size(); i++){
        Nutzer.toString();
    
    }


}



class ServerThread extends Thread {
    Socket client;
    ChatServer server;
    DataInputStream input;
    DataOutputStream output;
    Thread thread;

    public ServerThread(ChatServer server, Socket client) throws IOException {
    
   this.client = client;
//Outputstream wird erstellet, um an Client schreiben zu können        
         output = new DataOutputStream( client.getOutputStream() );
//InputStream wird erstellt zum lesen der Nachrichten        
        input = new DataInputStream(client.getInputStream());

    }

    @Override
    public void run() {
        
            String s=null;
            String mac = null;
 // MAC-Adresse die der Client sendet abfragen 
       try {
            mac= input.readUTF();
           // System.out.println(mac);
            
        } catch (IOException ex) {
            System.out.println("Fehler bei Authentifizierung!");
        }
  // Nutzername der vom Client abgefragt wird empfangen
        try {
            s=input.readUTF();
            
        } catch (IOException ex) {
        }
 // MAC als Key und Nutzername als Object ablegen in Hashtabelle
       if(mac != null && s != null){
           for(int i=0; i<Nutzer.size();i++){
               
           
           }
           Nutzer.put(mac, s);
           System.out.println(Nutzer);
       }
        
            try {
                while((s = input.readUTF()) != null){
                    
                    System.out.println(s);
                //output.writeUTF(s);
                SendenAlle(s,this);
                
                }
            } catch (IOException ex) {
                
            }
        try {
                
           
            trennen(client, this);
        } catch (IOException ex) {
        }
            
        
        }
    
    public  void senden(String s){
    try{
        output.writeUTF(s);
        }catch (IOException e){
        
        }
}
    
    public void WerIstOn(){
        
    
    
    }
    }
    














    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        new ChatServer(8080);
    
}
}

