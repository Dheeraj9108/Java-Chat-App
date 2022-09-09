import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

import java.net.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;

public class client extends JFrame {

    Socket socket;

    BufferedReader br;
    PrintWriter out;


    // Declare components;
    private JLabel heading = new JLabel("Client Area");
    private JTextArea msgarea = new JTextArea();
    private JTextField msginp = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);

    public client() {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1", 7778);
            System.out.println("Connection done..");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            // startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEvents() {
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
                // System.out.println("key released"+e.getKeyCode());
                if(e.getKeyCode() == 10){
                    // System.out.println("You have pressed Enter");
                    String contentToSend = msginp.getText();
                    msgarea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    msginp.setText("");
                    msginp.requestFocus();
                }
            }

        });

    }

    private void createGUI(){
        this.setTitle("Client Messanger[END]");
        this.setSize(600,600);//width,height
        this.setLocationRelativeTo(null);//sets the window center
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// by clicking on X program terminates

        // coding for component
        heading.setFont(font);
        msgarea.setFont(font);
        msginp.setFont(font);
        // heading.setIcon(new ImageIcon("chat.png"));
        heading.setIcon(new ImageIcon("logo.jpg"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        msgarea.setEditable(false);
        msginp.setHorizontalAlignment(SwingConstants.CENTER);
        // setting frame layout
        this.setLayout(new BorderLayout());
        // adding the components to frame

        this.add(heading,BorderLayout.NORTH);//this meanse our window
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
                        System.out.println("Server Terminated the caht");
                        JOptionPane.showMessageDialog(this,"Serever Terminated the chat");
                        msginp.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Clent : " + msg);
                    msgarea.append("server :"+msg+"\n");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(r1).start();
    }
    // startWriting method
    public void startWriting() {
        // thread data user se lega and then send karega client tak
        Runnable r2 = () -> {
            System.out.println("Writer started.. ");
            while (true) {
                try {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This ic client");
        new client();
    }
}
