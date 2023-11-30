//package com.ftt;
//
//import java.awt.Canvas;
//import java.awt.Frame;
//import java.awt.Graphics;
//import java.awt.Image;
//import java.awt.Toolkit;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//
//public class ImageCanvas extends Canvas {
//
//    private Image[] images;
//
//    public ImageCanvas(String[] imagePaths) {
//        // Load multiple images from file
//        images = new Image[imagePaths.length];
//        for (int i = 0; i < imagePaths.length; i++) {
//            images[i] = Toolkit.getDefaultToolkit().getImage(imagePaths[i]);
//        }
//        
//    }
//
//    @Override
//    public void paint(Graphics g) {
//        // Render each image at different positions on the canvas
//        for (int i = 0; i < images.length; i++) {
//            int x = i * 200;  // Adjust the x-coordinate based on the index
//            int y = 0;       // Set a constant y-coordinate for simplicity
//            g.drawImage(images[i], x, y, 180, 320, this);
//        }
//        
//    }
//
//    public static void main(String[] args) {
//        // Create a frame to hold the canvas
//        Frame frame = new Frame("Image Canvas Example");
//
//        // Create an instance of the ImageCanvas with an array of image paths
//        String[] imagePaths = {"src/assets/foto1.jpg", "src/assets/foto2.jpg", "src/assets/foto3.jpg"};
//        ImageCanvas imageCanvas = new ImageCanvas(imagePaths);
//
//        // Add the canvas to the frame
//        frame.add(imageCanvas);
//
//        // Set the size of the frame
//        frame.setSize(800, 400);
//
//        // Make the frame visible
//        frame.setVisible(true);
//
//        // Ensure the application exits when the frame is closed
//        frame.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent we) {
//                System.exit(0);
//            }
//            
//        });
//        
//    }
//    
//}
