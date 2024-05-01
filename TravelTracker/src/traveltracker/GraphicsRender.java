/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package traveltracker;


import interfaz.Message;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

/**
 *
 * @author Sebastian
 */
public class GraphicsRender {
    public int count = 0;
    
    public void render (ArrayList<ArrayList<Airport>> routes) {
        try {
            
            BufferedImage image = ImageIO.read(new File("src/images/Map.jpg"));

            //Position you wish to plot
            double lat = 11;
            double lon = 121;

            Graphics2D graphics = (Graphics2D) image.getGraphics();
            
            Airport prev = null;
            for (ArrayList<Airport> route: routes) {
                for (Airport airport: route) {
                    if (prev == null) {
                        drawAirport(graphics, image, airport);
                    } else {
                        drawAirport(graphics, image, airport);
                        drawRoute(graphics, image, prev, airport);
                    }
                    prev = airport;
                }
                prev = null;
            }

            ImageIO.write(image, "png", new File("src/generated_images/temp_"+count+".png"));
            
            count++;
            
            
            

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }
    

    public static void drawRoute (Graphics2D graphics, BufferedImage image, Airport source, Airport destination) {
            float R = (float) (707 / Math.PI / 2);
            double min_lat = -90;
            double min_lon = -150;
            double max_lat = 90;
            double max_lon = 180;
            
            int bubble_size = 10;

            double latExtent = max_lat - min_lat;
            double lonExtent = max_lon - min_lon;
            
            double lat = source.lat;
            double lon = source.lon;
            double lat2 = destination.lat;
            double lon2 = destination.lon;

            double ly1 = (image.getHeight() * (lat - min_lat)) / latExtent;
            double lx1 = (image.getWidth() * (lon - min_lon)) / lonExtent;

            ly1 = Math.log(Math.tan((Math.PI / 4) + ((lat * Math.PI/180) / 2)));
            int ly = (int) ((image.getHeight() / 2) - (image.getWidth() * ly1 / (2 * Math.PI)));
            int lx = (int) (lon + 180) * (image.getWidth() / 300);
            
            
            double ly12 = Math.log(Math.tan((Math.PI / 4) + ((lat2 * Math.PI/180) / 2)));
            int ly2 = (int) ((image.getHeight() / 2) - (image.getWidth() * ly12 / (2 * Math.PI)));
            int lx2 = (int) (lon2 + 180) * (image.getWidth() / 300);

            graphics.setColor(new Color(255, 0, 0));
            graphics.drawLine(lx, ly, lx2, ly2);
        }
    
    
        public static void drawAirport (Graphics2D graphics, BufferedImage image, Airport airport) {
            float R = (float) (707 / Math.PI / 2);
            double min_lat = -90;
            double min_lon = -180;
            double max_lat = 90;
            double max_lon = 180;
            
            int bubble_size = 10;

            double latExtent = max_lat - min_lat;
            double lonExtent = max_lon - min_lon;
            
            double lat = airport.lat;
            double lon = airport.lon;

            double ly1 = (image.getHeight() * (lat - min_lat)) / latExtent;
            double lx1 = (image.getWidth() * (lon - min_lon)) / lonExtent;

            ly1 = Math.log(Math.tan((Math.PI / 4) + ((lat * Math.PI/180) / 2)));
            int ly = (int) ((image.getHeight() / 2) - (image.getWidth() * ly1 / (2 * Math.PI)));
            int lx = (int) (lon + 180) * (image.getWidth() / 300);

            graphics.setColor(new Color(255, 0, 0));
            graphics.fillOval(lx - bubble_size / 2, ly - bubble_size / 2,
                    bubble_size, bubble_size);
            graphics.drawString(airport.code, lx + 10, ly);
    }
}
