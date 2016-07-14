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
		//
		return packLocation(allTiles, name);
	}
	private ArrayList generateDistrictFloorTilesTest(int x, int y)
	{
		ArrayList floorTiles = new ArrayList<>();
		HashMap floorTile = FSCore.getInstance().getTile("floor_ground_center");
		Random random = new Random();
		int beginX = x;
		int beginY = y;
		int oldX = x;
		int oldY = y;
		floorTiles.addAll(addAreaTiles(beginX, beginY, 1, floorTile));
		while(true)
		{
			int lenght = 1;
			int oX = 7;
			int oY = 8;
			if(random.nextBoolean() && random.nextBoolean())
			{
				break;
			}
			beginX+=oX;
			beginY+=oY;
			floorTiles.addAll(addAreaTiles(beginX, beginY, lenght, floorTile));
			floorTiles.addAll(addBridgeTiles(oldX, oldY, beginX, beginY, floorTiles));
			oldX = beginX;
			oldY = beginY;
		}
		return floorTiles;
	}
	private ArrayList generateDistrictFloorTiles(int x, int y)
	{
		ArrayList floorTiles = new ArrayList<>();
		HashMap floorTile = FSCore.getInstance().getTile("floor_ground_center");
		Random random = new Random();
		int beginX = x;
		int beginY = y;
		int oldX = x;
		int oldY = y;
		floorTiles.addAll(addAreaTiles(beginX, beginY, 1, floorTile));
		//floorTiles.addAll(addAreaTiles(beginX, beginY, random.nextInt(5)+1, floorTile));
		while(true)
		{
			//int lenght = random.nextInt(5)+1;
			//lenght *= random.nextInt(1)+1;
			int lenght = 1;
			int oX = random.nextInt(10)-5;
			int oY = random.nextInt(10)-5;
			oX *= random.nextInt(2)+1;
			oY *= random.nextInt(2)+1;
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
			beginX+=oX;
			beginY+=oY;
			floorTiles.addAll(addAreaTiles(beginX, beginY, lenght, floorTile));
			floorTiles.addAll(addBridgeTiles(oldX, oldY, beginX, beginY, floorTiles));
			oldX = beginX;
			oldY = beginY;
			//beginX/=2;
			//beginY/=2;
		}
		return floorTiles;
	}
	private ArrayList addAreaTiles(long x, long y, int lenght, HashMap tile)
	{
		long offsetX = 0;
		long offsetY = 0;
		HashMap copyTile = new HashMap<String, Object>();
    	HashMap rect = (HashMap)tile.get("rect");
		int h = ((Number)rect.get("h")).intValue();
		int w = ((Number)rect.get("w")).intValue();
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
	private ArrayList addBridgeTiles(long beginX, long beginY, long endX, long endY, ArrayList allTiles)
	{
		//System.out.println("addBridgeTiles");
		long rX = Math.abs(beginX-endX);
		long rY = Math.abs(beginY-endY);
		long offsetX = 0;
		long offsetY = 0;
		HashMap bridgeTile = FSCore.getInstance().getTile("floor_asphalt_center");
		ArrayList tiles = new ArrayList<>();
		if(rX >= rY)
		{
			int dX = 1;
			int dY = 1;
			boolean side = true;
			if(beginY<endY)
			{
				dX = 1;
				dY = 1;
			}
			else
			{
				dX = 1;
				dY = -1;
			}
			tiles.addAll(addBridges(side, beginX, beginY, rX, rY, dX, dY, bridgeTile, allTiles));
			tiles.addAll(addBridges(side, endX, endY, rX, rY, -dX, -dY, bridgeTile, allTiles));
			HashMap coordinates = new HashMap<>();
			coordinates.put("x", beginX + rX/2);
			if(rY % 2 == 0) {
				coordinates.put("y", beginY + (rY/2)*dY);
			} else {
				coordinates.put("y", beginY + (rY/2+1)*dY);
			}
			bridgeTile.put("coordinates", coordinates);
			tiles.add(shallowCopy(bridgeTile));
		}
		else
		{
			int dX = 1;
			int dY = 1;
			boolean side = false;
			if(beginX<endX)
			{
				dX = 1;
				dY = 1;
			}
			else
			{
				dX = -1;
				dY = 1;
			}
			tiles.addAll(addBridges(side, beginX, beginY, rX, rY, dX, dY, bridgeTile, allTiles));
			tiles.addAll(addBridges(side, endX, endY, rX, rY, -dX, -dY, bridgeTile, allTiles));
			HashMap coordinates = new HashMap<>();
			coordinates.put("x", beginX + (rX/2)*dX);
			if(rY % 2 == 0) {
				coordinates.put("y", beginY + (rY/2)*dY);
			} else {
				coordinates.put("y", beginY + (rY/2+1)*dY);
			}
			bridgeTile.put("coordinates", coordinates);
			tiles.add(shallowCopy(bridgeTile));
		}
		return tiles;
	}
	private ArrayList addBridges(boolean side, long beginX, long beginY, long rX, long rY, int dX, int dY, HashMap bridgeTile, ArrayList allTiles)
	{
		System.out.println("addBridges");
		//System.out.println("addBridges beginX-"+beginX+" beginY-"+beginY);
		long offsetX = 0;
		long offsetY = 0;
		ArrayList tiles = new ArrayList<>();
		if(side)
		{
			offsetX += dX;
		}
		else
		{
			offsetY += dY;
		}
		while(Math.abs(offsetX) < rY/2+1 && Math.abs(offsetY) < rX/2+1)
		{
			side = !side;
			HashMap coordinates = new HashMap<>();
			coordinates.put("x", beginX + offsetX);
			coordinates.put("y", beginY + offsetY);
			bridgeTile.put("coordinates", coordinates);
			tiles.add(shallowCopy(bridgeTile));
			if(side)
			{
				offsetX += dX;
			}
			else
			{
				offsetY += dY;
			}
		}
		if(side)
		{
			while(Math.abs(offsetX) < rX/2+1)
			{
				HashMap coordinates = new HashMap<>();
				coordinates.put("x", beginX + offsetX);
				coordinates.put("y", beginY + offsetY);
				bridgeTile.put("coordinates", coordinates);
				tiles.add(shallowCopy(bridgeTile));
				offsetX += dX;
			}
		}
		else
		{
			while(Math.abs(offsetY) < rY/2+1)
			{
				HashMap coordinates = new HashMap<>();
				coordinates.put("x", beginX + offsetX);
				coordinates.put("y", beginY + offsetY);
				bridgeTile.put("coordinates", coordinates);
				tiles.add(shallowCopy(bridgeTile));
				offsetY += dY;
			}
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