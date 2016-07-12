package stan.free.space.core.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import stan.free.space.core.FSCore;

public class DistrictLocationGenerator
	extends MainGenerator
{
	protected DistrictLocationGenerator()
	{
		
	}
	
	public HashMap generate()
	{
		String name = "district";
		int beginX = 0;
		int beginY = 0;
		HashMap location = new HashMap<>();
		ArrayList allTiles = new ArrayList<>();
		//
		ArrayList floorTiles = generateDistrictFloorTiles(beginX, beginY);
		correctingTilesCoordinates(floorTiles);
		allTiles.addAll(floorTiles);
		HashMap rect = getRectTiles(allTiles);
		location.put("rect", rect);
        int h = ((Number)rect.get("h")).intValue();
        int w = ((Number)rect.get("w")).intValue();
		allTiles = generateBridges(w, h, allTiles);
		//
		return packLocation(allTiles, name);
	}
	private ArrayList generateDistrictFloorTiles(int x, int y)
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
	private ArrayList addAreaTiles(long x, long y, int lenght, HashMap tile)
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
	
	private ArrayList generateBridges(int wei, int hei, ArrayList allTiles)
	{
		System.out.println("generateBridges - " + wei + ":" + hei);
		HashMap bridgeTile = FSCore.getInstance().getTile("floor_asphalt_center");
		boolean search = true;
		while(search)
		{
			search = false;
			for(int i=0; i<hei; i++)
			{
				//System.out.println("generateBridges - i " + i);
				int j=0;
				while(j<wei)
				{
					//System.out.println("generateBridges - j " + j);
					HashMap tile = LocationGenerator.findTile(j, i, allTiles);
					if(tile != null)
					{
						HashMap rect = (HashMap)tile.get("rect");
						if(j < wei-1 && i < hei-1 && j>0 && i>0)
						{
							HashMap bridge = searchHoles(j, i, bridgeTile, allTiles);
							if(bridge != null)
							{
								System.out.println("hole find - " + j + ":" + i);
								System.out.println("bridge - " + bridge);
								allTiles.add(shallowCopy(bridge));
								search = true;
							}
						}
						int w = ((Number)rect.get("w")).intValue();
						j+=w;
					}
					else
					{
						j++;
					}
				}
			}
		}
		return allTiles;
	}
	private HashMap searchHoles(int x, int y, HashMap bridgeTile, ArrayList allTiles)
	{
		//System.out.println("searchHoles - " + x + ":" + y);
		int dirs[] = new int[4];
		dirs[0] = 0;
		dirs[1] = 0;
		dirs[2] = 0;
		dirs[3] = 0;
		int dir = 1;
		int c = 0;
		while(c < 4)
		{
			int tmpX = x;
			int tmpY = y;
			if(dir == 1)
			{
				tmpY--;
			}
			else if(dir == 2)
			{
				tmpX++;
			}
			else if(dir == 3)
			{
				tmpY++;
			}
			else if(dir == 4)
			{
				tmpX--;
			}
			HashMap tile = LocationGenerator.findTile(tmpX, tmpY, allTiles);
			if(tile == null)
			{
				dirs[c] = dir;
			}
			c++;
			dir++;
		}
		for(int i=0; i<4; i++)
		{
			//System.out.println("dirs[" + i + "] = " + dirs[i]);
			if(dirs[i] == 0)
			{
				return null;
			}
		}
		Random random = new Random();
		dir = 0;
		while(dir == 0)
		{
			dir = dirs[random.nextInt(3)];
		}
		int tmpX = x;
		int tmpY = y;
		if(dir == 1)
		{
			tmpY--;
		}
		else if(dir == 2)
		{
			tmpX++;
		}
		else if(dir == 3)
		{
			tmpY++;
		}
		else if(dir == 4)
		{
			tmpX--;
		}
		HashMap coordinates = new HashMap<String, Object>();
		coordinates.put("x", tmpX);
		coordinates.put("y", tmpY);
		bridgeTile.put("coordinates", coordinates);
		System.out.println("coordinates = " + coordinates);
		return bridgeTile;
	}
}