
import java.security.Key;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/*RemoteImpl ist eine Klasse die um das UnicastRemoteObject erweitert ist und das Interface TestRemote implementiert.
  Die einzelnen Methoden des Interfaces sind hier enthalten und verweisen auf die gleichnamigen Methoden des Servers, 
  in welchem die eigentliche Funktionen der Methode ablaufen. Deshalb werden die Methoden hier nicht weiter kommentiert*/

public class RemoteImpl extends UnicastRemoteObject implements TestRemote {

    private Server server;
    
    
    public RemoteImpl( Server server) throws RemoteException, IOException{
        super();
        this.server =server;
                
    }
    
    @Override
    public boolean isLoginValid(String user) throws RemoteException {
        return user.equals("heiko");
    }

      
    
    @Override
    public void senden(String message, String UserName) throws RemoteException {
        try {
            server.senden(message, UserName);
        } catch (IOException ex) {
            }
    }

    @Override
    public void verbinden(String user) throws RemoteException {
        server.verbinden(user);
    }
    
    @Override
    public void trennen(String UserName) throws RemoteException{
        try {
            server.trennen(UserName);
        } catch (IOException ex) {
        }
    
    }

    @Override
    public ArrayList online() throws RemoteException {
        return(server.online());
    }

    @Override
    public Key holePublic(String Nutzername) throws RemoteException {
        Key key = null;
        try {
             key = server.holePublic(Nutzername);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(RemoteImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return(key);
    }

    @Override
    public void speicherKey(String UserName, Key key) throws RemoteException {
        try {
            server.speicherKey(UserName, key);
        } catch (KeyStoreException | IOException ex) {
           
        }
    }

    @Override
    public void verbindenUser(String header, String Nutzername, String ZielUser) throws RemoteException {
        try{
        server.verbindenUser(header,Nutzername,ZielUser);
    
        }catch (Exception e) {}
    }

    @Override
    public void zeigeOff(String UserName) throws RemoteException {
        server.zeigeOff(UserName);
        }

    @Override
    public void schluesselTausch(String header, byte[] key, String Nutzername, String ZielUser) throws RemoteException {
        try {
            server.schluesselTausch(header, key, Nutzername, ZielUser);
        } catch (IOException ex) {
        }
    }

    @Override
    public void sendeAES(String aes, String ZielUser, String Nutzername) throws RemoteException {
        try {
            server.sendeAES(aes, ZielUser, Nutzername);
        } catch (IOException ex) {
        }
    }
}
