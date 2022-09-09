import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class server extends JFrame{

    ServerSocket server;
    Socket socket;
    BufferedReader br;// to read
    PrintWriter out;// to write

    // declare components
    private JLabel heading = new JLabel("Server Area");
    private JTextArea msgarea = new JTextArea();
    private JTextField msginp = new JTextField();
    private Font font = new Font("Robot",Font.PLAIN,20);

    // constructor ..
    public server() {
        try {
            server = new ServerSocket(7778);// server will run on 7777 port so client can connect to the server using
            //                                 // this port number
            System.out.println("Server is raedy to accept the connections....");
            System.out.println("Waiting for the connections....");
            socket = server.accept();// here we are storing the object of the client .when the client try to connect
            //                          // it return the object whic we are storing in socket

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            eventHandle();
            startReading();
            // startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void eventHandle() {
        msginp.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                if(e.getKeyCode() == 10){
                    String content = msginp.getText();
                    msgarea.append("Me :"+content+"\n");
                    out.println(content);
                    out.flush();
                    msginp.setText("");
                    msginp.requestFocus();
                }
                
            }

        });

    }
    private void createGUI(){
        // Initiall settings 
        this.setTitle("Server Messanger[END]");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        heading.setFont(font);
        msgarea.setFont(font);
        msginp.setFont(font);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        msgarea.setEditable(false);
        msginp.setHorizontalAlignment(SwingConstants.CENTER);
        this.setLayout(new BorderLayout());

        this.add(heading,BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(msgarea);
        this.add(scrollPane,BorderLayout.CENTER);
        this.add(msginp,BorderLayout.SOUTH);

        this.setVisible(true);
    }
    
    // startReadng method
    public void startReading() {
        // thread read karke deta rahega
        Runnable r1 = () -> {
            System.out.println("Reader started..");
            try {

                while (!socket.isClosed()) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        JOptionPane.showMessageDialog(this,"Client left the chat");
                        msginp.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Clent : " + msg+"\n");
                    msgarea.append("Clent : " + msg+"\n");

                }
            } catch (Exception e) {
                System.out.println("Connection closed");
            }
        };
        new Thread(r1).start();
    }
    // startWriting method
    public void startWriting() {
        // thread data user se lega and then send karega client tak
        Runnable r2 = () -> {
            System.out.println("Writer started.. ");
            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }
  
    public static void main(String[] args) {
        System.out.println("This is server");
        new server();
    }
}