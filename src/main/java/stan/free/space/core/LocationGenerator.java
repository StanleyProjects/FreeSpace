package stan.free.space.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import stan.free.space.helpers.FileHelper;
import stan.free.space.helpers.json.JSONWriter;

public class LocationGenerator
{
	static public void generate()
	{
		String name = "testloc";
		int w = 4;
		int h = 3;
		int beginX = 0;
		int beginY = 1;
		HashMap location = new HashMap<String, Object>();
		HashMap rect = new HashMap<String, Object>();
		rect.put("w", w);
		rect.put("h", h);
		location.put("rect", rect);
		HashMap begin = new HashMap<String, Object>();
		begin.put("x", beginX);
		begin.put("y", beginY);
		location.put("begin", begin);
		ArrayList allTiles = new ArrayList<Object>();
		//
		ArrayList floorTiles = new ArrayList<Object>();
		HashMap floorTile = FSCore.getInstance().getTile("floor_ground_center");
		floorTiles.addAll(addTiles(beginX, beginY, 2, w, floorTile));
		allTiles.addAll(floorTiles);
		//
		ArrayList tiles = createTilesList(w, h, allTiles);
		location.put("tiles", tiles);
        String data = null;
        try
        {
            data = JSONWriter.mapToJSONString(location);
        }
        catch(Exception e)
        {
            System.out.println("mapToJSONString - " + e.getMessage());
            return;
        }
        File main = new File(FSCore.systemPath + "/locations/" + name + ".lct");
        try
        {
            FileHelper.writeFile(data, FSCore.systemPath + "/locations/" + name + ".lct");
        }
        catch(Exception e)
        {
            System.out.println("writeFile - " + e.getMessage());
            return;
        }
	}
	static private ArrayList addTiles(int x, int y, int direction, int lenght, HashMap tile)
	{
		int offsetX = 0;
		int offsetY = 0;
		HashMap copyTile = new HashMap<String, Object>();
    	HashMap rect = (HashMap)tile.get("rect");
        int h = ((Long)rect.get("h")).intValue();
        int w = ((Long)rect.get("w")).intValue();
		ArrayList tiles = new ArrayList<Object>();
		for(int i=0; i<lenght; i++)
		{
			if(direction == 2)
			{
				HashMap coordinates = new HashMap<String, Object>();
				coordinates.put("x", (long)x + offsetX);
				coordinates.put("y", (long)y + offsetY);
				tile.put("coordinates", coordinates);
				offsetX += w;
			}
			tiles.add(shallowCopy(tile));
		}
		return tiles;
	}
	static private final Map shallowCopy(final Map source)
	{
        try
        {
			final Map newMap = source.getClass().newInstance();
			newMap.putAll(source);
			return newMap;
        }
        catch(Exception e)
        {
            System.out.println("shallowCopy - " + e.getMessage());
        }
		return null;
	}
	static private ArrayList createTilesList(int wei, int hei, ArrayList allTiles)
	{
		ArrayList tiles = new ArrayList<Object>();
		for(int i=0; i<hei; i++)
		{
			int j=0;
			while(j<wei)
			{
				HashMap tile = findTile(j, i, allTiles);
				tiles.add(tile);
				HashMap rect = (HashMap)tile.get("rect");
				int w = ((Long)rect.get("w")).intValue();
				j+=w;
			}
		}
		return tiles;
	}
	static private HashMap findTile(int x, int y, ArrayList allTiles)
	{
		for(int i=0; i<allTiles.size(); i++)
		{
			HashMap tmp = (HashMap)allTiles.get(i);
			HashMap coordinates = (HashMap)tmp.get("coordinates");
			int tmpX = ((Long)coordinates.get("x")).intValue();
			int tmpY = ((Long)coordinates.get("y")).intValue();
			if(tmpX == x && tmpY == y)
			{
				return tmp;
			}
		}
		HashMap tile = FSCore.getInstance().getEmptyTile();
		HashMap coordinates = new HashMap<String, Object>();
		coordinates.put("x", x);
		coordinates.put("y", y);
		tile.put("coordinates", coordinates);
		return tile;
	}
}