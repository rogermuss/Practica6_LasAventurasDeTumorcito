package Practica_6;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Jugador extends Entidad {
    private int dy = 0;
    private boolean izquierda = false, derecha = false, enSuelo = false, dash = false;
    private boolean cooldownDash = true;
    private boolean cooldownDobleSalto = true;
    private int direccionActual = 0;

    public Jugador(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    public void actualizar() {
        if(!dash){
            if (izquierda) x -= 6;
            if (derecha) x += 6;
            dy += 1;
            y += dy;
        }
    }

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

            if (e instanceof Enemigo && getRect().intersects(e.getRect())) {
                x = 50; y = 500; dy = 0;
            }
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
        g.setColor(Color.BLUE);
        g.fillRect(x, y, ancho, alto);
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

    public void saltar() {
        if (enSuelo) {
            dy = -15;
            cooldownDobleSalto = true;
        }
        else if (cooldownDobleSalto) {
            dy = -15;
            cooldownDobleSalto = false;
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
