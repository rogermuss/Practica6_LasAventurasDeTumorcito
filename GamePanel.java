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

    // Referencia al enemigo volador para actualizarlo
    private EnemigoVolador enemigoMovil;

    private final int ANCHO_VENTANA = 854;
    private final int ALTO_VENTANA = 480;

    private final int TAMANO_JUGADOR = 40;
    private final int TAMANO_ENEMIGO = 40;

    private final int ALTURA_SUELO = ALTO_VENTANA - 40;
    private final int ALTURA_PLATAFORMA_2 = ALTO_VENTANA - 280;

    private final int ANCHO_PARED = 40;

    public GamePanel() {
        setPreferredSize(new Dimension(ANCHO_VENTANA, ALTO_VENTANA));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        jugador = new Jugador(60, ALTURA_SUELO - TAMANO_JUGADOR, TAMANO_JUGADOR+10, TAMANO_JUGADOR);
        entidades = new ArrayList<>();
        archivo = new ArchivoJuego("progreso.txt");

        crearNivel();

        archivo.cargar(jugador);

        timer = new Timer(16, this);
        timer.start();
    }

    private void crearNivel() {

        entidades.add(new Plataforma(0, 0, ANCHO_PARED, ALTO_VENTANA, true));
        entidades.add(new Plataforma(ANCHO_VENTANA - ANCHO_PARED, 0, ANCHO_PARED, ALTO_VENTANA, false));


        entidades.add(new Plataforma(ANCHO_PARED, ALTURA_SUELO, ANCHO_VENTANA - 2*ANCHO_PARED, 40, false));


        int posXEnemigo1 = 120;
        int posXEnemigo2 = posXEnemigo1 + TAMANO_ENEMIGO + 200;
        int posXEnemigo3 = posXEnemigo2 + TAMANO_ENEMIGO + 200;

        entidades.add(new EnemigoTerrestre(posXEnemigo1, ALTURA_SUELO - TAMANO_ENEMIGO, TAMANO_ENEMIGO, TAMANO_ENEMIGO));
        entidades.add(new EnemigoTerrestre(posXEnemigo2, ALTURA_SUELO - TAMANO_ENEMIGO, TAMANO_ENEMIGO, TAMANO_ENEMIGO));
        entidades.add(new EnemigoTerrestre(posXEnemigo3, ALTURA_SUELO - TAMANO_ENEMIGO, TAMANO_ENEMIGO, TAMANO_ENEMIGO));


        int anchoPlatforma2 = 600;
        entidades.add(new Plataforma(ANCHO_PARED, ALTURA_PLATAFORMA_2, anchoPlatforma2, 20, false));


        int posXTriangulo1 = 150;
        int posXTriangulo2 = posXTriangulo1 + TAMANO_ENEMIGO + 100;
        int posXTriangulo3 = posXTriangulo2 + TAMANO_ENEMIGO + 100;
        int posXTriangulo4 = posXTriangulo3 + TAMANO_ENEMIGO + 100;

        entidades.add(new EnemigoTrampa(posXTriangulo1, ALTURA_PLATAFORMA_2 - TAMANO_ENEMIGO, TAMANO_ENEMIGO, TAMANO_ENEMIGO));
        entidades.add(new EnemigoTrampa(posXTriangulo2, ALTURA_PLATAFORMA_2 - TAMANO_ENEMIGO, TAMANO_ENEMIGO, TAMANO_ENEMIGO));
        entidades.add(new EnemigoTrampa(posXTriangulo3, ALTURA_PLATAFORMA_2 - TAMANO_ENEMIGO, TAMANO_ENEMIGO, TAMANO_ENEMIGO));
        entidades.add(new EnemigoTrampa(posXTriangulo4, ALTURA_PLATAFORMA_2 - TAMANO_ENEMIGO, TAMANO_ENEMIGO, TAMANO_ENEMIGO));


        int posXPlataformaSubida = anchoPlatforma2 + 60;
        int anchoPlataformaSubida = 60;
        entidades.add(new Plataforma(posXPlataformaSubida, ALTURA_PLATAFORMA_2 + 70, anchoPlataformaSubida, 20, false));


        int tamanoVolador = 30;


        int posXVolador = posXPlataformaSubida + anchoPlataformaSubida + 10;
        int posYVolador = ALTURA_PLATAFORMA_2 + 70 - tamanoVolador - 10;


        int limiteIzquierdo = posXPlataformaSubida + anchoPlataformaSubida;
        int limiteDerecho = ANCHO_VENTANA - ANCHO_PARED;


        enemigoMovil = new EnemigoVolador(posXVolador, posYVolador, tamanoVolador, tamanoVolador, limiteIzquierdo, limiteDerecho);
        entidades.add(enemigoMovil);


    }

    private void manejarColisionesParedes() {
        Rectangle jugadorRect = jugador.getRect();

        Rectangle paredIzquierda = new Rectangle(0, 0, ANCHO_PARED, ALTO_VENTANA);
        if (jugadorRect.intersects(paredIzquierda)) {
            jugador.x = ANCHO_PARED;
        }

        Rectangle paredDerecha = new Rectangle(ANCHO_VENTANA - ANCHO_PARED, 0, ANCHO_PARED, ALTO_VENTANA);
        if (jugadorRect.intersects(paredDerecha)) {
            jugador.x = ANCHO_VENTANA - ANCHO_PARED - TAMANO_JUGADOR;
        }
    }

    public void actionPerformed(ActionEvent e) {

        jugador.actualizar();

        if (enemigoMovil != null) {
            enemigoMovil.actualizar();
        }

        jugador.verificarColisiones(entidades);
        manejarColisionesParedes();


        if (jugador.y > ALTO_VENTANA + 100) {
            jugador.x = 60;
            jugador.y = ALTURA_SUELO - TAMANO_JUGADOR;
            try {
                java.lang.reflect.Field dyField = jugador.getClass().getDeclaredField("dy");
                dyField.setAccessible(true);
                dyField.set(jugador, 0);
            } catch (Exception ex) {

            }
        }

        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);


        g.setColor(Color.WHITE);
        g.fillRect(0, 0, ANCHO_VENTANA, ALTO_VENTANA);


        for (Entidad ent : entidades) {
            ent.dibujar(g);
        }


        jugador.dibujar(g);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) jugador.setIzquierda(true);
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) jugador.setDerecha(true);
        if (e.getKeyCode() == KeyEvent.VK_UP) jugador.saltar();
        if (e.getKeyCode() == KeyEvent.VK_SPACE) jugador.setDash();
        if (e.getKeyCode() == KeyEvent.VK_S) archivo.guardar(jugador);


        if (e.getKeyCode() == KeyEvent.VK_R) {
            jugador.x = 60;
            jugador.y = ALTURA_SUELO - TAMANO_JUGADOR;
            try {
                java.lang.reflect.Field dyField = jugador.getClass().getDeclaredField("dy");
                dyField.setAccessible(true);
                dyField.set(jugador, 0);
            } catch (Exception ex) {

            }
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) jugador.setIzquierda(false);
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) jugador.setDerecha(false);
    }

    public void keyTyped(KeyEvent e) {}
}
