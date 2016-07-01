package stan.free.space.ui.panes.main;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import stan.free.space.core.FSCore;
import stan.free.space.core.LocationGenerator;

import stan.free.space.ui.controls.canvases.SpaceCanvas;

public class MainPane
    extends VBox
{
    //VIEWS
    private SpaceCanvas spaceCanvas;

    //FIELDS

    public MainPane()
    {
        super();
        this.getStylesheets().add("css/theme.css");
        initViews();
        spaceCanvas.initCanvasFromLocation(LocationGenerator.generateCorridor());
		//LocationGenerator.generateCorridor();
        //test();
    }
    private void initViews()
    {
    	spaceCanvas = new SpaceCanvas();
        ScrollPane scrollPane = new ScrollPane(spaceCanvas);
        //this.getChildren().add(spaceCanvas);
        this.getChildren().add(scrollPane);
    }

    private void test()
    {
		HashMap location = FSCore.getInstance().getLocation("corridor");
        if(location != null)
        {
            spaceCanvas.initCanvasFromLocation(location);
        }
    }
}