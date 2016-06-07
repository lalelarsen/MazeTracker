/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myimagecapture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Frederik
 */
public class MyImageCapture {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Searcher s = new Searcher();
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("route.png"));
        } catch (IOException e) {
        }
        //s.fintRoute(img);
        s.Track(-1);

    }
}
