/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myimagecapture;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.JPanel;

/**
 *
 * @author Frederik
 */
public class frame extends JPanel implements ActionListener, Observer {
    
    int width = 0;
    int height = 0;
    public frame(int x, int y){
        width = x;
        height = y;
    }
    
    Timer tm = new Timer(5, this);
    int x = 0, y = 0;
    BufferedImage img;
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.drawImage(img, 0, 0, width, height, null);
        g.fillOval(x, y, 5, 5);
        tm.start();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }

    public void newCord(int x, int y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }

    @Override
    public void update(Observable o, Object o1) {
        Object[] rec = (Object[])o1;
        int[] i = (int[])rec[1];
        BufferedImage pic = (BufferedImage)rec[0];
        newCord(i[0], i[1], pic);
    }
}
