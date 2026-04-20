package controlador;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

// 🎵 NUEVO - Clase entera nueva
public class GestorMusica {

    private static MediaPlayer mediaPlayer;

    public static void reproducirMusica(String nombreArchivo) {
        try {
            detener(); // para cualquier música previa
            String uri = GestorMusica.class
                .getResource("/resources/" + nombreArchivo)
                .toExternalForm();

            Media media = new Media(uri);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.4);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Error al cargar música: " + e.getMessage());
        }
    }

    public static void detener() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    public static void pausar() {
        if (mediaPlayer != null) mediaPlayer.pause();
    }

    public static void reanudar() {
        if (mediaPlayer != null) mediaPlayer.play();
    }

    public static void setVolumen(double volumen) {
        if (mediaPlayer != null) mediaPlayer.setVolume(volumen);
    }
}