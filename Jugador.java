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
            spriteActual = ImageIO.read(new File("textures/prota.png"));
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

            // Después del bloque de código para Plataforma normal y antes del bloque para PlataformaTrampolin
            if (e instanceof PlataformaMovil && getRect().intersects(e.getRect())) {
                PlataformaMovil plataforma = (PlataformaMovil) e;
                Rectangle rectJugador = getRect();
                Rectangle rectPlataforma = e.getRect();

                Rectangle interseccion = rectJugador.intersection(rectPlataforma);

                // Colisión desde arriba (jugador sobre la plataforma)
                if (dy > 0 && rectJugador.y + rectJugador.height - dy <= rectPlataforma.y + 5) {
                    y = rectPlataforma.y - alto;
                    dy = 0;
                    enSuelo = true;

                    // Si es una plataforma vertical que se mueve hacia abajo
                    if (plataforma.esMovimientoVertical()) {
                        int deltaY = plataforma.getDeltaY();
                        if (deltaY > 0) { // Si la plataforma se mueve hacia abajo
                            y += deltaY; // Mover el jugador con la plataforma
                        }
                    }

                    // Para el movimiento horizontal siempre ajustamos
                    if (!plataforma.esMovimientoVertical()) {
                        x += plataforma.getDeltaX();
                    }
                }
                // Colisión desde abajo
                else if (dy < 0 && rectJugador.y - dy >= rectPlataforma.y + rectPlataforma.height - 5) {
                    y = rectPlataforma.y + rectPlataforma.height;
                    dy = 0;
                }
                // Colisión desde la izquierda
                else if (interseccion.height > interseccion.width &&
                        rectJugador.x + rectJugador.width - interseccion.width <= rectPlataforma.x + 5) {
                    x = rectPlataforma.x - ancho;
                }
                // Colisión desde la derecha
                else if (interseccion.height > interseccion.width &&
                        rectJugador.x + interseccion.width >= rectPlataforma.x + rectPlataforma.width - 5) {
                    x = rectPlataforma.x + rectPlataforma.width;
                }
            }

            if (e instanceof PlataformaTrampolin && getRect().intersects(e.getRect())) {
                PlataformaTrampolin plataforma = (PlataformaTrampolin) e;
                Rectangle rectJugador = getRect();
                Rectangle rectPlataforma = e.getRect();

                // Calcular la intersección entre el jugador y la plataforma
                Rectangle interseccion = rectJugador.intersection(rectPlataforma);

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

                //region dy para fisica Slime
                // dy = 0;
                //endregion

                //region dy para trampolin
                dy = -15;
                //endregion
            }

            if(e instanceof TerrenoSlime && getRect().intersects(e.getRect())){
                dy = 0;
                enSuelo = true;
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

            if(e instanceof EnemigoTerrestre && getRect().intersects(e.getRect())){
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
                dy = -12;
                cooldownDobleSalto = false;
            }
        }
        else{
            if (enSuelo) {
            dy = -15;
            }
        }
    }

    public void setDash(List<Entidad> entidades) {
        if (!dash && cooldownDash){
            dash = true;
            cooldownDash = false;

            int direccion = (direccionActual == 1) ? -10 : 10; // paso por frame
            int repeticiones = 10; // mover 10 veces (total 100 px)
            dy = 0;

            Timer dashTimer = new Timer(10, null); // 10 ms entre cada paso


            dashTimer.addActionListener(new ActionListener() {
                int pasos = 0;

                public void actionPerformed(ActionEvent e) {
                    // Crear una "posición futura"
                    Rectangle futuro = new Rectangle(x + direccion, y, getRect().width, getRect().height);

                    boolean colision = false;

                    for (Entidad ent : entidades) {
                        if ((ent instanceof Plataforma || ent instanceof PlataformaTrampolin) && futuro.intersects(ent.getRect())) {
                        colision = true;
                        break;
                        }
                    }

                    if (!colision) {
                        x += direccion; // Solo se mueve si NO hay colisión
                        pasos++;
                    } else {
                        dashTimer.stop(); // Detiene el dash si hay colisión
                        dash = false;
                    }

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
