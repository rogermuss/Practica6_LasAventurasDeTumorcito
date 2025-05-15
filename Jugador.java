package Practica_6;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Jugador extends Entidad {
    private int dy = 0;
    private boolean izquierda = false, derecha = false, enSuelo = false, dash = false;
    private boolean cooldownDash = true;
    private boolean cooldownDobleSalto = true;
    private int direccionActual = 0;

    private BufferedImage spriteActual;

    public Jugador(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto, false);

        try {
            spriteActual = ImageIO.read(new File("prota.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actualizar() {
        if(!dash){
            if (izquierda) x -= 6;
            if (derecha) x += 6;
            dy += 1;
            y += dy;
        }
    }

    // Método original de verificarColisiones
    public void verificarColisiones(List<Entidad> entidades) {
        enSuelo = false;

        for (Entidad e : entidades) {
            if (e instanceof Plataforma && getRect().intersects(e.getRect())) {
                Plataforma plataforma = (Plataforma) e;
                Rectangle rectJugador = getRect();
                Rectangle rectPlataforma = e.getRect();

                // Calcular la intersección entre el jugador y la plataforma
                Rectangle interseccion = rectJugador.intersection(rectPlataforma);

                // Colisión desde arriba
                if (dy > 0 && rectJugador.y + rectJugador.height - dy <= rectPlataforma.y) {
                    y = rectPlataforma.y - alto;
                    dy = 0;
                    enSuelo = true;
                }
                // Colisión desde abajo
                else if (dy < 0 && rectJugador.y - dy >= rectPlataforma.y + rectPlataforma.height) {
                    y = rectPlataforma.y + rectPlataforma.height;
                    dy = 0;
                }
                // Colisión desde la izquierda
                else if (interseccion.height > interseccion.width &&
                        rectJugador.x + rectJugador.width - interseccion.width <= rectPlataforma.x) {
                    x = rectPlataforma.x - ancho;
                }
                // Colisión desde la derecha
                else if (interseccion.height > interseccion.width &&
                        rectJugador.x + interseccion.width >= rectPlataforma.x + rectPlataforma.width) {
                    x = rectPlataforma.x + rectPlataforma.width;
                }
            }

            if (e instanceof EnemigoTerrestre && getRect().intersects(e.getRect())) {
                x = 50; y = 400; dy = 0;
            }

            if(e instanceof EnemigoVolador && getRect().intersects(e.getRect())){
                x = 50; y = 400; dy = 0;
            }

            if (e instanceof EnemigoTrampa && getRect().intersects(e.getRect())) {
                dy = -20;
            }
        }
    }

    // Nuevo método adaptado para el nivel de la imagen
    public void verificarColisionesPersonalizado(List<Entidad> entidades) {
        enSuelo = false;

        // Posición inicial para reiniciar
        int posicionInicialX = 60;
        int posicionInicialY = 440 - alto;

        for (Entidad e : entidades) {
            // Colisiones con plataformas
            if (e instanceof Plataforma && getRect().intersects(e.getRect())) {
                Plataforma plataforma = (Plataforma) e;
                Rectangle rectJugador = getRect();
                Rectangle rectPlataforma = e.getRect();

                Rectangle interseccion = rectJugador.intersection(rectPlataforma);

                // Colisión desde arriba
                if (dy > 0 && rectJugador.y + rectJugador.height - dy <= rectPlataforma.y) {
                    y = rectPlataforma.y - alto;
                    dy = 0;
                    enSuelo = true;
                }
                // Colisión desde abajo
                else if (dy < 0 && rectJugador.y - dy >= rectPlataforma.y + rectPlataforma.height) {
                    y = rectPlataforma.y + rectPlataforma.height;
                    dy = 0;
                }
                // Colisión desde la izquierda
                else if (interseccion.height > interseccion.width &&
                        rectJugador.x + rectJugador.width - interseccion.width <= rectPlataforma.x) {
                    x = rectPlataforma.x - ancho;
                }
                // Colisión desde la derecha
                else if (interseccion.height > interseccion.width &&
                        rectJugador.x + interseccion.width >= rectPlataforma.x + rectPlataforma.width) {
                    x = rectPlataforma.x + rectPlataforma.width;
                }
            }

            // Colisiones con enemigos triangulares (picos rojos)
            if (e instanceof EnemigoEstaticoTriangulo && getRect().intersects(e.getRect())) {
                // Reiniciar al punto de inicio
                x = posicionInicialX;
                y = posicionInicialY;
                dy = 0;
            }

            // Colisiones con enemigos voladores
            if (e instanceof EnemigoVolador && getRect().intersects(e.getRect())) {
                // Reiniciar al punto de inicio
                x = posicionInicialX;
                y = posicionInicialY;
                dy = 0;
            }

            // Mantenemos el comportamiento del trampolín
            if (e instanceof EnemigoTrampa && getRect().intersects(e.getRect())) {
                dy = -20;
            }

            // No incluimos verificación para ObjetivoFinal, ya que eso se maneja en GamePanel
        }
    }

    public void fisicaSlime(List<Entidad> entidades) {
        enSuelo = false;
        for (Entidad e : entidades) {
            if (e instanceof Plataforma && getRect().intersects(e.getRect())) {
                y = e.getRect().y - alto;
                dy = 0;
                enSuelo = true;
            }
            if (e instanceof Enemigo && getRect().intersects(e.getRect())) {
                //region dy para fisica Slime
                dy = 0;
                //endregion

                //region dy para trampolin
//                dy = -20;
                //endregion
            }
        }
    }

    public void dibujar(Graphics g) {
        if (spriteActual != null) {
            g.drawImage(spriteActual, x, y, ancho, alto, null);
        } else {
            // Si no hay sprite, dibujamos un cuadrado azul (para el inicio)
            g.setColor(Color.BLUE);
            g.fillRect(x, y, ancho, alto);
        }
    }

    public void cambiarSprite(String rutaSprite) {
        try {
            spriteActual = ImageIO.read(new File(rutaSprite));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setIzquierda(boolean b) {
        izquierda = b;
        if(b){
            direccionActual = 1;
            derecha = false;
        }
    }

    public void setDerecha(boolean b) {
        derecha = b;
        if(b) direccionActual = 2;
    }

    public void saltar(boolean dobleSaltoDisponible) {
        if(dobleSaltoDisponible){
            if (enSuelo) {
                dy = -15;
                cooldownDobleSalto = true;
            }
            else if (cooldownDobleSalto) {
                dy = -15;
                cooldownDobleSalto = false;
            }
        }
        else{
            if (enSuelo) {
            dy = -15;
            }
        }
    }

    public void setDash() {
        if (!dash && cooldownDash){
            dash = true;
            cooldownDash = false;

            int direccion = (direccionActual == 1) ? -10 : 10; // paso por frame
            int repeticiones = 10; // mover 10 veces (total 100 px)

            Timer dashTimer = new Timer(10, null); // 10 ms entre cada paso

            dashTimer.addActionListener(new ActionListener() {
                int pasos = 0;

                public void actionPerformed(ActionEvent e) {
                    x += direccion; // aquí sí se mueve
                    pasos++;
                    if (pasos >= repeticiones) {
                        dashTimer.stop();
                        dash = false;
                    }
                }
            });

            dashTimer.start();

            new java.util.Timer().schedule(new java.util.TimerTask() {
                public void run() {
                    cooldownDash = true;
                }
            },500);
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
