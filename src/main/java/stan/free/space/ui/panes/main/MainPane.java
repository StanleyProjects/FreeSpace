package stan.free.space.ui.panes.main;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import javafx.scene.layout.VBox;

import stan.free.space.core.FSCore;
import stan.free.space.core.Location;
import stan.free.space.core.LocationGenerator;

public class MainPane
    extends VBox
{
    //VIEWS
    private Canvas canvas;

    //FIELDS
    private GraphicsContext graphicsContext;
	private int hei = 132;
	private int wid = 132;

    public MainPane()
    {
        super();
        this.getStylesheets().add("css/theme.css");
        initViews();
		//LocationGenerator.generate();
        test();
    }
    private void initViews()
    {
    	canvas = new Canvas();
        graphicsContext = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);
    }

    private void test()
    {
		HashMap location = FSCore.getInstance().getLocation("testloc");
        if(location != null)
        {
        	initCanvasFromLocation(location);
        }
    }
    private void initCanvasFromLocation(HashMap location)
    {
    	HashMap rect = (HashMap)location.get("rect");
        int h = ((Long)rect.get("h")).intValue();
        int w = ((Long)rect.get("w")).intValue();
    	canvas.setWidth(w*wid/2+h*wid/2+124);
    	canvas.setHeight((h+w)*hei/4 + hei);
		graphicsContext.setFill(Color.GREEN);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawTiles(graphicsContext, (ArrayList)location.get("tiles"), h*wid/2, wid);
    }
    private void drawTiles(GraphicsContext gc, ArrayList tiles, int centerX, int offsetY)
    {
    	for(int i=0; i<tiles.size(); i++)
    	{
    		HashMap tile = (HashMap)tiles.get(i);
	        long type = (Long)tile.get("type");
			if(type == -1)
			{
				continue;
			}
			HashMap coordinates = (HashMap)tile.get("coordinates");
			int x = ((Long)coordinates.get("x")).intValue();
			int y = ((Long)coordinates.get("y")).intValue();
	        int offset = y*wid/2;
			ArrayList levels = (ArrayList)tile.get("levels");
    		ArrayList levelTiles = (ArrayList)levels.get(0);
    		HashMap levelTile = (HashMap)levelTiles.get(0);
			String image = (String)levelTile.get("image");
            drawTile(gc, FSCore.getInstance().getImagePath(image), x*wid/2-offset+centerX, (y+x)*hei/4 + offsetY);
    	}
	}

    private void drawTile(GraphicsContext gc, String tileName, int x, int y)
    {
        Image image = new Image("file:"+tileName);
        gc.drawImage(image, x, y-image.getHeight());
    }
}