package stan.free.space.ui.controls.canvases;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import stan.free.space.core.FSCore;
import stan.free.space.core.generators.LocationGenerator;

public class SpaceCanvas
    extends Canvas
{
    //VIEWS

    //FIELDS
    private ArrayList tiles;
    private int hei = 132;
    private int wid = 132;

    public SpaceCanvas()
    {
        super();
    }

    public void initCanvasFromLocation(HashMap location)
    {
    	HashMap rect = (HashMap)location.get("rect");
        System.out.println("SpaceCanvas rect " + rect);
        int h = ((Number)rect.get("h")).intValue();
        int w = ((Number)rect.get("w")).intValue();
        tiles = LocationGenerator.createTilesList(w, h, (ArrayList)location.get("tiles"));
        //int correctH = LocationGenerator.correctH(w, h, tiles);
        int correctH = 0;
        //int correctW = LocationGenerator.correctW(w, h, tiles);
        int correctW = 0;
        //this.setWidth(w*wid/2+h*wid/2+124);
        //this.setHeight((h+w)*hei/4 + hei);
        this.setWidth((w-correctW)*wid/2+(h-correctH)*wid/2 + wid);
        this.setHeight((h+w)*hei/4 + hei);
		getGraphicsContext2D().setFill(Color.BLACK);
        getGraphicsContext2D().fillRect(0, 0, this.getWidth(), this.getHeight());
		getGraphicsContext2D().setFill(Color.WHITE);
        System.out.println("SpaceCanvas" + " drawTiles");
        drawTiles((h-correctH)*wid/2, wid);
    }
    private void drawTiles(int centerX, int offsetY)
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
			int x = ((Number)coordinates.get("x")).intValue();
			int y = ((Number)coordinates.get("y")).intValue();
	        int offset = y*wid/2;
			ArrayList levels = (ArrayList)tile.get("levels");
    		ArrayList levelTiles = (ArrayList)levels.get(0);
    		HashMap levelTile = (HashMap)levelTiles.get(0);
			String image = (String)levelTile.get("image");
            drawTile(FSCore.getInstance().getImagePath(image), x*wid/2-offset+centerX, (y+x)*hei/4 + offsetY);
			getGraphicsContext2D().fillText(x + " " + y, x*wid/2-offset+centerX, (y+x)*hei/4 + offsetY);
            //drawTileAsync(FSCore.getInstance().getImagePath(image), x+y*200, x*wid/2-offset+centerX, (y+x)*hei/4 + offsetY);
    	}
	}
    private void drawTile(String tileName, int x, int y)
    {
        Image image = new Image("file:"+tileName);
        getGraphicsContext2D().drawImage(image, x, y-image.getHeight());
    }
    private void drawTileAsync(String tileName, int sleep, int x, int y)
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(sleep);
                }
                catch(Exception e)
                {
                    System.out.println("Thread.sleep - " + e.getMessage());
                }
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        drawTile(tileName, x, y);
                    }
                });
            }
        }).start();
    }
}