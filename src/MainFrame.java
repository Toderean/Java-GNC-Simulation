import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.*;

///grid
// 72 Lines
// 90 Cols



public class MainFrame extends JFrame {

    Line lineType;
    private ArrayList<String> array= new ArrayList<String>();
    MainFrame() throws IOException {
        this.setSize(1250,900);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        lineType = new Line();

        JPanel commandsPanel = new JPanel();
        SimulationGrid simulationPanel = new SimulationGrid();
        JLabel textLabel = new JLabel();
        JMenuBar menuBar = new JMenuBar();
        JMenu lineDraw = new JMenu("Draw");
        JMenuItem item = new JMenuItem("Line");
        JMenuItem start = new JMenuItem("Start");

        textLabel.setText("Commands:\n");

        commandsPanel.setBounds(0,0,200,900);
        commandsPanel.setBorder(new LineBorder(new Color(0,0,0)));
        commandsPanel.setLayout(new BoxLayout(commandsPanel,BoxLayout.Y_AXIS));
        commandsPanel.add(textLabel);

        simulationPanel.setGrid();
        simulationPanel.setBackground(new Color(0,0,255));
        simulationPanel.setBounds(200,0,1050,900);
        simulationPanel.setBorder(new LineBorder(new Color(0,0,0)));

        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Line Type");
                frame.setLocationRelativeTo(null);
                frame.setSize(400,100);
                frame.getContentPane().setLayout(new FlowLayout());
                JLabel text = new JLabel();
                text.setText("Command: ");
                JTextField textField = new JTextField(20);
                JButton write = new JButton("Write file");
                frame.getContentPane().add(text);
                frame.getContentPane().add(textField);
                frame.getContentPane().add(write);
                write.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            JLabel command = new JLabel();
                            array.add(textField.getText());
                            command.setText(textField.getText());
                            commandsPanel.add(command);
                            commandsPanel.revalidate();
                            commandsPanel.repaint();
                            frame.dispose();
                    }
                });
                frame.setResizable(false);
                frame.setVisible(true);
            }
        });

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulationPanel.cleanGrid();
                AtomicInteger startX = new AtomicInteger(simulationPanel.getCenterX());
                AtomicInteger startY = new AtomicInteger(simulationPanel.getCenterY());
                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Commands.txt"));
                    for(String lines: array){
                        bufferedWriter.write(lines);
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.close();

                    BufferedReader bufferedReader = new BufferedReader(new FileReader("Commands.txt"));
                    Thread thread = new Thread(()->{
                        int feedRate = 10;
                        String line;
                    while(true) {
                        try {
                            if ((line = bufferedReader.readLine()) == null) break;
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        int sx = startX.get();
                        int sy = startY.get();
                        int code = lineType.lineType(line);
                        System.out.println(code);
                        if (code == 0) {
                            System.out.println("x= " + lineType.getX() + "y= " + lineType.getY());
                            System.out.println("initial starting point: " + startX.get() + " " + startY.get());
                            startX.set(lineType.getX());
                            startY.set(lineType.getY());
                            simulationPanel.paintCell(startX.get(),startY.get());
                            System.out.println("after rapid positioning: " + startX.get() + " " + startY.get());
                        } else if (code == 1) {
                            System.out.println("x= " + lineType.getX() + "y= " + lineType.getY() + "feedRate= " + lineType.getFeedRate());
                            int currentX = startX.get();
                            int cX = startX.get();
                            int currentY = startY.get();
                            int endX = startX.get() + lineType.getX();
                            int endY = startY.get() + lineType.getY();
                            feedRate = lineType.getFeedRate();
                            System.out.println(sx + " " + sy + " " + endX + " " + endY);

                            while (currentY != endY && currentX != endX){
//                                if(currentY == endY && currentX == endX)
//                                    break;
                                if (currentY >= simulationPanel.getRightWall() || currentY < 0)
                                    break;
                                if (currentX >= simulationPanel.getBottomWall() || currentX < 0)
                                    break;
                                if (endX < sx) {

                                    currentX -= 1;
                                    currentY = (int) (sy - (currentX - sx) * (endY - sy) / (double) (endX - sx));

                                    simulationPanel.paintCell(currentX, currentY);

                                    double temp = (sy - (currentX - sx) * (endY - sy) / (double) (endX - sx));
                                    double aux = abs(temp - (double) currentY);

                                    System.out.println("aux = " + aux + " temp = " + temp + "currentY = " +currentY);

                                    if(aux <= 0.01){
                                        currentY -=1;
                                    }else currentY +=1;

                                } else {

                                    currentX += 1;
                                    currentY = (int) (sy - (currentX - sx) * (endY - sy) / (double) (endX - sx));

                                    simulationPanel.paintCell(currentX, currentY);

                                    double temp = (sy - (currentX - sx) * (endY - sy) / (double) (endX - sx));
                                    double aux = abs(temp - (double) currentY);
                                    System.out.println("aux = " + aux + " temp = " + temp + " currentY = " +currentY);

                                    if(aux <= 0.01){
                                        currentY -=1;
                                    }else currentY +=1;
                                }
                                System.out.println(currentX + " " + currentY);
                                simulationPanel.paintCell(currentX, currentY);

                                startX.set(currentX);
                                startY.set(currentY);
                                try {
                                    Thread.sleep(feedRate);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } else if (code == 2) {
//                            simulationPanel.paintCell(lineType.getX(),lineType.getY());
                            System.out.println("x= " + lineType.getX() + " y= " + lineType.getY() + " I= " + lineType.getI() + " J= " + lineType.getJ());
                            int endX = startX.get() + lineType.getX();
                            int endY = startY.get() + lineType.getY();
                            double currentX = startX.get();
                            double currentY = startY.get();
                            int centerX = startX.get() + lineType.getI();
                            int centerY = startY.get() + lineType.getJ();
                            double radius = sqrt(pow(centerX - startX.get(),2) + pow(centerY - startY.get(),2));

                            System.out.println("startX = " +startX.get() +" startY = " + startY.get());
                            System.out.println("centerX = " + centerX +" centerY = " + centerY + " endX = " + endX+ " endY = "+endY);
                            System.out.println("radius = " + radius);


                            double startAngle = atan2(startY.get() - centerY, startX.get() - centerX);
                            double endAngle = atan2(endY - startY.get(), endX - startX.get());
                            endAngle += 2*Math.PI;

                            double angleInc = (endAngle-startAngle)/100;
                            double currentAngle;

                            for (int i = 0 ; i < 100 ; i++){
                                currentAngle = startAngle + i * angleInc;

                                System.out.println("cos = " + cos(currentAngle) + " sin = "+ sin(currentAngle) + " radius* = "+ radius*cos(currentAngle));

                                currentX = centerX + radius * cos(currentAngle);
                                currentY = centerY + radius * sin(currentAngle);

                                System.out.println("currentX = " + currentX + " currentY = " + currentY);

                                if(centerX >= 0 && centerY >= 0) {
                                    simulationPanel.paintCell((int) currentX, (int) currentY);
                                }
                                if (abs(currentX - endX) < 1 && abs(currentY -endY) < 1) {
                                    //G02 X10 Y9 I5 J5 for start 50,46
                                    startX.set((int) currentX);
                                    startY.set((int) currentY);
                                    break;
                                }
                                try {
                                    Thread.sleep(30);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }

                            }
                        }
                       else if(code == 3){
                            simulationPanel.paintCell(lineType.getX(),lineType.getY());
                            System.out.println("x= " + lineType.getX() + " y= " + lineType.getY() + " I= " + lineType.getI() + " J= " + lineType.getJ());
                            int endX = startX.get() + lineType.getX();
                            int endY = startY.get() + lineType.getY();
                            double currentX = startX.get();
                            double currentY = startY.get();
                            int centerX = startX.get() + lineType.getI();
                            int centerY = startY.get() + lineType.getJ();
                            double radius = sqrt(pow(centerX - startX.get(),2) + pow(centerY - startY.get(),2));

                            System.out.println("startX = " +startX.get() +" startY = " + startY.get());
                            System.out.println("centerX = " + centerX +" centerY = " + centerY + " endX = " + endX+ " endY = "+endY);
                            System.out.println("radius = " + radius);


                            double startAngle = atan2(startY.get() - centerY, startX.get() - centerX);
                            double endAngle = atan2(endY - startY.get(), endX - startX.get());
                            endAngle -= 2*Math.PI;

                            double angleInc = (endAngle-startAngle)/100;
                            double currentAngle;

                            for (int i = 0 ; i < 100 ; i++){
                                currentAngle = startAngle + i * angleInc;

                                System.out.println("cos = " + cos(currentAngle) + " sin = "+ sin(currentAngle) + " radius* = "+ radius*cos(currentAngle));

                                currentX = centerX + radius * cos(currentAngle);
                                currentY = centerY + radius * sin(currentAngle);

                                System.out.println("currentX = " + currentX + " currentY = " + currentY);

                                if(centerX >= 0 && centerY >= 0) {
                                    simulationPanel.paintCell((int) currentX, (int) currentY);
                                }
                                if (abs(currentX - endX) < 1 && abs(currentY -endY) < 1) {
                                    //G02 X10 Y9 I5 J5 for start 50,46
                                    startX.set((int) currentX);
                                    startY.set((int) currentY);
                                    break;
                                }
                                try {
                                    Thread.sleep(30);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }

                            }
                        }
                    }
                    });
                    thread.start();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        this.getContentPane().setLayout(null);
        this.getContentPane().add(commandsPanel);
        this.getContentPane().add(simulationPanel);
        this.setJMenuBar(menuBar);
        menuBar.add(lineDraw);
        lineDraw.add(item);
        lineDraw.add(start);

        this.setResizable(false);
        this.setVisible(true);
    }
}
