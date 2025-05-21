package Practica_6;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.Timer;

public class EnemigoTerrestre extends Enemigo {

    private int velocidad = 2;
    private int limiteIzquierdo;
    private int limiteDerecho;
    private int limiteSuperior;
    private int limiteInferior;
    private int dy = 0;

    private boolean moverHorizontal = false;
    private boolean moverVertical = false;
    private boolean enSuelo = false;
    private int conSalto = 1;

    public static final int CON_SALTO = 1;

    private BufferedImage spriteActual;
    private boolean intentoCargaImagen = false;

    public EnemigoTerrestre(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    public EnemigoTerrestre(int x, int y, int ancho, int alto, int limiteIzquierdo, int limiteDerecho, int conSalto) {
        super(x, y, ancho, alto);
        this.limiteIzquierdo = limiteIzquierdo;
        this.limiteDerecho = limiteDerecho;
        this.moverHorizontal = true;
        if (conSalto == CON_SALTO) {
            saltar();
        }
    }

    public void saltar() {
        int delay = 2000;

        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (enSuelo) {
                    dy = -15;
                }
            }
        });
        timer.start();
    }

    public void actualizar() {
        if (moverHorizontal) {
            x += velocidad;
            if (x <= limiteIzquierdo || x + ancho >= limiteDerecho) {
                velocidad = -velocidad;
            }
        }

        if (moverVertical) {
            y += velocidad;
            if (y <= limiteSuperior || y + alto >= limiteInferior) {
                velocidad = -velocidad;
            }
        }

        dy += 1;
        y += dy;
    }

    public void verificarColisionesPersonalizado(List<Entidad> entidades) {
        enSuelo = false;

        for (Entidad e : entidades) {
            if (e instanceof Plataforma && getRect().intersects(e.getRect())) {
                Plataforma plataforma = (Plataforma) e;
                Rectangle rectEnemigo = getRect();
                Rectangle rectPlataforma = e.getRect();
                Rectangle interseccion = rectEnemigo.intersection(rectPlataforma);

                if (dy > 0 && rectEnemigo.y + rectEnemigo.height - dy <= rectPlataforma.y) {
                    y = rectPlataforma.y - alto;
                    dy = 0;
                    enSuelo = true;
                } else if (dy < 0 && rectEnemigo.y - dy >= rectPlataforma.y + rectPlataforma.height) {
                    y = rectPlataforma.y + rectPlataforma.height;
                    dy = 0;
                } else if (interseccion.height > interseccion.width &&
                        rectEnemigo.x + rectEnemigo.width - interseccion.width <= rectPlataforma.x) {
                    x = rectPlataforma.x - ancho;
                } else if (interseccion.height > interseccion.width &&
                        rectEnemigo.x + interseccion.width >= rectPlataforma.x + rectPlataforma.width) {
                    x = rectPlataforma.x + rectPlataforma.width;
                }
            }
        }
    }

    private void cargarImagen() {
        try {
            File archivo = new File("textures/enemigoTerrestre.png");

            if (!archivo.exists()) {
                archivo = new File("src/Practica_6/enemigoTerrestre.png");
            }

            if (archivo.exists()) {
                spriteActual = ImageIO.read(archivo);
                System.out.println("Imagen del enemigo terrestre cargada desde: " + archivo.getAbsolutePath());
            } else {
                System.out.println("No se pudo encontrar la imagen del enemigo terrestre en ninguna ubicación");
            }
        } catch (IOException e) {
            System.out.println("Error al cargar la imagen del enemigo terrestre: " + e.getMessage());
            e.printStackTrace();
        }

        intentoCargaImagen = true;
    }

    @Override
    public void dibujar(Graphics g) {
        if (!intentoCargaImagen) {
            cargarImagen();
        }

        if (spriteActual != null) {
            g.drawImage(spriteActual, x, y, ancho, alto, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, ancho, alto);
        }
    }
}
