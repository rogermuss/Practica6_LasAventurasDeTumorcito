package Practica_6;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Jugador jugador;
    private ArrayList<Entidad> entidades;
    private ArchivoJuego archivo;

    public GamePanel() {
        setPreferredSize(new Dimension(1920, 1080));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        jugador = new Jugador(205, 7, 40, 40);
        entidades = new ArrayList<>();
        archivo = new ArchivoJuego("progreso.txt");

        //AGREGAR PLATAFORMAS ESCALABLES Y PLATAFORMAS FIRMES
        entidades.add(new Plataforma(0, 780, 1555, 20,false));
        entidades.add(new Plataforma(200, 630, 120, 20, true));
        entidades.add(new Plataforma(500, 630, 120, 20, true));
        entidades.add(new EnemigoTrampa( 200, 1000,100, 120));
        entidades.add(new EnemigoTerrestre(300, 780-40, 40, 40));
        for(int i = 1; i<=10; i++){
        entidades.add(new EnemigoTerrestre(300+50*i, 780-40, 40, 40));
        }
        entidades.add(new EnemigoVolador(500, 300, 40, 40));
        entidades.add(new Plataforma(0, 0, 40, 1000, false));
        entidades.add(new Plataforma(1500, 0, 40, 1000, false));


        archivo.cargar(jugador);

        timer = new Timer(16, this);
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        jugador.actualizar();
//        jugador.verificarColisiones(entidades);
        // Pruebas de distintas ficicas de colision
        jugador.fisicaSlime(entidades); // - Slime

        repaint();
        //Agregar verificacion para actualizar movimientos del enemigo por patron y a su vez
        for(Entidad ent:entidades){
            if(ent instanceof Enemigo){
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        jugador.dibujar(g);
        for (Entidad ent : entidades) {
            ent.dibujar(g);
        }
    }

    public void keyPressed(KeyEvent e) {
        int direcion = 0;
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            jugador.setIzquierda(true);
            direcion = 1;
        };
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            jugador.setDerecha(true);
            direcion = 2;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) jugador.saltar();
        if (e.getKeyCode() == KeyEvent.VK_SPACE) jugador.setDash();
        if (e.getKeyCode() == KeyEvent.VK_S) archivo.guardar(jugador);
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) jugador.setIzquierda(false);
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) jugador.setDerecha(false);
    }

    public void keyTyped(KeyEvent e) {}
}
