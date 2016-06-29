package stan.free.space.ui.panes.main;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import javafx.scene.layout.VBox;

import stan.free.space.core.Location;

public class MainPane
    extends VBox
{
    //VIEWS

    //FIELDS
    private GraphicsContext gc;

    public MainPane()
    {
        super();
        this.getStylesheets().add("css/theme.css");
        initViews();
    }
    private void initViews()
    {
        Canvas canvas = new Canvas(666, 666);
        gc = canvas.getGraphicsContext2D();
        drawTiles(gc);
        this.getChildren().add(canvas);
    }

    private void drawShapes(GraphicsContext gc)
    {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 10, 40);
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);
        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
        gc.fillPolygon(new double[] {10, 40, 10, 40},
                       new double[] {210, 210, 240, 240}, 4);
        gc.strokePolygon(new double[] {60, 90, 60, 90},
                         new double[] {210, 210, 240, 240}, 4);
        gc.strokePolyline(new double[] {110, 140, 110, 140},
                          new double[] {210, 210, 240, 240}, 4);
    }

    private void drawMatrix(GraphicsContext gc)
    {
        int h = 15;
        int w = 10;
        int hei = 10;
        int wid = 20;
        for(int i=0; i<h; i++)
        {
            int y = i;
            for(int j=0; j<w; j++)
            {
                //gc.strokeOval(i*15, j*15, 10, 10);
                int offset = i*wid/2;
                //gc.strokeLine(j*wid/2-offset, y*hei/2, (j+1)*wid/2-offset, (y+1)*hei/2);
                //gc.strokeLine((j+1)*wid/2-offset, (y+1)*hei/2, j*wid/2-offset, (y+2)*hei/2);
                gc.fillPolygon(new double[] {j*wid/2-offset, (j+1)*wid/2-offset, j*wid/2-offset, (j-1)*wid/2-offset},
                               new double[] {y*hei/2, (y+1)*hei/2, (y+2)*hei/2, (y+1)*hei/2}, 2);
                y++;
            }
        }
    }

    private void drawImage(GraphicsContext gc, int x, int y)
    {
        Image image = new Image("res/images/test_tile.png");
        gc.drawImage(image, x, y);
    }

    private void drawTiles(GraphicsContext gc)
    {
        int h = 4;
        int w = 5;
        int hei = 48;
        int wid = 96;
        for(int i=0; i<h; i++)
        {
            int y = i;
            int offset = i*wid/2;
            for(int j=0; j<w; j++)
            {
                drawImage(gc, (j-1)*wid/2-offset, y*hei/2);
                y++;
            }
        }
    }
}