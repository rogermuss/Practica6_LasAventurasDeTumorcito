package Practica_6;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Jugador extends Entidad {
    private int dy = 0;
    private boolean izquierda = false, derecha = false, enSuelo = false, dash = false;
    private boolean cooldownDash = true;
    private boolean cooldownDobleSalto = true;
    private int direccionActual = 0;

    private BufferedImage spriteActual; // <- imagen del jugador

    public Jugador(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);

        try {
            spriteActual = ImageIO.read(new File("C:\\Users\\rogel\\OneDrive\\Escritorio\\Sprite\\pipopo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actualizar() {
        if (!dash) {
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
                y = e.getRect().y - alto;
                dy = 0;
                enSuelo = true;
            }
            if (e instanceof PlataformaFija && getRect().intersects(e.getRect())) {
                Rectangle plataforma = e.getRect();
                Rectangle jugador = getRect();

                int dx = (jugador.x + jugador.width / 2) - (plataforma.x + plataforma.width / 2);
               int dy = (jugador.y + jugador.height / 2) - (plataforma.y + plataforma.height / 2);

    int anchoSolapado = (jugador.width + plataforma.width) / 2 - Math.abs(dx);      
    int altoSolapado = (jugador.height + plataforma.height) / 2 - Math.abs(dy);

    if (anchoSolapado < altoSolapado) {
        // Colisión horizontal
        if (dx > 0) {
            // Chocando desde la derecha
            x = plataforma.x + plataforma.width;
        } else {
            // Chocando desde la izquierda
            x = plataforma.x - jugador.width;
        }
    } else {
        // Colisión vertical
        if (dy > 0) {
            // Chocando desde abajo (subió por error)
            y = plataforma.y + plataforma.height;
        } else {
            // Cayendo desde arriba
            y = plataforma.y - jugador.height;
            velocidadY = 0; // Detener caída si usas gravedad
        }
    }
}

            if (e instanceof Enemigo && getRect().intersects(e.getRect())) {
                x = 50;
                y = 500;
                dy = 0;
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
                dy = 0;
            }
        }
    }

    public void dibujar(Graphics g) {
        if (spriteActual != null) {
            g.drawImage(spriteActual, x, y, ancho, alto, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, ancho, alto); // fallback si no hay imagen
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
        if (b) {
            direccionActual = 1;
            derecha = false;
        }
    }

    public void setDerecha(boolean b) {
        derecha = b;
        if (b) direccionActual = 2;
    }

    public void saltar() {
        if (enSuelo) {
            dy = -15;
            cooldownDobleSalto = true;
        } else if (cooldownDobleSalto) {
            dy = -15;
            cooldownDobleSalto = false;
        }
    }

    public void setDash() {
        if (!dash && cooldownDash) {
            dash = true;
            cooldownDash = false;

            int direccion = (direccionActual == 1) ? -10 : 10;
            int repeticiones = 10;

            Timer dashTimer = new Timer(10, null);
            dashTimer.addActionListener(new ActionListener() {
                int pasos = 0;

                public void actionPerformed(ActionEvent e) {
                    x += direccion;
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
            }, 500);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
