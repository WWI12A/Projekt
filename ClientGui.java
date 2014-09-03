
import java.io.*;
import java.net.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.swing.JOptionPane;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Heiko
 */
public class ClientGui extends javax.swing.JFrame implements Runnable {

    //Deklaration verschiedener Variablen
  
    //private Socket s;
    private SSLSocket s;
    private DataInputStream in;
    private TestRemote remote;
    private Thread thread;
    private int schleife =0;
    private AES aes;
    String ZielUser= null ;
    
    
    
    /**
     * Creates new form ClientGui
     * @throws java.rmi.RemoteException
     * @throws java.rmi.NotBoundException
     */
    //Konstruktor
    public ClientGui() throws RemoteException, NotBoundException, Exception {
        // Layout initiieren
        initComponents();
        
        //Dem Client mitteilen, wo sein TrustStore liegt. Dieser wurde zuvor mit Keytool erzeugt:
        //keytool erzeugt KeyStore dann wird selbstsigniertes Zertifikat exportiert um im Client importiert und als Vertrauenswürdig erteilt
        System.setProperty("javax.net.ssl.trustStore", "clientstore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");
       
        //Dem Client mitteilen, wo sein KeyStore liegt, in diesem Fall gleichzeitig sein TrustStore
        System.setProperty("javax.net.ssl.keyStore", "clientstore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");

        //KeyStore aus der Datei laden
        KeyStore ks  = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("clientstore.jks"), "password".toCharArray());
            
        //   SSLServerSocketFactory initialisieren
        SSLServerSocketFactory sssf ;
        try{
        //SSLRMIClientSocketFactory erzeugen
        SslRMIClientSocketFactory csf = new SslRMIClientSocketFactory();
        //Verbindung aufbauen zum Registry auf Localhost, Port 2222 und dem SslRMiClientSocketFactory, um mittels RMI Methodenaufrufe zu starten.
        Registry registry =LocateRegistry.getRegistry("localhost", 2222, csf);
        
        
        //Verbindung aufbauen zum Registry um mittels RMI Methodenaufrufe zu starten. Ohne RMI,deshalbt auskommentiert
        //Registry registry =LocateRegistry.getRegistry("localhost", 2222);
        
        //Anlegenen eines TestRemotes Objektes um auf die Methoden zugreifen zu können.
        remote= (TestRemote) registry.lookup("remote");
        }catch (RemoteException | NotBoundException e){
        
        System.out.println(e);
        }
        
        //Überprüfen ob Methodenaufruf klappt
        System.out.println(remote.isLoginValid("heiko"));
        //AES Objekt erzeugen um auf die Methoden der Klasse zuzugreifen zu können.
        aes = new AES();
        //RSA Schlüsselpaar erzeugen und in Datei speichern
        generateRSA();
        //Liste wer online ist updaten
        online(remote.online());
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        list1 = new java.awt.List();
        jScrollPane1 = new javax.swing.JScrollPane();
        Anzeigefeld = new javax.swing.JTextArea();
        Eingabefeld = new javax.swing.JTextField();
        Senden = new javax.swing.JButton();
        Verbinden = new javax.swing.JButton();
        UserName = new javax.swing.JTextField();
        Trennen = new javax.swing.JButton();
        UserListe = new java.awt.List();
        UserVerbinden = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        Anzeigefeld.setEditable(false);
        Anzeigefeld.setColumns(20);
        Anzeigefeld.setRows(5);
        jScrollPane1.setViewportView(Anzeigefeld);

