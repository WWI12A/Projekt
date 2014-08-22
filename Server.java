
import java.io.*;
import java.net.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.*;
import javax.crypto.SecretKey;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Heiko
 */
public class Server {
    //Variablendeklarationen
    private ServerSocket socket;
    private Hashtable Nutzer = new Hashtable();
    private String UserName ="kein Name übermittelt";
    private DataOutputStream out;
    private Socket client;
    Vector onlineUser = new Vector();
    
    
    
    
    
    public Server(int port) throws IOException, RemoteException, AlreadyBoundException{
        //Erstellen der Remoteimplementation
        RemoteImpl impl = new RemoteImpl(this);
        //Erstellen der registry auf Port 2222
        Registry registry = LocateRegistry.createRegistry(2222);
        //Den Namen "remote" zum auffinden in die Registry binden mit der Remoteimplementation
        registry.bind("remote", impl);
        System.out.println("server startet...");
        //startet die Methode listen, die auf sich verbindende Clients wartet und aktzeptiert
        listen(port);
    
    }
    
    public static void main(String[] args) throws RemoteException, AlreadyBoundException, IOException{
        
        //Erstellt neuen Insanz des Servers, damit dieser startet
        new Server(8080);
    
    }
    
    
    private void listen(int port) throws IOException{
        //Erstellen eines Serversockets auf mitgegebenem Port
        socket = new ServerSocket(port);
        System.out.println("Server aktiv auf: " + socket);
        
        while(true){
    
        //Server akzeptiert Client und fügt ihn zur Tabelle hinzu
        hinzufügen(socket.accept());
        System.out.println(Nutzer);
        
        
      
}
     
  }
    //Gibt den UserNamen zurück
    public String getUser(){
        return(UserName);
    }
    //Fügt den Nutzername mit dazugehörigem Socket in Hashtable Nutzer
    public void hinzufügen(Socket client) throws IOException{
                 
                    
        Nutzer.put(UserName, client);
        this.client = client; 
        
    }
    
    //Sendenmethode die die Hashtabelle durchläuft und an alle angemeldeten Clients die Nachricht die mitgegeben wird versendet. Zusätzlich mit dem Nutzernamen
    public void senden(String message, String UserName) throws IOException {
        Iterator it = Nutzer.keySet().iterator();
        String user=null;
        while(it.hasNext()){
            
            user= it.next().toString();
            client = (Socket) Nutzer.get(user);
            out= new DataOutputStream(client.getOutputStream());
            System.out.println(client+ message);
            out.writeUTF(UserName+ ":"+ message);
            
            
                   
        }
        
    }
    //Übergibt den Nutzername, damit dieser bekannt ist. Und speichert ihn in die Namensliste der online User
    public void verbinden(String s){
        this.UserName= s;
        onlineUser.add(s);
        
    }
    //Aufruf wenn der Client sich trennen will, der Nutzer wird aus Tabelle entfernt
    public void trennen(String UserName) throws IOException {
        Nutzer.remove(UserName);
        System.out.println("Nutzer "+ UserName+ " hat sich abgemeldet");
        System.out.println(Nutzer);
    }
    //Methode online gibt eine ArrayList mit allen angemeldeten Usern zurück.
    public ArrayList online() {
        
        //Neue Liste erstellen 
        ArrayList online = new ArrayList();
        
        //For-Schleife zum Durchlaufen der onlineUser liste
        for(int i=0; i<onlineUser.size(); i++){
            
            //hinzufügen der Online User zur ArrayList
            online.add(onlineUser.elementAt(i));
        }
                
        //Rckgabe der onlineListe
        return(online);
    }
    
    //Methode die zu übergebenem Nutzername den publicKey zurückgibt
    public Key holePublic(String Nutzername) throws FileNotFoundException, IOException, ClassNotFoundException {
         //Datei intitialisieren die Nutzernamenspezifisch gespeichert wurde
       File datei = new File(Nutzername+".key");
       // Erzeugen einer Variable
       Key publKey;
        try ( 
            // PublicKey aus der Datei mittels FileInput und ObjectInputStream lesen
            // in Variable publKey schreiben
            ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(datei))) {
            publKey = (Key) keyIn.readObject();
        }           
        //Den PublicKey zurückgeben
        return (publKey);
    }
    
    //Methode, die vom Nutzer übergebenen Namen mit dazugehörigem öffentlichen Key in einer Datei ablegt
    public void speicherKey(String UserName, Key key) throws KeyStoreException, IOException {
        File datei= new File(UserName+".key");
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(datei));
        out.writeObject(key);
        out.close();
        
        
    }

    public void verbindenUser(String header, String Nutzername, String ZielUser) throws IOException {
        Socket ziel;
        
        ziel= (Socket) Nutzer.get(ZielUser);
        
        out = new DataOutputStream(ziel.getOutputStream());
        out.writeUTF(header+":"+Nutzername);
        
    }

    void zeigeOff(String UserName) {
        onlineUser.remove(UserName);
    }

    //Methode nicht mehr in Verwendung. Siehe sendeAES
    void schluesselTausch(String header, byte[] key, String Nutzername, String ZielUser) throws IOException {
        Socket ziel;
        
        //Senden des Headers KEYs, zum initialisieren des Schlüsselaustausches
        verbindenUser("KEY", Nutzername, ZielUser);
        ziel= (Socket) Nutzer.get(ZielUser);
        System.out.println("Das ist der schlüssel: " +key);
        OutputStream out= ziel.getOutputStream();
        out.write(key);
        
        
        
        
       
    }

    void sendeAES(String aes, String ZielUser,String Nutzername) throws IOException {
        Socket ziel;
        
        ziel = (Socket) Nutzer.get(ZielUser);
        verbindenUser("KEY", Nutzername, ZielUser);
        DataOutputStream out = new DataOutputStream(ziel.getOutputStream());
        out.writeUTF(aes);
    }
   
    
    
    



}