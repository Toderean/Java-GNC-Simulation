import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class SimulationGrid extends JPanel{
        private int pixelHeight = 11;
        private int pixelWidth = 11;
        private int frameHeight = 800;
        private int frameWidth = 1000;
        private JPanel[][] matrixPixel;
        SimulationGrid(){
                matrixPixel = new JPanel[frameHeight/pixelHeight][frameWidth/pixelWidth];
        }

        public JPanel setGrid(){
                this.setLayout(new GridLayout( frameHeight/pixelHeight,frameWidth/pixelWidth));
                for (int i = 0 ; i < frameHeight/pixelHeight ; i++){
                        for (int j = 0 ; j <frameWidth/pixelWidth ; j++){
                                matrixPixel[i][j] = new JPanel();
                                matrixPixel[i][j].setPreferredSize(new Dimension(pixelWidth,pixelHeight));
                                matrixPixel[i][j].setBorder(new LineBorder(new Color(0,0,0)));
                                this.add(matrixPixel[i][j]);
                        }
                }
                return this;
        }

        public void paintCell(int x, int y){
                matrixPixel[y][x].setBackground(new Color(0,0,0));
        }
        public JPanel getCell(int x, int y){return matrixPixel[x][y];}
        public JPanel[][] getMatrix(){ return matrixPixel;}

        public int getCenterX(){return frameWidth/pixelWidth/2;}

        public int getCenterY(){return frameHeight/pixelHeight/2;}

        public int getRightWall(){
                return frameWidth/pixelWidth;
        }

        public int getBottomWall(){
                return frameHeight/pixelHeight;
        }

        public void cleanGrid(){
                for (int i = 0 ; i < frameHeight/pixelHeight ; i++){
                        for (int j = 0 ; j <frameWidth/pixelWidth ; j++){
                                matrixPixel[i][j].setBackground(new Color(255,255,255));
                        }
                }
        }

}
