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

    // Tamaño de la ventana
    private final int ANCHO_VENTANA = 854;
    private final int ALTO_VENTANA = 480;

    public GamePanel() {
        setPreferredSize(new Dimension(ANCHO_VENTANA, ALTO_VENTANA));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        jugador = new Jugador(100, 7, 60, 40); // Ajusté la posición inicial
        entidades = new ArrayList<>();
        archivo = new ArchivoJuego("progreso.txt");

        // Ajustar posiciones y tamaños para la nueva dimensión

        // Suelo: ajustado a lo ancho de la ventana
        entidades.add(new Plataforma(0, ALTO_VENTANA - 50, ANCHO_VENTANA, 20, false));

        // Plataformas flotantes: ajustar posición vertical
        entidades.add(new Plataforma(200, ALTO_VENTANA - 200, 120, 20, true));
        entidades.add(new Plataforma(500, ALTO_VENTANA - 200, 120, 20, true));

        // Trampa: ajustar posición vertical
        entidades.add(new EnemigoTrampa(200, ALTO_VENTANA - 80, 100, 80));

        // Enemigos terrestres: ajustar posición vertical
        entidades.add(new EnemigoTerrestre(300, ALTO_VENTANA - 90, 40, 40));
        for(int i = 1; i<=5; i++){ // Reduje cantidad de enemigos para que quepan
            entidades.add(new EnemigoTerrestre(300+50*i, ALTO_VENTANA - 90, 40, 40));
        }

        // Enemigo volador: ajustar posición vertical
        entidades.add(new EnemigoVolador(500, ALTO_VENTANA - 300, 40, 40));

        // Paredes laterales: ajustar altura al tamaño de la ventana
        entidades.add(new Plataforma(0, 0, 40, ALTO_VENTANA, false)); // Pared izquierda
        entidades.add(new Plataforma(ANCHO_VENTANA - 40, 0, 40, ALTO_VENTANA, false)); // Pared derecha

        archivo.cargar(jugador);

        timer = new Timer(16, this);
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        jugador.actualizar();
        jugador.verificarColisiones(entidades);

        // Limitar al jugador para que no salga de la pantalla
        if (jugador.x < 40) jugador.x = 40;
        if (jugador.x + 40 > ANCHO_VENTANA - 40) jugador.x = ANCHO_VENTANA - 80;
        if (jugador.y > ALTO_VENTANA + 100) { // Si cae muy abajo, reposicionar
            jugador.x = 100;
            jugador.y = 7;
        }

        repaint();

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
        if (e.getKeyCode() == KeyEvent.VK_LEFT) jugador.setIzquierda(true);
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) jugador.setDerecha(true);
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
