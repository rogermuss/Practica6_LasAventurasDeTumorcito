package Practica_6;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MenuTumorcin {
    //Crear ventana de juego, asi como una ventana de menu inicial, necesario para seleccionar el modo de juego,
    //puede ser para dos jugadores en una misma red o jugando contra la cpu.

    //Se aplicara una seccion de botones en toda la ventana, asi como un diseño de fondo haciendo referencia al mar

    //Se ingresaran una seleccion de barcos para el juego para colocar los barcos seleccionandolos con el mouse y colocarlos
    //en la posicion deseada


    //Atributos
    public final int XSIZE = 854;
    public final int YSIZE = 480;
    private JButton opcionJugar = new JButton("Jugar");
    private JButton opcionSalir = new JButton("Salir");
    private JButton opcionJugarTutorial = new JButton("Tutorial");
    private JFrame ventanaMenu = new JFrame("Version 1.0");
    private JPanel panelInferior = new JPanel();


    // Constructor principal que configura y muestra la ventana del menú inicial.
    // Establece el tamaño, fondo, logo, botones de selección de modo de juego y acciones correspondientes.
    public MenuTumorcin() {
        // Agregar las opciones al menú
        ventanaMenu.setSize(XSIZE, YSIZE);
        ventanaMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaMenu.setLayout(new BorderLayout());
        ventanaMenu.getContentPane().setBackground(new Color(0x000000));
        ventanaMenu.setResizable(false);
        ventanaMenu.setLocationRelativeTo(null);



        //Se busca y coloca la imagen como logo del menu
        ImageIcon imagenCentral = buscarImagenMenu();
        Image img = imagenCentral.getImage().getScaledInstance(900, 500, Image.SCALE_SMOOTH);
        ImageIcon imagenRedimensionada = new ImageIcon(img);
        JLabel etiquetaImagen = new JLabel(imagenRedimensionada);


        opcionJugar.setBackground(new Color(30, 144, 255)); // Azul dodger
        opcionJugar.setForeground(Color.WHITE); // Texto blanco
        opcionJugar.setFocusPainted(false); // Quita el borde de enfoque


        opcionJugarTutorial.setBackground(new Color(100, 144, 200)); 
        opcionJugarTutorial.setForeground(Color.WHITE); // Texto blanco
        opcionJugarTutorial.setFocusPainted(false); // Quita el borde de enfoque

        // Botón Salir - Rojo
        opcionSalir.setBackground(new Color(178, 34, 34)); // Rojo ladrillo
        opcionSalir.setForeground(Color.WHITE);
        opcionSalir.setFocusPainted(false);

        opcionJugarTutorial.setOpaque(true);
        opcionJugar.setOpaque(true);
        opcionSalir.setOpaque(true);

        //Se crea un panel para colocar las opciones del menu para comenzar el juego
        panelInferior.setBackground(Color.WHITE); // test visual
        panelInferior.add(opcionJugar);
        panelInferior.add(opcionJugarTutorial);
        panelInferior.add(opcionSalir);


        opcionJugarTutorial();
        opcionSalir();
        opcionJugar();

        ventanaMenu.add(panelInferior, BorderLayout.SOUTH);
        ventanaMenu.add(etiquetaImagen, BorderLayout.NORTH);

        ventanaMenu.setVisible(true);
    }

    //Se genera el juego y al terminar regresa al menu del juego.
    public void opcionJugar(){
        opcionJugar.addActionListener(e -> {
            ventanaMenu.setVisible(false);
            new GameFrame(true);
        });
    }

    public void opcionJugarTutorial(){
        opcionJugarTutorial.addActionListener(e->{
            ventanaMenu.setVisible(false);
            new GameFrame(false);
        });
    }

    //Busca la imagen de el menu en la carpeta
    public ImageIcon buscarImagenMenu(){
        String rutaImagen = "tumorcito.jpg";
        java.net.URL imgURL = getClass().getResource(rutaImagen);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("No se encontró la imagen: " + rutaImagen);
            return new ImageIcon(); // Retorna un icono vacío en caso de error
        }
    }

    //Sale del menu y termina el programa, dando la opcion de aceptar o cancelar
    public void opcionSalir(){
        opcionSalir.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(ventanaMenu, "¿Seguro que quieres salir?", "Salir", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    //Genera el menu de juego
    public static void main(String[] args) {
        new MenuTumorcin();
    }

}
