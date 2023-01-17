package OpencvCam;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OpencvCam extends JFrame {

    //JLabel is something that is used to display text, image, etc. so basically this is cam screen
    private JLabel camScreen;

    private JButton btnCapture;

    private VideoCapture capture;

    //The Mat class of OpenCV library is used to store the values of an image. It represents an n-dimensional array and is used to store image data of grayscale or color images, voxel volumes, vector fields, point clouds, tensors, histograms, etc.
    private Mat image;

    private boolean clicked = false;

    public OpencvCam() {
        //Design the UI
        setLayout(null);

        camScreen = new JLabel();
        camScreen.setBounds(0,0, 640,480);
        add(camScreen);

        btnCapture = new JButton("Capture");
        btnCapture.setBounds(300,480,80,40);
        add(btnCapture);

        btnCapture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clicked = true;
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                capture.release();
                image.release();
                System.exit(0);
            }
        });

        setSize(new Dimension(640, 560));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    //connecting camera to the screen
    public void startCamera() {
        capture = new VideoCapture(0);
        image = new Mat();
        byte[] imageData;
        ImageIcon icon;

        while(true) {
            //read image to matrix
            capture.read(image);

            //convert matrix to byte
            final MatOfByte buf = new MatOfByte();

            //Imgcodecs - An abstract class allowing the creation of image decoders and encoders.
            Imgcodecs.imencode(".jpg",image, buf);

            imageData = buf.toArray();

            //add to JLabel
            icon = new ImageIcon(imageData);
            camScreen.setIcon(icon);

            //capture and save to file
            if (clicked) {
                //prompt for entering image name
                String name = JOptionPane.showInputDialog(this,"Enter image name");
                if(name == null){
                    name = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss").format(new Date());
                }
//                //if cancel button is pressed
//                else {
//
//                }
                //write to file
                Imgcodecs.imwrite("images/" + name + ".jpg",image);

                clicked = false;
            }
        }
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        System.out.println("load success");
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                OpencvCam camera = new OpencvCam();
                //start camera in thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        camera.startCamera();
                    }
                }).start();
            }
        });


    }
}
