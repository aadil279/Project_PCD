package game;

import environment.Direction;
import gui.BoardJComponent;
import gui.GameGuiMain;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.function.Predicate;

public class Client extends Thread {
    // TODO CRIAR CONSTRUTOR COMO TA NO ENUNCIADO


    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    private ObjectInputStream in;
    private PrintWriter out;
    private Socket serverSocket;
    private JFrame frame = new JFrame("client");
    private BoardJComponent board = new BoardJComponent(null, false);
    private boolean controller = true;

    @Override
    public void run() {
        makeConnections();
        createMainFrame();

        while (controller) { // O server trata de fechar a ligacao
            try {
                Game game = (Game) in.readObject();
                board.updateGame(game);         // Ao receber um novo game, temos que atualizar o nosso BoardJComponent board
                updateFrame(board);

                if (board.getLastPressedDirection() != null) {
                    System.out.println(" TECLA CLICADA" + board.getLastPressedDirection());
                    out.println(board.getLastPressedDirection().toString());
                    board.clearLastPressedDirection();
                }

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            in.close();
            out.close();
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void makeConnections() {
        try {
            serverSocket = new Socket("localhost", Game.SERVER_PORT);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream())), true);
            in = new ObjectInputStream(serverSocket.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createMainFrame() {
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        jframeSupport();
    }
    private void updateFrame(BoardJComponent board) {
        frame.getContentPane().removeAll();
        frame.add(board);
        frame.addKeyListener(board);
        frame.setVisible(true);
        frame.repaint();
    }

    private void jframeSupport() {
        JFrame frameJ = new JFrame("Quer sair?");
        frameJ.setSize(100, 100);
        frameJ.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JButton button = new JButton("Sair");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller = false;
                System.exit(0);
            }
        });
        frameJ.add(button);
        frameJ.setVisible(true);
    }
}