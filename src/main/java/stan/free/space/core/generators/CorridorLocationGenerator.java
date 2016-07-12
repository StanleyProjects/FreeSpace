package stan.free.space.core.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import stan.free.space.core.FSCore;

public class CorridorLocationGenerator
	extends LocationGenerator
{
	public HashMap generate()
	{
		String name = "corridor";
		int beginX = 0;
		int beginY = 0;
		HashMap location = new HashMap<>();
		HashMap begin = new HashMap<>();
		begin.put("x", beginX);
		begin.put("y", beginY);
		location.put("begin", begin);
		ArrayList allTiles = new ArrayList<>();
		//
		ArrayList floorTiles = generateCorridorFloorTiles(beginX, beginY, 2);
		correctingTilesCoordinates(floorTiles);
		allTiles.addAll(floorTiles);
		//
		location.put("rect", getRectTiles(allTiles));
		location.put("tiles", allTiles);
		location.put("name", name);
		return location;
	}
	static private ArrayList generateCorridorFloorTiles(int x, int y, int direction)
	{
		ArrayList floorTiles = new ArrayList<>();
		HashMap floorTile = FSCore.getInstance().getTile("floor_ground_center");
		Random random = new Random();
		int beginX = x;
		int beginY = y;
		floorTiles.addAll(addLineTiles(beginX, beginY, direction, random.nextInt(5)+1, floorTile));
		while(true)
		{
			int lenght = random.nextInt(5)+1;
			int dir = random.nextInt(120);
			if(dir < 10)
			{
				break;
			}
			dir /= 25;
			floorTiles.addAll(addLineTiles(beginX, beginY, dir, lenght, floorTile));
			if(dir == 1)
			{
				beginY -= lenght;
			}
			else if(dir == 2)
			{
				beginX += lenght;
			}
			else if(dir == 3)
			{
				beginY += lenght;
			}
			else if(dir == 4)
			{
				beginX -= lenght;
			}
		}
		return floorTiles;
	}
	private ArrayList addLineTiles(long x, long y, int direction, int lenght, HashMap tile)
	{
		long offsetX = 0;
		long offsetY = 0;
		HashMap copyTile = new HashMap<String, Object>();
    	HashMap rect = (HashMap)tile.get("rect");
        int h = ((Long)rect.get("h")).intValue();
        int w = ((Long)rect.get("w")).intValue();
		ArrayList tiles = new ArrayList<Object>();
		for(int i=0; i<lenght; i++)
		{
			HashMap coordinates = new HashMap<String, Object>();
			coordinates.put("x", x + offsetX);
			coordinates.put("y", y + offsetY);
			tile.put("coordinates", coordinates);
			if(direction == 1)
			{
				offsetY -= h;
			}
			else if(direction == 2)
			{
				offsetX += w;
			}
			else if(direction == 3)
			{
				offsetY += h;
			}
			else if(direction == 4)
			{
				offsetX -= w;
			}
			tiles.add(shallowCopy(tile));
		}
		return tiles;
	}
}