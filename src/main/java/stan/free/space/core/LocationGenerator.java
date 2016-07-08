package stan.free.space.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LocationGenerator
{
	static public HashMap generateDistrict()
	{
		String name = "district";
		int beginX = 0;
		int beginY = 0;
		HashMap location = new HashMap<>();
		HashMap begin = new HashMap<>();
		begin.put("x", beginX);
		begin.put("y", beginY);
		location.put("begin", begin);
		ArrayList allTiles = new ArrayList<>();
		//
		ArrayList floorTiles = generateDistrictFloorTiles(beginX, beginY);
		correctingTilesCoordinates(floorTiles);
		allTiles.addAll(floorTiles);
		//
		location.put("rect", getRectTiles(allTiles));
		location.put("tiles", allTiles);
		location.put("name", name);
		return location;
	}
	static private ArrayList generateDistrictFloorTiles(int x, int y)
	{
		ArrayList floorTiles = new ArrayList<>();
		HashMap floorTile = FSCore.getInstance().getTile("floor_ground_center");
		Random random = new Random();
		int beginX = x;
		int beginY = y;
		floorTiles.addAll(addAreaTiles(beginX, beginY, random.nextInt(5)+1, floorTile));
		while(true)
		{
			int lenght = random.nextInt(5)+1;
			lenght *= random.nextInt(1)+1;
			int oX = random.nextInt(10)-5;
			int oY = random.nextInt(10)-5;
			oX *= random.nextInt(1)+1;
			oY *= random.nextInt(1)+1;
			if(oX == 0 && 
				(random.nextBoolean() || random.nextBoolean()))
			{
				oX = random.nextInt(10)-5;
				oX *= random.nextInt(1)+1;
			}
			if(oX == 0)
			{
				break;
			}
			floorTiles.addAll(addAreaTiles(beginX + oX, beginY + oY, lenght, floorTile));
			beginX = (beginX+oX)/2;
			beginY = (beginY+oY)/2;
		}
		return floorTiles;
	}
	static private ArrayList addAreaTiles(long x, long y, int lenght, HashMap tile)
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
			offsetX = 0;
			for(int j=0; j<lenght; j++)
			{
				HashMap coordinates = new HashMap<String, Object>();
				coordinates.put("x", x + offsetX);
				coordinates.put("y", y + offsetY);
				tile.put("coordinates", coordinates);
				tiles.add(shallowCopy(tile));
				offsetX += w;
			}
			offsetY += h;
		}
		return tiles;
	}
	
	static public HashMap generateCorridor()
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
	static private void correctingTilesCoordinates(ArrayList tiles)
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
	static private void correctingTilesCoordinates(ArrayList tiles, long offsetX, long offsetY)
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
	static private HashMap getRectTiles(ArrayList tiles)
	{
		long w = 0;
		long h = 0;
		HashMap rect = new HashMap<String, Object>();
		for(int i=0; i<tiles.size(); i++)
		{
			HashMap tmp = (HashMap)tiles.get(i);
			HashMap coordinates = (HashMap)tmp.get("coordinates");
			int tmpX = ((Long)coordinates.get("x")).intValue();
			int tmpY = ((Long)coordinates.get("y")).intValue();
			if(tmpX+1 > w)
			{
				System.out.println("tmpX - " + tmpX);
				w = tmpX+1;
			}
			if(tmpY+1 > h)
			{
				System.out.println("tmpY - " + tmpY);
				h = tmpY+1;
			}
		}
		rect.put("w", w);
		rect.put("h", h);
		System.out.println(rect);
		return rect;
	}
	static private ArrayList addLineTiles(long x, long y, int direction, int lenght, HashMap tile)
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
	static public ArrayList createTilesList(int wei, int hei, ArrayList allTiles)
	{
		ArrayList tiles = new ArrayList<Object>();
		for(int i=0; i<hei; i++)
		{
			int j=0;
			while(j<wei)
			{
				HashMap tile = findTile(j, i, allTiles);
				if(tile == null)
				{
					tile = FSCore.getInstance().getEmptyTile();
					HashMap coordinates = new HashMap<String, Object>();
					coordinates.put("x", j);
					coordinates.put("y", i);
					tile.put("coordinates", coordinates);
				}
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
		return null;
	}

	static public int correctW(int wei, int hei, ArrayList allTiles)
	{
		int correctW = 0;
		int offset = 0;
		int pos;
		while(true)
		{
			if(wei-offset == 2)
			{
				break;
			}
			pos = wei*(offset+1)-1;
			offset++;
			if(checkDiagonal(pos, wei, offset, allTiles))
			{
				break;
			}
			correctW++;
		}
		return correctW;
	}
	static public int correctH(int wei, int hei, ArrayList allTiles)
	{
		int correctH = 0;
		int offset = 0;
		int pos;
		while(true)
		{
			if(hei-offset == 2)
			{
				break;
			}
			pos = wei*(hei-offset-1);
            //System.out.println("checkWidth pos - " + pos);
			offset++;
			if(checkDiagonal(pos, wei, offset, allTiles))
			{
				break;
			}
			correctH++;
		}
		return correctH;
	}
	static private boolean checkDiagonal(int pos, int wei, int offset, ArrayList allTiles)
	{
		for(int i=0; i<offset; i++)
		{
			HashMap tmp = (HashMap)allTiles.get(pos);
	        long type = (Long)tmp.get("type");
			if(type == -1)
			{
				pos += wei + 1;
				continue;
			}
			else
			{
				return true;
			}
		}
		return false;
	}
}