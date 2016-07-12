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
			long tmpX = (Long)coordinates.get("x");
			long tmpY = (Long)coordinates.get("y");
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
			long tmpX = (Long)coordinates.get("x");
			long tmpY = (Long)coordinates.get("y");
			coordinates.put("x", tmpX + offsetX);
			coordinates.put("y", tmpY + offsetY);
			//((HashMap)tiles.get(i)).put("coordinates", coordinates);
		}
	}
	protected HashMap getRectTiles(ArrayList tiles)
	{
		long w = 0;
		long h = 0;
		HashMap rect = new HashMap<>();
		for(int i=0; i<tiles.size(); i++)
		{
			HashMap tmp = (HashMap)tiles.get(i);
			HashMap coordinates = (HashMap)tmp.get("coordinates");
			int tmpX = ((Number)coordinates.get("x")).intValue();
			int tmpY = ((Number)coordinates.get("y")).intValue();
			if(tmpX+1 > w)
			{
				//System.out.println("tmpX - " + tmpX);
				w = tmpX+1;
			}
			if(tmpY+1 > h)
			{
				//System.out.println("tmpY - " + tmpY);
				h = tmpY+1;
			}
		}
		rect.put("w", w);
		rect.put("h", h);
		System.out.println(rect);
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