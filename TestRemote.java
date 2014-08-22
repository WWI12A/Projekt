

import java.net.Socket;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.Key;
import java.util.ArrayList;
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

// TestRemote ist das Interface welches die gemeinsamen Methoden deklariert
public interface TestRemote extends Remote {
    public boolean isLoginValid(String user) throws RemoteException;
    public void verbinden(String UserName) throws RemoteException;
    public void zeigeOff (String UserName) throws RemoteException;
    public void senden(String message, String UserName) throws RemoteException;
    public void trennen (String UserName) throws RemoteException;
    public ArrayList online () throws RemoteException;
    public Key holePublic (String Nutzername) throws RemoteException;
    public void speicherKey(String UserName, Key key) throws RemoteException;
    public void verbindenUser(String header, String Nutzername, String ZielUser) throws RemoteException;
    public void schluesselTausch(String header, byte[] key, String Nutzername, String ZielUser) throws RemoteException;
    public void sendeAES(String aes, String ZielUser, String Nutzername) throws RemoteException;
}
