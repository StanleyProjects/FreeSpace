package stan.free.space.core.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import stan.free.space.core.FSCore;

public abstract class LocationGenerator
{
	static protected HashMap findTile(int x, int y, ArrayList allTiles)
	{
		for(int i=0; i<allTiles.size(); i++)
		{
			HashMap tmp = (HashMap)allTiles.get(i);
			HashMap coordinates = (HashMap)tmp.get("coordinates");
			int tmpX = ((Number)coordinates.get("x")).intValue();
			int tmpY = ((Number)coordinates.get("y")).intValue();
			if(tmpX == x && tmpY == y)
			{
				return tmp;
			}
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
	
    static private ILocationGenerator districtInstance;
    static public ILocationGenerator getDistrict()
    {
        if(districtInstance == null)
        {
            districtInstance = new DistrictLocationGenerator();
        }
        return districtInstance;
    }
}