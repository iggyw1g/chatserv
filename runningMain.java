/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatserver;

/**
 *
 * @author alisa
 */
public class runningMain {
   //final static int SERVER_PORT = 12345;

   public static void main(final String[] args) {
      Thread t1 = new Thread(new Runnable() {
         public void run() {
            ChatServer.main(args);
         }
      });
      t1.start();
      Thread t2 = new Thread(new Runnable() {
         public void run() {
            ChatClient.main(args);
         }
      });
      t2.start();

      // wait some
      try{Thread.sleep(5000);}catch(Exception x){}
     // System.exit(0); // done

   }  // end main()



    
}
