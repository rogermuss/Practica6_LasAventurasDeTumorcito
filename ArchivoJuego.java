package Practica_6;

import java.io.*;

public class ArchivoJuego {
    private String ruta;

    public ArchivoJuego(String ruta) {
        this.ruta = ruta;
    }

    public void guardar(Jugador jugador) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ruta))) {
            pw.println(jugador.getX());
            pw.println(jugador.getY());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargar(Jugador jugador) {
        File f = new File(ruta);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            jugador.x = Integer.parseInt(br.readLine());
            jugador.y = Integer.parseInt(br.readLine());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
