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

    private BufferedImage spriteQuieto;
    private BufferedImage[] spritesCaminar;
    private BufferedImage spriteActual;
    private int frameActual = 0;
    private Timer animacionTimer;

    public Jugador(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto, false);
        cargarSprites();
        iniciarAnimacion();
    }

    private void cargarSprites() {
        try {
            // Sprite quieto
            spriteQuieto = ImageIO.read(new File("textures/prota.png"));
            spriteActual = spriteQuieto;

            // Sprite sheet
            BufferedImage spriteSheet = ImageIO.read(new File("textures/prota-sheet.png"));
            spritesCaminar = cortarSpriteSheet(spriteSheet, 8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage[] cortarSpriteSheet(BufferedImage sheet, int columnas) {
        int anchoFrame = sheet.getWidth() / columnas;
        int altoFrame = sheet.getHeight();
        BufferedImage[] frames = new BufferedImage[columnas];

        for (int i = 0; i < columnas; i++) {
            frames[i] = sheet.getSubimage(i * anchoFrame, 0, anchoFrame, altoFrame);
        }

        return frames;
    }

    private void iniciarAnimacion() {
        animacionTimer = new Timer(120, e -> {
            if (izquierda || derecha) {
                frameActual = (frameActual + 1) % spritesCaminar.length;
                spriteActual = spritesCaminar[frameActual];
            } else {
                spriteActual = spriteQuieto;
            }
        });
        animacionTimer.start();
    }

    public void actualizar() {
        if (!dash) {
            if (izquierda) x -= 4;
            if (derecha) x += 4;
            dy += 1;
            y += dy;
        }
    }

    private BufferedImage voltearImagenHorizontal(BufferedImage imagen) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();

        BufferedImage imagenInvertida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imagenInvertida.createGraphics();

        // Dibuja la imagen original pero escalada horizontalmente -1x
        g2d.drawImage(imagen, 0, 0, ancho, alto, ancho, 0, 0, alto, null);
        g2d.dispose();

        return imagenInvertida;
    }

    public void dibujar(Graphics g) {
        if (spriteActual != null) {
            BufferedImage imagenADibujar = spriteActual;

            if (izquierda) {
                imagenADibujar = voltearImagenHorizontal(spriteActual);
            }

            g.drawImage(imagenADibujar, x, y, ancho, alto, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, ancho, alto);
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

    public void saltar(boolean dobleSaltoDisponible) {
        if (dobleSaltoDisponible) {
            if (enSuelo) {
                dy = -15;
                cooldownDobleSalto = true;
            } else if (cooldownDobleSalto) {
                dy = -12;
                cooldownDobleSalto = false;
            }
        } else {
            if (enSuelo) {
                dy = -15;
            }
        }
    }

    public void setDash(List<Entidad> entidades) {
        if (!dash && cooldownDash) {
            dash = true;
            cooldownDash = false;

            int direccion = (direccionActual == 1) ? -10 : 10;
            int repeticiones = 10;
            dy = 0;

            Timer dashTimer = new Timer(10, null);
            dashTimer.addActionListener(new ActionListener() {
                int pasos = 0;

                public void actionPerformed(ActionEvent e) {
                    Rectangle futuro = new Rectangle(x + direccion, y, getRect().width, getRect().height);
                    boolean colision = false;

                    for (Entidad ent : entidades) {
                        if ((ent instanceof Plataforma || ent instanceof PlataformaTrampolin)
                                && futuro.intersects(ent.getRect())) {
                            colision = true;
                            break;
                        }
                    }

                    if (!colision) {
                        x += direccion;
                        pasos++;
                    } else {
                        dashTimer.stop();
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
            }, 500);
        }
    }

    public void verificarColisionesPersonalizado(List<Entidad> entidades) {
        enSuelo = false;

        int posicionInicialX = 60;
        int posicionInicialY = 440 - alto;

        for (Entidad e : entidades) {
            if (e instanceof Plataforma && getRect().intersects(e.getRect())) {
                Plataforma plataforma = (Plataforma) e;
                Rectangle rectJugador = getRect();
                Rectangle rectPlataforma = e.getRect();
                Rectangle interseccion = rectJugador.intersection(rectPlataforma);

                if (dy > 0 && rectJugador.y + rectJugador.height - dy <= rectPlataforma.y) {
                    y = rectPlataforma.y - alto;
                    dy = 0;
                    enSuelo = true;
                } else if (dy < 0 && rectJugador.y - dy >= rectPlataforma.y + rectPlataforma.height) {
                    y = rectPlataforma.y + rectPlataforma.height;
                    dy = 0;
                } else if (interseccion.height > interseccion.width &&
                        rectJugador.x + rectJugador.width - interseccion.width <= rectPlataforma.x) {
                    x = rectPlataforma.x - ancho;
                } else if (interseccion.height > interseccion.width &&
                        rectJugador.x + interseccion.width >= rectPlataforma.x + rectPlataforma.width) {
                    x = rectPlataforma.x + rectPlataforma.width;
                }
            }

            if (e instanceof PlataformaTrampolin && getRect().intersects(e.getRect())) {
                dy = -15;
                enSuelo = true;
            }

            if (e instanceof TerrenoSlime && getRect().intersects(e.getRect())) {
                dy = 0;
                enSuelo = true;
            }

            if (e instanceof EnemigoEstaticoTriangulo || e instanceof EnemigoTerrestre || e instanceof EnemigoVolador) {
                if (getRect().intersects(e.getRect())) {
                    x = posicionInicialX;
                    y = posicionInicialY;
                    dy = 0;
                }
            }

            if (e instanceof EnemigoTrampa && getRect().intersects(e.getRect())) {
                dy = -20;
            }
        }
    }


    public int getX() { return x; }
    public int getY() { return y; }
}
