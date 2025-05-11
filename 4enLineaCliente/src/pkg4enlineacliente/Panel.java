
package pkg4enlineacliente;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;



public class Panel extends Thread {
 private JLabel lbl;
 
 public Panel (JLabel lbl){
     this.lbl=lbl;
 }

 
 public void run(){
     while (true){
     Date hoy = new Date();
     SimpleDateFormat s= new SimpleDateFormat("hh: mm: ss:");
     lbl.setText(s.format(hoy));
     try{sleep(1000);
         
     }catch(Exception ex){
         
     }
     }
 }
}
    
    

