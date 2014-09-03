
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
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

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
    //private ServerSocket socket;
    private SSLServerSocket socket ;
    private Hashtable Nutzer = new Hashtable();
    private String UserName ="kein Name übermittelt";
    private DataOutputStream out;
    private Socket client;
    Vector onlineUser = new Vector();
    
    
    
    
    
    public Server(int port) throws IOException, RemoteException, AlreadyBoundException, Exception{
        //Erstellen der Remoteimplementation
        RemoteImpl impl = new RemoteImpl(this);
        
        
        //Laden der Zertifikate bzw. des KeyStores, welcher das eigene Zertifikat enthält
        KeyStore ks  = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("keystore.jks"), "password".toCharArray());
        
        //Dem Server mitteilen, wo sein KeyStore liegt und welches Passwort gilt
        System.setProperty("javax.net.ssl.keyStore", "keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");

        
        //Erzeugen von Client und ServerSocketFactory
        SslRMIClientSocketFactory csf = new SslRMIClientSocketFactory();
        SslRMIServerSocketFactory ssf = new SslRMIServerSocketFactory();
        
        //Erstellen der registry auf Port 2222 mit dem SslRmiclientSocketFactory und SslRmiServerSocketFactory
        Registry registry = LocateRegistry.createRegistry(2222, csf,  ssf);
        
        
        //Erstellen der Registry auf Port 2222
        // Registry registry = LocateRegistry.createRegistry(2222);
        //Den Namen "remote" zum auffinden in die Registry binden mit der Remoteimplementation
        registry.bind("remote", impl);
        
        
        System.out.println("server startet...");
        //startet die Methode listen, die auf sich verbindende Clients wartet und aktzeptiert
        listen(port);
    
    }
    
    public static void main(String[] args) throws RemoteException, AlreadyBoundException, IOException, Exception{
        
        //Erstellt neuen Insanz des Servers, damit dieser startet
        new Server(8080);
    
    }
    
    
    private void listen(int port) throws IOException{
        //Erstellen eines Serversockets auf mitgegebenem Port
        
        //SSL ServerSocket erstellen auf mitgegebenem Port 
        SSLServerSocketFactory sslserversocketfactory =(SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
             socket =  (SSLServerSocket) sslserversocketfactory.createServerSocket(port);
         
            
        //Alter Socket ohne SSL 
        //socket = new ServerSocket(port);
        
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
    public void senden(String message, String UserName, String ZielUser) throws IOException {
        
            client = (Socket) Nutzer.get(ZielUser);
            out= new DataOutputStream(client.getOutputStream());
            System.out.println(client+ message);
            out.writeUTF(UserName+ ":"+ message);
            
                //An eigenen Nutzer auch ausgeben
            client = (Socket) Nutzer.get(UserName);
            out= new DataOutputStream(client.getOutputStream());
             out.writeUTF(UserName+ ":"+ message);
            
            
            
                   
        
        
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
    //Methode die zur Übermittlung von Nachrichrichten via Header funktioniert. Header sind KEY, VERBINDEN, TRENNEN, VERBINDUNG
    // je nach Header wahl, wird im Client unterschiedlich agiert
    public void verbindenUser(String header, String Nutzername, String ZielUser) throws IOException {
        Socket ziel;
        
        ziel= (SSLSocket) Nutzer.get(ZielUser);
        
        out = new DataOutputStream(ziel.getOutputStream());
        out.writeUTF(header+":"+Nutzername);
        
    }
    //Methode die den jeweiligen Nutzer aus der onlineListe nicht aber aus der UserListe schmeißt!
    void zeigeOff(String UserName) {
        onlineUser.remove(UserName);
    }

    //Methode die den verpackten AES an den Chat Partner übergibt
    public byte[] schluesselTausch(String Nutzername, String ZielUser) throws IOException {
         //Datei Variable anlegen von AesEncrypted.key
       File datei = new File("AesEncr-"+ZielUser+".key");
       //Liest die Datei in umgekehrter Reihenfolge wie sie gespeichert wurde wieder ein.
       DataInputStream in = new DataInputStream(new FileInputStream(datei));
        int length = in.readInt();
        byte[] wrappedKey = new byte[length];
        in.read(wrappedKey, 0, length);
        return(wrappedKey);
        
        
        
        
        
       
    }

    void speicherAES(byte[] aes, String ZielUser,String Nutzername) throws IOException {
        //Erzeugen einer Datei mti dem Namen AesEncr-Nutzername.key
        File datei = new File("AesEncr-"+Nutzername+".key");
        
        //Schickt den Header Key mit dem Nutzer an den ZielUser, damit dieser den KEY abholen kann
        verbindenUser("KEY", Nutzername, ZielUser);
        
        //Schreibt den Schlüssel in Datei mit der Länge des Schlüssel am Anfang
        DataOutputStream out = new DataOutputStream(new FileOutputStream(datei));
        out.writeInt(aes.length);
        out.write(aes);
        out.close();
    }
   
    
    
    



}