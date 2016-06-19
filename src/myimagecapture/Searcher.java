/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myimagecapture;

import com.github.sarxos.webcam.Webcam;
import java.awt.Dimension;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Frederik
 */
public class Searcher extends Observable {

    SerialTest st = new SerialTest();

    int target1 = 0;
    int target2 = 0;
    int curr1 = 0;
    int curr2 = 0;

    public void Track(int seconds) {
        st.initialize();
        String str = JOptionPane.showInputDialog("test");
        st.writeData(str);
        boolean infi = false;
        if (seconds == -1) {
            //use -1 to keep it tracking non stop
            infi = true;
        }
        List<Webcam> list = Webcam.getWebcams();
        // til at finde dit camera
        /*for (int i = 0; i < list.size(); i++) {
         System.out.println(list.get(i).getName());
         }*/

        Webcam webcam = list.get(2);
        webcam.setViewSize(new Dimension(320, 240));
        webcam.open();
        BufferedImage imgg = webcam.getImage();
        int specialX = 600 / imgg.getWidth();
        int specialY = 600 / imgg.getHeight();

        try {
            ImageIO.write(imgg, "PNG", new File("first.png"));
        } catch (IOException ex) {
            Logger.getLogger(Searcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        JFrame jf = new JFrame();
        frame f = new frame(imgg.getWidth(), imgg.getHeight());
        jf.setTitle("MyTracker");
        jf.setSize(imgg.getWidth(), imgg.getHeight());
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.add(f);
        this.addObserver(f);
        //int[] blackDot = findColor(imgg, 10, 10, 10);
        int[] blackDot = {160, 120};
        long start = System.currentTimeMillis();
        long sendTime = System.currentTimeMillis();
        int[] lastPos = {0, 0};

        while (System.currentTimeMillis() < start + seconds * 1000 || infi) {
            BufferedImage img = webcam.getImage();
            int height = img.getHeight();
            int width = img.getWidth();
            ArrayList<RGB> rgbList = new ArrayList();
            int[] n = findColor(img, 86, 117, 166);
            int[] speed = {n[0] - lastPos[0], n[1] - lastPos[1]};
            lastPos = n;
            int[] finalPos = {0, 0};
            //finalPos[0] = n[0] - blackDot[0];
            //finalPos[1] = n[1] - blackDot[1];
            finalPos[0] = n[0] - blackDot[0] + speed[0];
            finalPos[1] = n[1] - blackDot[1] + speed[1];
            System.out.println("////");
            System.out.println(finalPos[0] + " " + finalPos[1]);
            System.out.println((finalPos[0] + speed[0]) + " " + (finalPos[1]+ speed[1])); 
            System.out.println("////");
            target1 = finalPos[0];
            target2 = finalPos[1];
            //System.out.println(finalPos[0] + " " + finalPos[1]);
            //System.out.println(n[0] + " " + n[1]);
            if (sendTime + 50 < System.currentTimeMillis()) {
                //st.writeData(finalPos[0] + " " + finalPos[1]);

                st.writeData(finalPos[0] + "," + finalPos[1] + ",");
                //System.out.println(finalPos[0] + "," + finalPos[1]);
//                String mess = SendTurnSignal(curr1, curr2, target1, target2);
//                System.out.println(mess);
//                st.writeData(mess);
                sendTime = System.currentTimeMillis();
            }

            Object[] pack = {img, n};
            setChanged();
            notifyObservers(pack);
        }
    }

    public String SendTurnSignal(int curr1, int curr2, int target1, int target2) {
        String end = "";
        if (curr1 < target1) {
            this.curr1++;
            end = end + "1,";
        } else {
            this.curr1--;
            end = end + "0,";
        }

        if (curr2 < target2) {
            this.curr2++;
            end = end + "1,";
        } else {
            this.curr2--;
            end = end + "0,";
        }

        return end;
    }

    public int[][] fintRoute(BufferedImage img) {
        int height = img.getHeight();
        int width = img.getWidth();
        ArrayList<RGB> startPoint = new ArrayList();
        ArrayList<int[]> usedPoints = new ArrayList();
        ArrayList<int[]> list = new ArrayList();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] colors = getPixelData(img, x, y);
                RGB pixelData = new RGB(colors[0], colors[1], colors[2], x, y);
                if (pixelData.red > 100 && pixelData.green < 70 && pixelData.blue < 70) {
                    startPoint.add(pixelData);
                }
            }
        }
        int maxX = 0;
        int maxY = 0;
        for (int i = 0; i < startPoint.size(); i++) {
            maxX += startPoint.get(i).x;
            maxY += startPoint.get(i).y;
        }
        int midtPunkt[] = {maxX / startPoint.size(), maxY / startPoint.size()};
        System.out.println(midtPunkt[0] + " " + midtPunkt[1]);
        int scopeX = 60;
        int scopeY = 60;
        boolean endPoint = false;
        while (!endPoint) {
            ArrayList<RGB> currArea = new ArrayList();
            for (int y = -scopeY / 2; y < scopeY / 2; y++) {
                for (int x = -scopeX / 2; x < scopeX / 2; x++) {
                    int currX = midtPunkt[0] + x;
                    int currY = midtPunkt[1] + y;

                    int[] colors = getPixelData(img, currX, currY);
                    RGB pixelData = new RGB(colors[0], colors[1], colors[2], currX, currY);
                    int[] currPixel = {pixelData.x, pixelData.y};
                    boolean used = false;
                    for (int i = 0; i < usedPoints.size(); i++) {
                        if (currPixel[0] == usedPoints.get(i)[0] && currPixel[1] == usedPoints.get(i)[1]) {
                            used = true;
                        }
                    }

                    if (pixelData.red < 10 && pixelData.green > 70 && pixelData.blue < 65) {
                        endPoint = true;
                    }

                    if (pixelData.red < 70 && pixelData.green < 165 && pixelData.blue > 60 && !used) {
                        currArea.add(pixelData);
                        usedPoints.add(currPixel);
                    }
                }
            }

            int cmaxX = 0;
            int cmaxY = 0;
            for (int i = 0; i < currArea.size(); i++) {
                cmaxX += currArea.get(i).x;
                cmaxY += currArea.get(i).y;
            }
            int cmidtPunkt[] = {cmaxX / currArea.size(), cmaxY / currArea.size()};
            System.out.println(cmidtPunkt[0] + " " + cmidtPunkt[1]);
            midtPunkt[0] = cmidtPunkt[0];
            midtPunkt[1] = cmidtPunkt[1];
        }
        return null;

    }

    private int[] findColor(BufferedImage img, int red, int green, int blue) {

        int height = img.getHeight();
        int width = img.getWidth();
        ArrayList<RGB> rgbList = new ArrayList();
        ArrayList<RGB> ball = new ArrayList();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] colors = getPixelData(img, x, y);
                RGB pixelData = new RGB(colors[0], colors[1], colors[2], x, y);
                if (pixelData.red > red - 20 && pixelData.red < red + 20
                        && pixelData.green > green - 20 && pixelData.green < green + 20
                        && pixelData.blue > blue - 20 && pixelData.blue < blue + 20) {
                    ball.add(pixelData);
                }
            }
        }

        int maxX = 0;
        int maxY = 0;
        for (int i = 0; i < ball.size(); i++) {
            maxX += ball.get(i).x;
            maxY += ball.get(i).y;
        }
        if (maxX == 0) {
            int temp[] = {0, 0};
            return temp;
        }
        int midtPunkt[] = {maxX / ball.size(), maxY / ball.size()};
        return midtPunkt;
    }

    private static int[] getPixelData(BufferedImage img, int x, int y) {
        int argb = img.getRGB(x, y);

        int rgb[] = new int[]{
            (argb >> 16) & 0xff, //red
            (argb >> 8) & 0xff, //green
            (argb) & 0xff //blue
        };

        return rgb;
    }

}
