/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg4enlinea;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author rodri
 */
public class matriz implements Runnable {
     private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private List<matriz> clientList;
    char matriz[][];
    
    public matriz(final Socket socket,final List<matriz> clientList) {
        this.socket = socket;
        this.clientList = clientList;
        createStream();
    }

    public matriz() {
    }
      
      
      
    @Override
    public void run() {
        boolean status=true;
        while (status) {
            llenar();
            genMatriz();
            jugar();
            
        }
    }
    
    private void readData(){
        String info;
         try {
             info = in.readUTF();
                  System.out.println(info);
         } catch (IOException ex) {
             Logger.getLogger(matriz.class.getName()).log(Level.SEVERE, null, ex);
         }
   
    }
      
         private void createStream() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
         
         public void sendData(int posicionColumna){
             Scanner input = new Scanner(System.in); 
              
            
         try {
             out.writeUTF(Integer.toString(posicionColumna));
             out.flush();
         } catch (IOException ex) {
             Logger.getLogger(matriz.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
         

    
    public void llenar(){
        matriz=new char[6][7];
    }
    
    public void genMatriz(){
        for(int i=0; i<matriz.length;i++){
            
            for(int j=0;j<matriz[i].length;j++){
                System.out.print(" | "+matriz[i][j]);
            }
            System.out.println(" | ");
        }
        for(int i=0;i<matriz.length;i++){
            System.out.print("----");
        }
        System.out.println("");
    }
    
    public void jugar(){
        boolean fin=false,turno=true;//si es true es turno del primer jugador
        int posicionColumna=0;
        char disco;
        Scanner input = new Scanner(System.in); 
       
        while(!fin){          
//                posicionColumna=Integer.parseInt(JOptionPane.showInputDialog("Jugador O, Inserte un disco rojo en una columna de 0-6"));
                  
                try {
                    if(turno){
                    String info = in.readUTF();
                     posicionColumna=Integer.parseInt(info);
                disco='O';
                
                    }else{
//                posicionColumna=Integer.parseInt(JOptionPane.showInputDialog("Jugador X, Inserte un disco azul en una columna de 0-6"));
                     System.out.println("Jugador X, Inserte un disco azul en una columna de 0-6");
                    posicionColumna=input.nextInt();
                    sendData(posicionColumna);
                disco='X';
                
            }
            while(posicionColumna <0 || posicionColumna >6){
                System.out.println( "Escoja un valor entre 0 y 6");
                posicionColumna=input.nextInt();
                
            }
            turno=!turno;
            if(soltarDisco(matriz,posicionColumna,disco)){
            turno=!turno;
        }else{
                genMatriz();
                if(status(matriz,posicionColumna,disco)){
                    fin=true;
                    int ronda=1;
                    System.out.println("Jugador "+ disco+" ha ganado la partida!");
                    ronda++;
                     System.out.println("\n -o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-");
                    System.out.println("\n Empezando ronda "+ronda+"!");
                    System.out.println();
//                    turno=!turno;
                }
            }
        
                } catch (IOException ex) {
                    Logger.getLogger(matriz.class.getName()).log(Level.SEVERE, null, ex);
                }
                 
    }
    }
    private boolean status(char matriz[][],int posicionColumna,char disco){
        int posicionFila=1;
        for(int i=0;i<matriz.length;i++){//de arriba hacia abajo
            if(matriz[i][posicionColumna]!=0){
                posicionFila=i;
                break;
            }
            if(revisaColumna(matriz,posicionColumna,disco,posicionFila)){
                return true;
            }  if(revisaFila(matriz,posicionColumna,disco,posicionFila)){
                return true;
            }if(revisaDiagMaj(matriz,posicionColumna,disco,posicionFila)){
                return true;
            }
             if(revisaDiagMen(matriz,posicionColumna,disco,posicionFila)){
                return true;
            }
        }
        
        for(int i=5;i>=0;i--){
            if(matriz[i][posicionColumna]==0){
                posicionFila=i;
                break;
            }
             if(revisaFila(matriz,posicionColumna,disco,posicionFila)){
                return true;
            }if(revisaDiagMaj(matriz,posicionColumna,disco,posicionFila)){
                return true;
            }
             if(revisaDiagMen(matriz,posicionColumna,disco,posicionFila)){
                return true;
            }
        }
        return false;
    }
    
//    private boolean revisaDiagMen(char matriz[][],int posicionColumna,char disco,int posicionFila){
//       
//    }
    
    private boolean revisaDiagMaj(char matriz[][],int posicionColumna,char disco,int posicionFila){
        int contadorDisco=1;
        for(int i=posicionFila-1,j=posicionColumna-1;i>=0 && j>=0;i--,j--){//abajo hacia arriba
            if(disco==matriz[i][j]){
                contadorDisco++;
            }else{
                break;
            }
            if(contadorDisco>=4){
                return true;
            }
        }
        
        for(int i=posicionFila+1,j=posicionColumna+1;i<matriz.length && j<matriz.length;i++,j++){//arriba hacia abajo
            if(disco==matriz[i][j]){
                contadorDisco++;
            }else{
                break;
            }
            if(contadorDisco>=4){
                return true;
            }
        }
        return false;
    }
    
    private boolean revisaDiagMen(char matriz[][],int posicionColumna,char disco,int posicionFila){
           int contadorDisco=1;
                for(int i=posicionFila-1,j=posicionColumna+1;i>=0 && j<matriz.length;i--,j++){//arriba hacia abajo
            if(disco==matriz[i][j]){
                contadorDisco++;
            }else{
                break;
            }
            if(contadorDisco>=3){
                return true;
            }
        }
        
                for(int i=posicionFila+1,j=posicionColumna-1;i<matriz.length && j>=0;i++,j--){//abajo hacia arriba
            if(disco==matriz[i][j]){
                contadorDisco++;
            }else{
                break;
            }
            if(contadorDisco>=4){
                return true;
            }
        }
   
   
        return false;
    }
   
    private boolean revisaFila(char matriz[][],int posicionColumna,char disco,int posicionFila){
       
        int contadorDisco=1;
        
        for(int i=posicionColumna-1;i>=0;i--){//revisa lado izquierdo de la matriz de arriba hacia abajo
            
            if(disco==matriz[posicionFila][i]){
                contadorDisco++;
            }else{
                break;
            } if(contadorDisco >=4){
                return true;
            }
            
            
        }
    
        for (int i=posicionColumna+1;i<matriz[0].length;i++){// revisa lado derecho de la matriz
            if(disco ==matriz[posicionFila][i]){
                contadorDisco++;
            }else{
                break;
            }  if(contadorDisco>=4){
                return true;
            }
      
        }
                      
        return false;
    }
    

    
    private boolean revisaColumna(char matriz[][],int posicionColumna,char disco,int posicionFila){
        int contadorDisco=1;
        if((posicionFila+4)<=6)
            for(int i =posicionFila+1;i<=(posicionFila+3);i++)
                if(disco==matriz[i][posicionColumna])
                    contadorDisco++;
                else
                    break;
                if(contadorDisco == 4)
                    return true;
                
        
         
        return false;
    }
    
    private boolean soltarDisco(char matriz[][],int posicionColumna,char disco){
        for(int i=matriz.length-1;i>=0;i--){
            if(matriz[i][posicionColumna]==0){
                matriz[i][posicionColumna]=disco;
                return false;
        }
        }
         System.out.println("Columna llena,"+ disco+" intentalo de nuevo");
        return true;
    }
    
    
}
