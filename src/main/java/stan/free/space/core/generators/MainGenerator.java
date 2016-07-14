package stan.free.space.core.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class MainGenerator
	implements ILocationGenerator
{
	protected void correctingTilesCoordinates(ArrayList tiles)
	{
		for(int i=0; i<tiles.size(); i++)
		{
			HashMap tmp = (HashMap)tiles.get(i);
			HashMap coordinates = (HashMap)tmp.get("coordinates");
			int tmpX = ((Number)coordinates.get("x")).intValue();
			int tmpY = ((Number)coordinates.get("y")).intValue();
			if(tmpX < 0 || tmpY < 0)
			{
				long offsetX = 0;
				long offsetY = 0;
				if(tmpX < 0)
				{
					offsetX -= tmpX;
					//System.out.println("offsetX - " + offsetX + " tmpX - " + tmpX);
				}
				if(tmpY < 0)
				{
					offsetY -= tmpY;
					//System.out.println("offsetY - " + offsetY + " tmpY - " + tmpY);
				}
				correctingTilesCoordinates(tiles, offsetX, offsetY);
			}
		}
	}
	protected void correctingTilesCoordinates(ArrayList tiles, long offsetX, long offsetY)
	{
		for(int i=0; i<tiles.size(); i++)
		{
			HashMap tmp = (HashMap)tiles.get(i);
			HashMap coordinates = (HashMap)tmp.get("coordinates");
			int tmpX = ((Number)coordinates.get("x")).intValue();
			int tmpY = ((Number)coordinates.get("y")).intValue();
			coordinates.put("x", tmpX + offsetX);
			coordinates.put("y", tmpY + offsetY);
			//((HashMap)tiles.get(i)).put("coordinates", coordinates);
		}
	}
	protected HashMap getRectTiles(ArrayList tiles)
	{
		int minX = 0;
		int maxX = 0;
		int minY = 0;
		int maxY = 0;
		HashMap rect = new HashMap<>();
		for(int i=0; i<tiles.size(); i++)
		{
			HashMap tmp = (HashMap)tiles.get(i);
			HashMap coordinates = (HashMap)tmp.get("coordinates");
			int tmpX = ((Number)coordinates.get("x")).intValue();
			int tmpY = ((Number)coordinates.get("y")).intValue();
			if(tmpX>maxX)
			{
				maxX = tmpX;
			}
			else if(tmpX<minX)
			{
				minX = tmpX;
			}
			if(tmpY > maxY)
			{
				maxY = tmpY;
			}
			else if(tmpY < minY)
			{
				minY = tmpY;
			}
		}
		rect.put("w", Math.abs(maxX-minX));
		rect.put("h", Math.abs(maxY-minY));
		return rect;
	}
	protected HashMap getRectTilesOld(ArrayList tiles)
	{
		int w = 0;
		int h = 0;
		HashMap rect = new HashMap<>();
		for(int i=0; i<tiles.size(); i++)
		{
			HashMap tmp = (HashMap)tiles.get(i);
			HashMap coordinates = (HashMap)tmp.get("coordinates");
			int tmpX = ((Number)coordinates.get("x")).intValue();
			int tmpY = ((Number)coordinates.get("y")).intValue();
			if(tmpX+1 > w)
			{
				w = tmpX+1;
			}
			if(tmpY+1 > h)
			{
				h = tmpY+1;
			}
		}
		rect.put("w", w);
		rect.put("h", h);
		return rect;
	}
	protected final Map shallowCopy(final Map source)
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
	protected HashMap packLocation(ArrayList allTiles, String name)
	{
		HashMap location = new HashMap<>();
		HashMap rect = getRectTiles(allTiles);
		location.put("rect", rect);
        int h = ((Number)rect.get("h")).intValue();
        int w = ((Number)rect.get("w")).intValue();
		location.put("tiles", LocationGenerator.createTilesList(w, h, allTiles));
		location.put("name", name);
		return location;
	}
}