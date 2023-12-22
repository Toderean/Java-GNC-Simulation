import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import static java.lang.Math.abs;

public class Line {
    private int x;
    private int y;
    private int feedRate;
    private int i;
    private int j;

    protected String rapidRegex = "(G\\d{1,})\\s((X[-]\\d{1,})\\s(Y[-]\\d{1,})|(X\\d{1,})\\s(Y\\d{1,}))";
    protected String linearRegex = "(G\\d{1,})\\s((X[-]\\d{1,})\\s(Y[-]\\d{1,})\\s(F\\d{1,})|(X\\d{1,})\\s(Y[-]\\d{1,})\\s(F\\d{1,})|(X[-]\\d{1,})\\s(Y\\d{1,})\\s(F\\d{1,})|(X\\d{1,})\\s(Y\\d{1,})\\s(F\\d{1,}))";
    protected String cirularRegex = "(G02)\\s((X\\d{1,})\\s(Y\\d{1,})\\s(I\\d{1,})\\s(J\\d{1,})|(X[-]\\d{1,})\\s(Y\\d{1,})\\s(I\\d{1,})\\s(J\\d{1,})|(X\\d{1,})\\s(Y[-]\\d{1,})\\s(I\\d{1,})\\s(J\\d{1,})|(X\\d{1,})\\s(Y\\d{1,})\\s(I[-]\\d{1,})\\s(J\\d{1,})|(X\\d{1,})\\s(Y\\d{1,})\\s(I\\d{1,})\\s(J[-]\\d{1,})|(X\\d{1,})\\s(Y\\d{1,})\\s(I[-]\\d{1,})\\s(J[-]\\d{1,})|(X[-]\\d{1,})\\s(Y\\d{1,})\\s(I\\d{1,})\\s(J[-]\\d{1,})|(X\\d{1,})\\s(Y[-]\\d{1,})\\s(I\\d{1,})\\s(J[-]\\d{1,})|(X[-]\\d{1,})\\s(Y[-]\\d{1,})\\s(I\\d{1,})\\s(J\\d{1,})|(X[-]\\d{1,})\\s(Y[-]\\d{1,})\\s(I[-]\\d{1,})\\s(J[-]\\d{1,}))";
    protected String counterCirularRegex = "(G03)\\s((X\\d{1,})\\s(Y\\d{1,})\\s(I\\d{1,})\\s(J\\d{1,})|(X[-]\\d{1,})\\s(Y\\d{1,})\\s(I\\d{1,})\\s(J\\d{1,})|(X\\d{1,})\\s(Y[-]\\d{1,})\\s(I\\d{1,})\\s(J\\d{1,})|(X\\d{1,})\\s(Y\\d{1,})\\s(I[-]\\d{1,})\\s(J\\d{1,})|(X\\d{1,})\\s(Y\\d{1,})\\s(I\\d{1,})\\s(J[-]\\d{1,})|(X\\d{1,})\\s(Y\\d{1,})\\s(I[-]\\d{1,})\\s(J[-]\\d{1,})|(X[-]\\d{1,})\\s(Y\\d{1,})\\s(I\\d{1,})\\s(J[-]\\d{1,})|(X\\d{1,})\\s(Y[-]\\d{1,})\\s(I\\d{1,})\\s(J[-]\\d{1,})|(X[-]\\d{1,})\\s(Y[-]\\d{1,})\\s(I\\d{1,})\\s(J\\d{1,})|(X[-]\\d{1,})\\s(Y[-]\\d{1,})\\s(I[-]\\d{1,})\\s(J[-]\\d{1,}))";

    Pattern rapidPattern = Pattern.compile(rapidRegex);
    Pattern linearPattern = Pattern.compile(linearRegex);
    Pattern circularPattern = Pattern.compile(cirularRegex);
    Pattern counterCircularPattern = Pattern.compile(counterCirularRegex);

    Line(){}

    public int lineType(String regex){
         if (linearPattern.matcher(regex).matches()) {
            return setCoordLinearInterpolation(regex);
        } else if(circularPattern.matcher(regex).matches()){
            return setCircularPattern(regex);
        } else if(rapidPattern.matcher(regex).matches()){
             return setCoordRapidPositioning(regex);
         }else if(counterCircularPattern.matcher(regex).matches()){
             return setCounterCircularPattern(regex);
         }
        return -1;
    }
    public int setCoordLinearInterpolation(String regex){
        x = Integer.parseInt(regex.substring(regex.indexOf("X") + 1, regex.indexOf("Y") - 1));
        y = Integer.parseInt(regex.substring(regex.indexOf("Y") + 1, regex.indexOf("F") - 1));
        feedRate = Integer.parseInt(regex.substring(regex.indexOf("F") + 1));
        return 1;
    }
    public int setCoordRapidPositioning(String regex){
        x = Integer.parseInt(regex.substring(regex.indexOf("X") + 1, regex.indexOf("Y") - 1));
        y = Integer.parseInt(regex.substring(regex.indexOf("Y") + 1));
        return 0;
    }
    public int setCircularPattern(String regex){
        x = Integer.parseInt(regex.substring(regex.indexOf("X") + 1, regex.indexOf("Y") - 1));
        y = Integer.parseInt(regex.substring(regex.indexOf("Y") + 1,regex.indexOf("I") - 1));
        i = Integer.parseInt(regex.substring(regex.indexOf("I") + 1,regex.indexOf("J") - 1));
        j = Integer.parseInt(regex.substring(regex.indexOf("J") + 1));
        return 2;
    }

