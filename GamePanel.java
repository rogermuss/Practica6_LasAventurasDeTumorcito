package Practica_6;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Jugador jugador;
    private ArrayList<Entidad> entidades;
    private ArchivoJuego archivo;
    private ObjetivoFinal objetivo;
    private int nivelActual;

    // Imagen de fondo
    private BufferedImage fondoImagen;
    private boolean fondoCargado = false;

    // Banderas para el estado del juego
    private boolean juegoTerminado = false;

    // Referencias a enemigos móviles para actualizarlos
    private ArrayList<EnemigoVolador> enemigosVoladores;

    private final int ANCHO_VENTANA = 854;
    private final int ALTO_VENTANA = 480;

    private final int TAMANO_JUGADOR = 40;
    private final int TAMANO_ENEMIGO = 30;
    private final int TAMANO_PLATAFORMA = 40; // Alto de las plataformas

    private final int ANCHO_PARED = 40;

    public GamePanel() {
        setPreferredSize(new Dimension(ANCHO_VENTANA, ALTO_VENTANA));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        // Cargar imagen de fondo
        cargarFondo();

        // Iniciamos el jugador en la posición inicial (esquina inferior izquierda)
        jugador = new Jugador(60, ALTO_VENTANA - TAMANO_PLATAFORMA - TAMANO_JUGADOR, TAMANO_JUGADOR+10, TAMANO_JUGADOR);
        entidades = new ArrayList<>();
        enemigosVoladores = new ArrayList<>();
        archivo = new ArchivoJuego("progreso.txt");


        crearNivelTutorial();
        // Solo cargar progreso si no deseas que empiece siempre desde el inicio
        // archivo.cargar(jugador);

        timer = new Timer(16, this);
        timer.start();
    }

    public GamePanel(int nivelActual) {
        setPreferredSize(new Dimension(ANCHO_VENTANA, ALTO_VENTANA));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        // Cargar imagen de fondo
        cargarFondo();

        // Iniciamos el jugador en la posición inicial (esquina inferior izquierda)
        jugador = new Jugador(60, ALTO_VENTANA - TAMANO_PLATAFORMA - TAMANO_JUGADOR, TAMANO_JUGADOR+10, TAMANO_JUGADOR);
        entidades = new ArrayList<>();
        enemigosVoladores = new ArrayList<>();
        archivo = new ArchivoJuego("progreso.txt");


        cargarNivel(nivelActual);
        this.nivelActual = nivelActual;
        // Solo cargar progreso si no deseas que empiece siempre desde el inicio
        // archivo.cargar(jugador);

        timer = new Timer(16, this);
        timer.start();
    }



    private void cargarFondo() {
        try {
            // Intentar cargar desde la ruta absoluta primero
            File archivo = new File("fondo.jpg");

            if (!archivo.exists()) {
                // Si no existe en la ruta absoluta, intentar rutas relativas
                archivo = new File("fondo.jpg");

                if (!archivo.exists()) {
                    archivo = new File("src/Practica_6/fondo.jpg");
                }
            }

            if (archivo.exists()) {
                fondoImagen = ImageIO.read(archivo);
                fondoCargado = true;
                System.out.println("Fondo cargado desde: " + archivo.getAbsolutePath());
            } else {
                System.out.println("No se pudo encontrar la imagen de fondo en ninguna ubicación");
            }
        } catch (IOException e) {
            System.out.println("Error al cargar la imagen de fondo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void crearNivelTutorial() {

        // Botón para mostrar/ocultar
        JButton btnInstrucciones = new JButton("Mostrar instrucciones");
        btnInstrucciones.setFocusPainted(false);
        btnInstrucciones.setBackground(new Color(100, 144, 200));
        btnInstrucciones.setForeground(Color.WHITE);
        add(btnInstrucciones, BorderLayout.NORTH);
        
        JTextArea textoInstrucciones = new JTextArea(5, 40);
        textoInstrucciones.setText(
            "Bienvenido al juego Tumorcin.\n\n" +
            "- =UP= Saltar - Doble Salto \n" +
            "- =LEFT= Moverse Hacia la Izquierda <-\n" +
            "- =RIGHT= Moverse Hacia la Derecha ->\n" +
            "- =SPACE= Dash <-- -->\n\n" +
            "¡Buena suerte!"
        );

        textoInstrucciones.setFont(new Font("Verdana", Font.BOLD, 14));
        textoInstrucciones.setBackground(new Color(0, 144, 100));
        textoInstrucciones.setForeground(Color.WHITE);
        textoInstrucciones.setLineWrap(true); // Ajuste de línea
        textoInstrucciones.setWrapStyleWord(true);
        textoInstrucciones.setEditable(false); // No editable, pero seleccionable
        textoInstrucciones.setFocusable(true); // Para permitir seleccionar texto
        textoInstrucciones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(textoInstrucciones);
        scroll.setVisible(false); // Oculto al inicio
        add(scroll, BorderLayout.CENTER);

        // Acción del botón
        btnInstrucciones.addActionListener((ActionEvent e) -> {
            boolean visible = scroll.isVisible();
            scroll.setVisible(!visible);
            btnInstrucciones.setText(visible ? "Mostrar instrucciones" : "Ocultar instrucciones");
            revalidate(); // Actualiza el diseño
            repaint();
            requestFocusInWindow();
        });


        // Paredes laterales del nivel
        entidades.add(new Plataforma(0, 0, ANCHO_PARED, ALTO_VENTANA, true));
        entidades.add(new Plataforma(ANCHO_VENTANA - ANCHO_PARED, 0, ANCHO_PARED, ALTO_VENTANA, false));

        // PUNTO DE INICIO - Plataforma de inicio (donde está el jugador - cuadrado azul)
        int altoPlataforma = 40;
        entidades.add(new Plataforma(ANCHO_PARED, ALTO_VENTANA - altoPlataforma, 120, altoPlataforma, false));

        // Plataforma pequeña a la derecha de la inicial
        entidades.add(new Plataforma(ANCHO_PARED + 200, ALTO_VENTANA - altoPlataforma, 60, altoPlataforma, false));

        // Plataforma horizontal después de la primera subida (con picos)
        entidades.add(new Plataforma(ANCHO_PARED, ALTO_VENTANA - 200, 100, altoPlataforma, false));

        // Picos en esta plataforma
        entidades.add(new EnemigoEstaticoTriangulo(ANCHO_PARED + 20, ALTO_VENTANA - 200 - TAMANO_ENEMIGO, TAMANO_ENEMIGO, TAMANO_ENEMIGO, EnemigoEstaticoTriangulo.UP));
        entidades.add(new EnemigoEstaticoTriangulo(ANCHO_PARED + 50, ALTO_VENTANA - 200 - TAMANO_ENEMIGO, TAMANO_ENEMIGO, TAMANO_ENEMIGO, EnemigoEstaticoTriangulo.UP));

        // Plataforma vertical a la derecha (segunda subida)
        entidades.add(new Plataforma(ANCHO_PARED + 230, ALTO_VENTANA - 520, 50, 140, false));

        entidades.add(new Plataforma(ANCHO_VENTANA/2 + 115, ALTO_VENTANA/2 + 20, 50, 80, false));

        // Par de enemigos voladores verticales (magenta)
        // Usando el nuevo constructor para movimiento vertical
        EnemigoVolador volador1 = new EnemigoVolador(
                ANCHO_VENTANA/2,
                ALTO_VENTANA/2 - 40,
                TAMANO_ENEMIGO,
                TAMANO_ENEMIGO,
                ALTO_VENTANA/2 - 80,
                ALTO_VENTANA/2 + 60,
                true // true para movimiento vertical
        );
        entidades.add(volador1);
        enemigosVoladores.add(volador1);

        EnemigoVolador volador2 = new EnemigoVolador(
                ANCHO_VENTANA/2 + 200,
                ALTO_VENTANA/2 + 40,
                TAMANO_ENEMIGO,
                TAMANO_ENEMIGO,
                ALTO_VENTANA/2 - 20,
                ALTO_VENTANA/2 + 160,
                true // true para movimiento vertical
        );
        entidades.add(volador2);
        enemigosVoladores.add(volador2);

        // Plataforma después de los enemigos voladores
        entidades.add(new Plataforma(ANCHO_VENTANA/2 - 290, ALTO_VENTANA/2+77, 90, 43, false));

        // Plataforma con picos hacia la derecha
        entidades.add(new Plataforma(ANCHO_VENTANA - ANCHO_PARED - 120, ALTO_VENTANA/2 + 30, 80, altoPlataforma, false));
        entidades.add(new EnemigoEstaticoTriangulo(ANCHO_VENTANA - ANCHO_PARED - 110, ALTO_VENTANA/2 + 30 - TAMANO_ENEMIGO, TAMANO_ENEMIGO, TAMANO_ENEMIGO, EnemigoEstaticoTriangulo.UP));
        entidades.add(new EnemigoEstaticoTriangulo(ANCHO_VENTANA - ANCHO_PARED - 80, ALTO_VENTANA/2 + 30 - TAMANO_ENEMIGO, TAMANO_ENEMIGO, TAMANO_ENEMIGO, EnemigoEstaticoTriangulo.UP));

        // Plataforma vertical derecha
        entidades.add(new Plataforma(ANCHO_VENTANA - ANCHO_PARED - 120, ALTO_VENTANA/2 + 30, 50, 200, false));

        // Plataforma L invertida
        entidades.add(new Plataforma(ANCHO_VENTANA - ANCHO_PARED - 220, ALTO_VENTANA/2 + 200, 150, altoPlataforma, false));
        entidades.add(new Plataforma(ANCHO_VENTANA - ANCHO_PARED - 220, ALTO_VENTANA/2 + 200, 50, 100, false));

        // Plataforma inferior derecha con picos
        entidades.add(new Plataforma(ANCHO_VENTANA - ANCHO_PARED - 220, ALTO_VENTANA - altoPlataforma, 180, altoPlataforma, false));
        //entidades.add(new EnemigoEstaticoTriangulo(ANCHO_VENTANA - ANCHO_PARED - 200, ALTO_VENTANA - altoPlataforma - TAMANO_ENEMIGO, TAMANO_ENEMIGO, TAMANO_ENEMIGO));
        //entidades.add(new EnemigoEstaticoTriangulo(ANCHO_VENTANA - ANCHO_PARED - 170, ALTO_VENTANA - altoPlataforma - TAMANO_ENEMIGO, TAMANO_ENEMIGO, TAMANO_ENEMIGO));

        // Plataforma en L inferior central
        entidades.add(new Plataforma(ANCHO_VENTANA/2, ALTO_VENTANA - altoPlataforma, 50, altoPlataforma, false));
        entidades.add(new Plataforma(ANCHO_VENTANA/2, ALTO_VENTANA - 150, 50, 80, false));
        entidades.add(new Plataforma(ANCHO_VENTANA/2 - 120, ALTO_VENTANA - 150, 170, altoPlataforma, false));

        // Plataforma con picos central
        entidades.add(new Plataforma(ANCHO_VENTANA/2 - 200, ALTO_VENTANA - 120, 80, altoPlataforma, false));
        entidades.add(new EnemigoEstaticoTriangulo(ANCHO_VENTANA/2 - 190, ALTO_VENTANA - 120 - TAMANO_ENEMIGO, TAMANO_ENEMIGO, TAMANO_ENEMIGO, EnemigoEstaticoTriangulo.UP));
        entidades.add(new EnemigoEstaticoTriangulo(ANCHO_VENTANA/2 - 160, ALTO_VENTANA - 120 - TAMANO_ENEMIGO, TAMANO_ENEMIGO, TAMANO_ENEMIGO, EnemigoEstaticoTriangulo.UP));

        // Añadir el objetivo final (cuadrado amarillo flotante)
        objetivo = new ObjetivoFinal(ANCHO_VENTANA/2-350, 80, 40, 40);
        entidades.add(objetivo);

        nivelActual = 0;
    }

    //Crear niveles-----------------------
    public void crearNivel1(){

        JButton btnInstrucciones = new JButton("No puedes utilizar el doble salto");
        btnInstrucciones.setFocusable(false);
        btnInstrucciones.setEnabled(false);
        btnInstrucciones.setFocusPainted(false);
        btnInstrucciones.setBackground(new Color(100, 50, 44));
        btnInstrucciones.setForeground(Color.WHITE);
        add(btnInstrucciones, BorderLayout.NORTH);

        // Paredes laterales del nivel
        entidades.add(new Plataforma(0, 0, ANCHO_PARED, ALTO_VENTANA, true));
        entidades.add(new Plataforma(ANCHO_VENTANA - ANCHO_PARED, 0, ANCHO_PARED, ALTO_VENTANA, false));

        //Techo del nivel
        entidades.add(new Plataforma(0, -20, ANCHO_PARED+820, 20, true));

        // PUNTO DE INICIO - Plataforma de inicio (donde está el jugador - cuadrado azul)
        int altoPlataforma = 40;
        entidades.add(new Plataforma(ANCHO_PARED, ALTO_VENTANA - altoPlataforma, 120, altoPlataforma, false));

        //Plataforma pequeña despues de la inicial
        entidades.add(new Plataforma(ANCHO_PARED + 200, ALTO_VENTANA - altoPlataforma, 30, altoPlataforma, false));

        //Plataforma en forma de L invertida con picos hacia la izquierda
        entidades.add(new Plataforma(ANCHO_PARED + 350, ALTO_VENTANA - altoPlataforma - 40, 70, altoPlataforma - 20, false));
        entidades.add(new Plataforma(ANCHO_PARED + 350+70, ALTO_VENTANA - altoPlataforma -140 , 30, altoPlataforma + 80, false));
        entidades.add(new EnemigoEstaticoTriangulo(ANCHO_PARED + 350+70-30, ALTO_VENTANA - altoPlataforma -140, TAMANO_ENEMIGO, TAMANO_ENEMIGO, EnemigoEstaticoTriangulo.LEFT));
        entidades.add(new EnemigoEstaticoTriangulo(ANCHO_PARED + 350+70-30, ALTO_VENTANA - altoPlataforma -140+TAMANO_ENEMIGO, TAMANO_ENEMIGO, TAMANO_ENEMIGO, EnemigoEstaticoTriangulo.LEFT));

        //Plataforma con trampolin trampa
        entidades.add(new Plataforma(ANCHO_PARED + 200, ALTO_VENTANA - altoPlataforma-130, 40, altoPlataforma, false));
        entidades.add(new PlataformaTrampolin(ANCHO_PARED + 200, ALTO_VENTANA - altoPlataforma-170, 40, altoPlataforma, false));

        //Plataforma con picos encima del trampolin
        entidades.add(new Plataforma(ANCHO_PARED + 200, ALTO_VENTANA - altoPlataforma-170-40-80, 40+40, altoPlataforma, false));
        entidades.add(new EnemigoEstaticoTriangulo(ANCHO_PARED + 200+40, ALTO_VENTANA - altoPlataforma-170-40-40, 40, altoPlataforma-20, EnemigoEstaticoTriangulo.DOWN));
        entidades.add(new EnemigoEstaticoTriangulo(ANCHO_PARED + 200, ALTO_VENTANA - altoPlataforma-170-40-40, 40, altoPlataforma-20, EnemigoEstaticoTriangulo.DOWN));

        //Plataforma esquina inferior derecha con trampolin y trampas
        entidades.add(new Plataforma(ANCHO_PARED+655, ALTO_VENTANA - altoPlataforma, 120, altoPlataforma, false));
        entidades.add(new EnemigoEstaticoTriangulo(ANCHO_PARED+655+45, ALTO_VENTANA - altoPlataforma-20, 20, altoPlataforma-20, EnemigoEstaticoTriangulo.UP));
        entidades.add(new EnemigoEstaticoTriangulo(ANCHO_PARED+655+25, ALTO_VENTANA - altoPlataforma-20, 20, altoPlataforma-20, EnemigoEstaticoTriangulo.UP));
        entidades.add(new PlataformaTrampolin(ANCHO_PARED+655+75, ALTO_VENTANA - altoPlataforma-20, 40, altoPlataforma-20, false));


        //Plataforma en forma de serpienta escalable
        entidades.add(new Plataforma(ANCHO_PARED+660, ALTO_VENTANA - altoPlataforma-270, 30, altoPlataforma+150, false));
        entidades.add(new Plataforma(ANCHO_PARED+690, ALTO_VENTANA - altoPlataforma-120, 30, altoPlataforma, false));
        entidades.add(new Plataforma(ANCHO_PARED+630, ALTO_VENTANA - altoPlataforma-270, 30, altoPlataforma, false));

        //Plataforma escalable pegada a la pared derecha
        entidades.add(new Plataforma(ANCHO_PARED+655+90, ALTO_VENTANA - altoPlataforma-215, 30, altoPlataforma-20, false));

        //Trampolin aereo
        entidades.add(new PlataformaTrampolin(ANCHO_PARED + 350+70, ALTO_VENTANA - altoPlataforma - 345, 20, 20, false));

        EnemigoVolador volador1 = new EnemigoVolador(
                ANCHO_PARED+630-100, 
                ALTO_VENTANA - altoPlataforma - 430,
                TAMANO_ENEMIGO,
                TAMANO_ENEMIGO,
                ALTO_VENTANA - altoPlataforma - 430,
                ALTO_VENTANA -altoPlataforma - 290,
                true // true para movimiento vertical
        );
        entidades.add(volador1);
        enemigosVoladores.add(volador1);

        EnemigoVolador volador2 = new EnemigoVolador(
                ANCHO_PARED+630-300, 
                ALTO_VENTANA - altoPlataforma - 390,
                TAMANO_ENEMIGO,
                TAMANO_ENEMIGO,
                ALTO_VENTANA - altoPlataforma - 430,
                ALTO_VENTANA -altoPlataforma - 280,
                true // true para movimiento vertical
        );
        entidades.add(volador2);
        enemigosVoladores.add(volador2);

        //Pared antes de la meta
        entidades.add(new Plataforma(ANCHO_VENTANA/2-380-8+114, 0, 30, 48+40, false));


        //Trampolin para llegar a la meta
        entidades.add(new Plataforma(ANCHO_VENTANA/2-380-8+56, 47+90, 51, 20, false));



        //Plataforma objetivo final
        entidades.add(new Plataforma(ANCHO_VENTANA/2-380-8, 5+42, 40+16, 40, false));
        objetivo = new ObjetivoFinal(ANCHO_VENTANA/2-380, 5, 40, 40);
        entidades.add(objetivo);
    }

    public void crearNivel2(){
        
    }

    public void crearNivel3(){

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

    

    // Método para verificar si el jugador ha alcanzado el objetivo
    private void verificarObjetivoAlcanzado() {
        if (objetivo != null && jugador.getRect().intersects(objetivo.getRect())) {
            objetivo.setAlcanzado(true);
            juegoTerminado = true;
            // Aquí puedes agregar lógica adicional para cuando el juego termina
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (juegoTerminado) {
            return; // No actualizar si el juego ha terminado
        }

        jugador.actualizar();

        // Actualizar todos los enemigos voladores
        for (EnemigoVolador volador : enemigosVoladores) {
            volador.actualizar();
        }

        jugador.verificarColisionesPersonalizado(entidades);
        manejarColisionesParedes();
        verificarObjetivoAlcanzado();

        // Si el jugador cae fuera del nivel, reiniciar posición
        if (jugador.y > ALTO_VENTANA + 100) {
            reiniciarJugador();
        }

        repaint();
    }

    private void reiniciarJugador() {
        jugador.x = 60;
        jugador.y = ALTO_VENTANA - TAMANO_PLATAFORMA - TAMANO_JUGADOR;
        try {
            java.lang.reflect.Field dyField = jugador.getClass().getDeclaredField("dy");
            dyField.setAccessible(true);
            dyField.set(jugador, 0);
        } catch (Exception ex) {
            // Ignorar excepciones
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar imagen de fondo si está cargada
        if (fondoCargado && fondoImagen != null) {
            g.drawImage(fondoImagen, 0, 0, ANCHO_VENTANA, ALTO_VENTANA, null);
        } else {
            // Fondo blanco si no hay imagen
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, ANCHO_VENTANA, ALTO_VENTANA);
        }

        // Dibujar todas las entidades
        for (Entidad ent : entidades) {
            ent.dibujar(g);
        }

        // Dibujar jugador
        jugador.dibujar(g);

        // Si el juego ha terminado, mostrar mensaje
        // Si el juego ha terminado
    if (juegoTerminado) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("¡Nivel Completado "+nivelActual+"!", ANCHO_VENTANA / 2 - 150, ALTO_VENTANA / 2);
        // Esperar unos segundos y avanzar al siguiente nivel
        if (nivelActual == 0) {
            JOptionPane.showMessageDialog(this, "Click para volver al menu principal");            
            SwingUtilities.getWindowAncestor(this).dispose();
            new MenuTumorcin();
            return;
        }
        if (nivelActual == 3) {
            JOptionPane.showMessageDialog(this, "Gracias por jugar TUMORCIN!!");            
            SwingUtilities.getWindowAncestor(this).dispose();
            new MenuTumorcin();
            return;
        }
        juegoTerminado = false; // evitar múltiples entradas
        new Thread(() -> {
            try {
                Thread.sleep(2000); // 2 segundos de pausa
                nivelActual++;
                removeAll();
                cargarNivel(nivelActual);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
    }


    //Se necesita un metodo con el cual se pueda cargar los datos del nivel dependiendo del nivel en el que se encuentren.
    public void cargarNivel(int nivel){
        switch(nivel){
            case 1:
                crearNivel1();
            break;
            case 2:
                crearNivel2();
            break;
            case 3:
                crearNivel3();
            break;
        }
    }
    
    public void keyPressed(KeyEvent e) {
        if (juegoTerminado) return; // No procesar teclas si el juego ha terminado

        if (e.getKeyCode() == KeyEvent.VK_LEFT) jugador.setIzquierda(true);
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) jugador.setDerecha(true);
        if(nivelActual == 1){
            if (e.getKeyCode() == KeyEvent.VK_UP) jugador.saltar(false);
        }
        else{
            if (e.getKeyCode() == KeyEvent.VK_UP) jugador.saltar(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) jugador.setDash();
        if (e.getKeyCode() == KeyEvent.VK_S) archivo.guardar(jugador);

        // Reiniciar posición con tecla R
        if (e.getKeyCode() == KeyEvent.VK_R) {
            reiniciarJugador();
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) jugador.setIzquierda(false);
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) jugador.setDerecha(false);
    }

    public void keyTyped(KeyEvent e) {}
}