        Senden.setText("Senden");
        Senden.setEnabled(false);
        Senden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendenActionPerformed(evt);
            }
        });

        Verbinden.setText("Verbinden mit Server");
        Verbinden.setEnabled(false);
        Verbinden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VerbindenActionPerformed(evt);
            }
        });

        UserName.setText("Name eingeben...");
        UserName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UserNameMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                UserNameMouseEntered(evt);
            }
        });
        UserName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserNameActionPerformed(evt);
            }
        });
        UserName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UserNameKeyPressed(evt);
            }
        });

        Trennen.setText("Trennen");
        Trennen.setEnabled(false);
        Trennen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TrennenActionPerformed(evt);
            }
        });

        UserListe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                UserListeMouseEntered(evt);
            }
        });

        UserVerbinden.setText("Verbinden mit User");
        UserVerbinden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserVerbindenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Eingabefeld, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Senden, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(UserName)
                            .addComponent(Verbinden, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Trennen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(UserListe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(UserVerbinden, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(UserVerbinden, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(UserListe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Eingabefeld, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(UserName))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Senden)
                    .addComponent(Verbinden))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Trennen)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Aufruf bei drücken von "Senden"
    private void SendenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendenActionPerformed
       
        try {
    //Wenn Senden gedrückt wird, inhalt von Eingabefeld an Server schicken, den Stringn davor mittels AES verschlüsseln
            remote.senden(aes.verschlüsseln(aes.SchlüsselAusDatei(), Eingabefeld.getText()), UserName.getText(), ZielUser);
        } catch (RemoteException ex) {
           
        } catch (Exception ex) {
        }
           //Anzeigefeld.append("Ich: " +Eingabefeld.getText()+ "\n" );
           Eingabefeld.setText("");
       
    }//GEN-LAST:event_SendenActionPerformed

    private void UserNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserNameActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_UserNameActionPerformed
    
    //Aufruf bei drücken von "Verbinden"
    private void VerbindenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VerbindenActionPerformed
        
            //Variable um den Thread bei Aufruf von "Trennen" anzuhalten und wieder zu starten. 1 heißt verbunden, 0 heißt getrennt
            schleife=1;
            //Variable damit Verbindung wirklich aufgebaut wird
            boolean starten =true;
        
            //Usernamen-Feld darf nicht leer sein, ansonsten Aufforderung!
            if(!UserName.getText().equals("") ){
                //Durchlaufen der Liste aller User die online sind, um doppelte Name auszuschließen
                for(int i=0; i<UserListe.getItemCount(); i++){
                    if(UserListe.getItem(i).equals(UserName.getText())){
                        Anzeigefeld.append("Nutzer existiert bereits...\n Bitte anderen Namen verwenden! \n");
                        starten =false;
                    }                
                }
            }
                if(starten == true){
                    
             Anzeigefeld.append("Verbindung wird aufgebaut..\n");
             
            //Aufruf der Methode verbinden des Servers, übermittelt den Nutzernamen 
            try {
                remote.verbinden(UserName.getText());
            } catch (RemoteException ex) {
                
            }
             try {
                 //Alter Socket ohne SSL
                 // s = new Socket ("localhost" , 8080);

            // Socket mit SSL zum Server aufbauen auf Port 8080 und Localhost
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
             s = (SSLSocket) sslsocketfactory.createSocket("localhost", 8080);
            
                 
            
            Anzeigefeld.append("Erfolgreich verbunden mit: " +s +"\n");
                                  
        } catch (IOException ex) {
            Anzeigefeld.setText("Verbindung fehlgeschlagen! \nServer nicht gefunden! Bitte neustarten!");     
                }                 
       
       // Aufruf der Methode zum Übermitteln des öffentlichen Schlüssels an Server inklusive Nutzernamen
       try {
            keyAnServer();
        } catch (RemoteException ex) {
            
        } catch (IOException | ClassNotFoundException ex) {
        }
       
            // Verschiedene Buttons sperren bzw. freigeben
             
             Verbinden.setEnabled(false);
             Trennen.setEnabled(true);
             UserName.setEnabled(false);
             
             // neuen Thread starten, um ständig auf Nachrichten zu warten und onlineListe zu überprüfen.
             thread =  new Thread(this);
             thread.start();       
            }      
    }//GEN-LAST:event_VerbindenActionPerformed

    private void UserNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UserNameKeyPressed
        
    }//GEN-LAST:event_UserNameKeyPressed

    private void UserNameMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UserNameMouseEntered
        
    }//GEN-LAST:event_UserNameMouseEntered

    private void UserNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UserNameMouseClicked
       //Bei click in das UserNamen-Feld dieses Frei machen und Verbinden-Button verfügbarmachen. Aber nur wenn getrennt ist!
        if (schleife==0){
            UserName.setText("");
            Verbinden.setEnabled(true);
       }
    }//GEN-LAST:event_UserNameMouseClicked

    // Trennen-Button drücken:
    private void TrennenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TrennenActionPerformed
        
        // Socket schließen
        try {
            
                s.close();
            
        } catch (IOException ex) {
            Anzeigefeld.append("Kann nicht geschlossen werden.. +\n");
        }
        
        // Trennen vom Server aufrufen, um User aus der Liste zu löschen
        try {
            remote.zeigeOff(UserName.getText());
            remote.trennen(UserName.getText());
        } catch (RemoteException ex) {
            System.out.println(ex);
            }
        
        // Verschiedene Buttones sperren oder freigeben
        Verbinden.setEnabled(true);
        Trennen.setEnabled(false);
        UserName.setEnabled(true);
        // Zustand sagt getrennt!
        schleife=0;
       
        
    }//GEN-LAST:event_TrennenActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
     
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
   // Methode falls nur das Fenster geschlossen wird, nicht aber zuvor getrennt!
        try {
            //Socket nur Schließen, wenn auch einer gestartet wurde
            if(s == null) {
            } else {
            s.close();}
        } catch (IOException ex) {
            Anzeigefeld.append("Kann nicht geschlossen werden.. +\n");
        }
        
        // Trennen vom Server aufrufen, um User aus der Liste zu löschen
        try {
            remote.trennen(UserName.getText());
        } catch (RemoteException ex) {
            System.out.println(ex);
            }    }//GEN-LAST:event_formWindowClosing
    
    //Verbindung mit einem ausgewählten User soll aufgebaut werden
    private void UserVerbindenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserVerbindenActionPerformed
        // TODO add your handling code here:
        
        
        
        
        
        //Überprüfung ob verbunden oder getrennt werden soll
        if(UserVerbinden.getText().equals("Verbinden mit User")){
        //Ausgewählten User aus der Online-Liste als ZielUser in Variable speichern
        ZielUser = UserListe.getSelectedItem();
        //Falls kein User / oder der Eigene ausgewählt wurde, eine Warnung ausgeben!
        if(ZielUser == null || ZielUser.equals(UserName.getText())){
            Anzeigefeld.append("Bitte User/anderen User auswählen und 'Verbinden mit User' klicken");
        }else{
        //Wenn einer ausgewählt ist, die UserListe deaktivieren    
        UserListe.setEnabled(false);
            
        // Button-Namen ändern damit getrennt werden kann
        UserVerbinden.setText("Trennen von User");
        //Senden-Button verfügbar machen
        Senden.setEnabled(true);
        
            try {
                //Offline anzeigen um keine weiteren Anfragen zu erhalten
                remote.zeigeOff(UserName.getText());
                //Aufruf der Server-Methode zum Verbinden mit Client
                remote.verbindenUser("VERBINDUNG", UserName.getText(), ZielUser);
                //AES Schlüssel anlegen
                 aes.SchlüsselInDatei();
                
            } catch (RemoteException ex) {
            } catch (Exception ex) {
                Logger.getLogger(ClientGui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
      
    }//GEN-LAST:event_UserVerbindenActionPerformed
//Was passiert wenn getrennt werden soll
        else if(UserVerbinden.getText().equals("Trennen von User")){
            //Userliste wieder verfügbar machen
            UserListe.setEnabled(true);
            //Den Button-Namen ändern
            UserVerbinden.setText("Verbinden mit User");
            //Senden-Button sperren
            Senden.setEnabled(false);
            
            
            try {
                //dem Gegenüber mitteilen, dass man den Chat verlassen hat
                remote.verbindenUser("TRENNEN", UserName.getText(), ZielUser);
                //Wieder zur online Liste hinzufügen
                remote.verbinden(UserName.getText());
            } catch (RemoteException ex) {
            }
           
        
        }
    }
    
    private void UserListeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UserListeMouseEntered
        try {
            // TODO add your handling code here:
            online(remote.online());
        } catch (RemoteException ex) {
        }
    }//GEN-LAST:event_UserListeMouseEntered
        
    
    
    /**
     * @param args the command line arguments
     * @throws java.rmi.RemoteException
     * @throws java.rmi.NotBoundException
     */
    public static void main(String args[]) throws RemoteException, NotBoundException {
        /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(ClientGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(ClientGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(ClientGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(ClientGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new ClientGui().setVisible(true);
                } catch (RemoteException | NotBoundException ex) {
                    Logger.getLogger(ClientGui.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(ClientGui.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea Anzeigefeld;
    private javax.swing.JTextField Eingabefeld;
    private javax.swing.JButton Senden;
    private javax.swing.JButton Trennen;
    private java.awt.List UserListe;
    private javax.swing.JTextField UserName;
    private javax.swing.JButton UserVerbinden;
    private javax.swing.JButton Verbinden;
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.List list1;
    // End of variables declaration//GEN-END:variables

    @Override
    // Methode run die bei Aufruf des Threads gestartet wird
    public void run() {
        //Endlosschleife zum abhören was der Server sedent. Neuen InputStream erzeugen und in Anzeigefenster ausgeben
        while(schleife == 1){
    
        try {
           // Methode online gibt die Nutzernamen der online-verfügabaren Usern zurück vom Server. Die eigene Methode schriebt diese in die UserListe. 
           online(remote.online());
           in = new DataInputStream(s.getInputStream());

            String lesen = in.readUTF();
        

            // Trennt den Einkommenden String am ":" um Username von der Nachricht zu trennen
            String [] nachrichten = lesen.split(":");
            
            //Überprüfen ob ein Header namens VERBINDUNG geschickt wird um entsprechend zu reagieren
            if(nachrichten[0].equals("VERBINDUNG")){
                //Aufruf einer MessageBox, dass Verbindung aufgenommen werden möchte.
                int eingabe = JOptionPane.showConfirmDialog(null,
                                                            "Wollen sie eine Verbindung mit " + nachrichten[1] +" aufbauen?",
                                                            "Verbindungsanfrage",
                                                            JOptionPane.YES_NO_CANCEL_OPTION);
                //Überprüfen der Eingabe in die Messagebox
                //Wenn 'Ja' gedrückt wurde
                if(eingabe == 0){
                    //UserListe sperren
                    UserListe.setEnabled(false);
                    //Verbinden mit User- Button auf trennen stellen
                    UserVerbinden.setText("Trennen von User");
                    //Offline Anzeigen um weitere Anfragen zu unterbinden
                    remote.zeigeOff(UserName.getText());
                    //Rückantwort,dass Verbindung aufgebaut werden soll!
                    remote.verbindenUser("VERBINDEN", UserName.getText(), nachrichten[1]);
                    //Übergabe des Namens des Chat-Partners in die globale Variable 
                    ZielUser= nachrichten[1];
                
                }else{
                    remote.verbindenUser("TRENNEN", UserName.getText() ,nachrichten[1] );
                
                }           
            //Wenn die Nachricht den Header 'TRENNEN' enthält, entweder bei beendigung oder bei ablehnen der Verbindung
            }else if(nachrichten[0].equals("TRENNEN")){
                    //Nachricht ausgeben, dass getrennt wird bzw. abgelehnt wurde
                    Anzeigefeld.append("Verbindung von "+ nachrichten[1]+ " wurde getrennt/abgelehnt! \n");
                    //UserListe entsperren
                    UserListe.setEnabled(true);
                    //Verbinden mit User- Button auf Verbinden stellen
                    UserVerbinden.setText("Verbinden mit User");
                    //wieder als online Anzeigen 
                    remote.verbinden(UserName.getText());
            
                    //wird aufgerufen wenn VERBINDEN als Header mitgegeben wird
            }else if(nachrichten[0].equals("VERBINDEN")){
                //Ausgabe dass Schlüsselaustausch stattfinden kann
                Anzeigefeld.append("Schlüssel wird an "+nachrichten[1]+ " gesendet \n");

                //Den eigenen AES Schlüssel mit öffentlichem Schlüssel des Gegenübers verschlüsseln
                EncryptAES(nachrichten[1]);
                
                //Aufruf der Methode zum Schlüsselaustausch. Überträgt byte[] des Schlüssels
                AESanServer(nachrichten[1]);      
                
            //wird aufgerufen wenn der Header KEY mitgegeben wird.
            }else if(nachrichten[0].equals("KEY")){
                
                // Aufruf der Methode Decrypt AES mit der Methode vom Server holeAES
                DecryptAES(remote.schluesselTausch(UserName.getText(), nachrichten[1]));
                
                //Senden-Button freigeben
                Senden.setEnabled(true);
                
               // Anzeigen, das Schlüssel erhalten wurde und Kommunikation starten kann
                Anzeigefeld.append("Key von "+ nachrichten[1] +" erhalten. Kommunikation kann starten! \n");
                
               

            
            }
            
            
            // Gibt die Nachricht die verschlüsselt vom Server kommt, entschlüsselt aus!
            Anzeigefeld.append(nachrichten[0] + ": "+  aes.entschlüsseln(aes.SchlüsselAusDatei(), nachrichten[1])+   "\n");

        } catch (IOException ex) {
        }   catch (Exception ex) {
            }
    }
}
    //Methode die die Usernamen in die UserListe anhängt. Sie nimmt eine ArrayList entgegen und durchläuft diese einzeln in einer For-Schleife
    public final void online (ArrayList users) throws RemoteException{
        String i;
        UserListe.removeAll();
        for (Object user : users) {
            i = user.toString();
            UserListe.add(i);
        }
        
    
    }
    //Methode die den mit RSA verschlüsselten AES-Schlüssel an den Server weiterleiten soll
    public void AESanServer(String ZielUser) throws FileNotFoundException, IOException, ClassNotFoundException {
        
        
        
        //Datei Variable anlegen von AesEncrypted.key
       File datei = new File("AesEncrypted.key");
       //Liest die Datei in umgekehrter Reihenfolge wie sie gespeichert wurde wieder ein.
       DataInputStream in = new DataInputStream(new FileInputStream(datei));
        int length = in.readInt();
        byte[] wrappedKey = new byte[length];
        in.read(wrappedKey, 0, length);
        
              
       //Aufruf der Methode speicherAES vom Server und Key und Nutzername übermitteln
       remote.speicherAES(wrappedKey, ZielUser, UserName.getText());
       //Überprüfung ob es geklappt hat
       System.out.println("es stimmt");

    
    }
    //Methode die den AES Schlüssel verschlüsselt und in Datei ablegt
    public void EncryptAES(String ZielUser) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, FileNotFoundException, IOException, InvalidKeyException, Exception{
        
        //Erzeugt eine Datei mit folgendem Namen
        File datei = new File("AesEncrypted.key");
        
        //Erzeugt einen RSA Cipher
        Cipher cipher = Cipher.getInstance("RSA");
        //Initialisiert den Cipher mit Wrap-Modus und dem öffentlichen Schlüssel des ZielUsers
        cipher.init(Cipher.WRAP_MODE, remote.holePublic(ZielUser));
        
        //Ezeugt byte Array des verpackten AES-Schlüssel mit RSA
        byte[] wrappedKey = cipher.wrap(aes.SchlüsselAusDatei());
        //Schreibt den Schlüssel in Datei mit der Länge des Schlüssel am Anfang
        DataOutputStream out = new DataOutputStream(new FileOutputStream(datei));
        out.writeInt(wrappedKey.length);
        out.write(wrappedKey);
        out.close();
    
    
    }
    
    //Funktion zum Entschlhüssen mittels RSA
    public void DecryptAES(byte[] encrAES) throws FileNotFoundException, IOException, ClassNotFoundException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException{
        
//hole den privaten Schlüssel
        File datei = new File("priv.key");
        ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(datei));
        Key privateKey = (Key) keyIn.readObject();
        keyIn.close();
        System.out.println(privateKey);
        
        //Entschlüssle das Übergebene Byte Array mittels RSA und privatem Schlüssel
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.UNWRAP_MODE, privateKey);
        Key key = cipher.unwrap(encrAES, "AES", Cipher.SECRET_KEY);
        
        //Datei für den AES KEy erzeugen
        File aeskey = new File("AES.key");
        
        //Den Schlüssel in die Datei schreiben
        FileOutputStream keyfos = new FileOutputStream(aeskey);
        byte[] AES = key.getEncoded();
        keyfos.write(AES);
        keyfos.close();
      
        
        
        
        
    
    
    
    }
    
    // Holt den public Key aus der Datei und übermittelt ihn an den Server
    public void keyAnServer() throws FileNotFoundException, RemoteException, IOException, ClassNotFoundException{
       //Datei intitialisieren von publ.key
       File datei = new File("publ.key");
       // Erzeugen einer Variable
       Key publKey;
        try ( 
            // PublicKey aus der Datei mittels FileInput und ObjectInputStream lesen
            // in Variable publKey schreiben
            ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(datei))) {
            publKey = (Key) keyIn.readObject();
            
        }           
              
       //Aufruf der Methode speicherKey vom Server und Key und Nutzername übermitteln
       remote.speicherKey(UserName.getText(), publKey);
    }
    
    

     // Methode zum erzeugen der RSA keys. Schreibt öffentlichen und privaten Schlüssel in Datei
public static void generateRSA() throws NoSuchAlgorithmException
{
    try {
    //Erstellen von Files für das Speichern der Schlüssel
        File priv = new File("priv.key");
        File publ = new File("publ.key");
    //Erzeugt das Schlüsselpaar mittels RSA
        KeyPairGenerator pairgen = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = new SecureRandom();
        pairgen.initialize(512, random);
        KeyPair keyPair = pairgen.generateKeyPair();
    //öffnet OutputStream zum Schreiben in Datei und holt mittels getPublic den öffentlichen Schlüssel.
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(publ));
        out.writeObject(keyPair.getPublic());
        out.close();
    //öffnet OutputStream und mittels getPrivate den privaten Schlüssel um ihn in Datei zu schreiben.    
        out = new ObjectOutputStream(new FileOutputStream(priv));
        out.writeObject(keyPair.getPrivate());
        out.close();
    } catch (IOException | GeneralSecurityException e) {
        
    }
}   
    
   
//Ab hier die Klasse zur AES Verschlüsselung
public final class AES {
    
    public AES() throws Exception{
         //SchlüsselInDatei();
         
         
}

public final void SchlüsselInDatei() throws Exception{
        // Datei erzeugen
    File datei = new File("AES.key");

    // Schlüssel erzeugen
    KeyGenerator keygen = KeyGenerator.getInstance("AES");
    keygen.init(128);
    Key key = keygen.generateKey();

    // Schlüssel in die Datei schreiben
    byte[] bytes = key.getEncoded();
    FileOutputStream keyfos = new FileOutputStream(datei);
    keyfos.write(bytes);
    keyfos.close();
    
}

public final byte[] SchlüsselTausch() throws Exception{
    //Neues File namens datei erstellen
    File datei = new File("AES.key");
    
    //Key auslesen
    FileInputStream fis = new FileInputStream(datei);
    byte[] encodedKey = new byte[(int) datei.length()];
    fis.read(encodedKey);
    fis.close();
    
    System.out.println(encodedKey);
    //Das byte[] des Schlüssels zurückgeben
    return(encodedKey);
}

public final SecretKey SchlüsselAusDatei() throws Exception{
        //Datei
       File datei = new File("AES.key");

       // Key lesen
       FileInputStream fis = new FileInputStream(datei);
       byte[] encodedKey = new byte[(int) datei.length()];
       fis.read(encodedKey);
       fis.close();
       
       // generiere Key
       SecretKey key = new SecretKeySpec(encodedKey, "AES");     
       
       return(key);
       


}
public String verschlüsseln(SecretKey key, String nachricht) throws Exception{
    

    // Verschluesseln
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] encrypted = cipher.doFinal(nachricht.getBytes());

    // bytes zu Base64-String konvertieren (dient der Lesbarkeit)
    BASE64Encoder myEncoder = new BASE64Encoder();
    String geheim = myEncoder.encode(encrypted);

    // Ergebnis
       return(geheim);

}
public String entschlüsseln(SecretKey key, String nachricht) throws Exception{
    // BASE64 String zu Byte-Array konvertieren
    BASE64Decoder myDecoder2 = new BASE64Decoder();
    byte[] crypted2 = myDecoder2.decodeBuffer(nachricht);

    // Entschluesseln
    Cipher cipher2 = Cipher.getInstance("AES");
    cipher2.init(Cipher.DECRYPT_MODE, key);
    byte[] cipherData2 = cipher2.doFinal(crypted2);
    String erg = new String(cipherData2);

    // Klartext
    return(erg);

}

   
    
}    
    
    

}