    public int setCounterCircularPattern(String regex){
        x = Integer.parseInt(regex.substring(regex.indexOf("X") + 1, regex.indexOf("Y") - 1));
        y = Integer.parseInt(regex.substring(regex.indexOf("Y") + 1,regex.indexOf("I") - 1));
        i = Integer.parseInt(regex.substring(regex.indexOf("I") + 1,regex.indexOf("J") - 1));
        j = Integer.parseInt(regex.substring(regex.indexOf("J") + 1));
        return 3;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getFeedRate() {
        return feedRate;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public int getOctant(Line line, int startX, int startY){
        int deltaY = line.getY() - startY;
        int deltaX = line.getX() - startX;

        float m = abs (deltaY) / (float)abs(deltaX);

        if ((m < 1) && (deltaX > 0) && (deltaY <= 0))
            return 1;
        if ((m > 1) && (deltaX > 0) && (deltaY <= 0))
            return 2;
        if ((m > 1) && (deltaX < 0) && (deltaY <= 0))
            return 3;
        if ((m < 1) && (deltaX < 0) && (deltaY <= 0))
            return 4;
        if ((m < 1) && (deltaX < 0) && (deltaY >= 0))
            return 5;
        if ((m > 1) && (deltaX < 0) && (deltaY >= 0))
            return 6;
        if ((m > 1) && (deltaX > 0) && (deltaY >= 0))
            return 7;
        if ((m < 1) && (deltaX > 0) && (deltaY >= 0))
            return 8;

        return 0;
    }

    public void rasterization(Line line, AtomicInteger startX, AtomicInteger startY, SimulationGrid simulationGrid){
        int tmpStartX = 0;
        int tmpStartY = 0;
        int tmpEndX = 0;
        int tmpEndY = 0;
        int tmpCurrentX = 0;
        int tmpCurrentY = 0;

        int dx = abs(line.getX() - startX.get());
        int dy = abs(line.getY() - startY.get());

        int d , inc1,inc2;
        int octant = getOctant(line,startX.get(),startY.get());
        switch (octant){
            case 1, 3, 5, 6, 8, 7:
                tmpStartX = tmpCurrentX = startX.get();
                tmpEndX = line.getX();
                tmpStartY = tmpCurrentY = startY.get();
                tmpEndY = line.getY();

                d = 2 * dy - dx;
                inc1 = 2 * dy;
                inc2 = 2 * (dy - dx);

                while (tmpCurrentX < tmpEndX)
                {
                    //Draw current point
                    simulationGrid.paintCell(tmpCurrentX,tmpCurrentY);
                    ++tmpCurrentX;

                    if (d < 0)
                        d += inc1;
                    else
                    {
                        d += inc2;
                        --tmpCurrentY;
                    }
                }
                break;
            case 2:
                tmpStartX = tmpCurrentX = startX.get();
                tmpEndX = line.getX();
                tmpStartY = tmpCurrentY = startY.get();
                tmpEndY = line.getY();

                d = 2 * dy - dx;
                inc1 = 2 * dy;
                inc2 = 2 * (dy - dx);

                while (tmpCurrentY < tmpEndY)
                {
                    //Draw current point
                    simulationGrid.paintCell(tmpCurrentX,tmpCurrentY);
                    ++tmpCurrentY;

                    if (d < 0)
                        d += inc1;
                    else
                    {
                        d += inc2;
                        --tmpCurrentX;
                    }
                }
                break;

            case 4:
                tmpStartX = tmpCurrentX = startX.get();
                tmpEndX = line.getX();
                tmpStartY = tmpCurrentY = startY.get();
                tmpEndY = line.getX();

                d = 2 * dy - dx;
                inc1 = 2 * dy;
                inc2 = 2 * (dy - dx);

                while (tmpCurrentX < tmpEndX)
                {
                    //Draw current point
                    simulationGrid.paintCell(tmpCurrentX,tmpCurrentY);
                    ++tmpCurrentX;

                    if (d < 0)
                        d += inc1;
                    else
                    {
                        d += inc2;
                        --tmpCurrentY;
                    }
                }
                break;

            default:
                break;
        }
    }

}


//    Thread thread = new Thread(()->{
//        int dx = abs(lineType.getX() - sx);
//        int dy = abs(lineType.getY() - sy);
//        int d = 2*dy - dx;
//        int inc1 = 2*dy;
//        int inc2 = 2*(dy-dx);
//
//        int endX = sx + lineType.getX();
//        int endY = sy + lineType.getY();
//        int currentX = sx;
//        int currentY = sy;
//
//        while(currentX != endX && currentY != endY){
//            simulationPanel.paintCell(currentX,currentY);
//            startX.set(currentX);
//            startY.set(currentY);
//            currentX += 1;
//            if (d < 0) {
//                d += inc1;
//            } else {
//                currentY -= 1;
//                d += inc2;
//            }
//            try{
//                Thread.sleep(10);
//            }catch (InterruptedException ex){
//                ex.printStackTrace();
//            }
//        }
//    });
//                            thread.start();