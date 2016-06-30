package stan.free.space.core;

import java.util.HashMap;

import stan.free.space.helpers.FileHelper;
import stan.free.space.helpers.json.JSONParser;

public class FSCore
{
	static public String systemPath = "C:/Program Files/StanleyProjects/FreeSpace";
	
    static private FSCore instance;
    static public FSCore getInstance()
    {
        if(instance == null)
        {
            instance = new FSCore();
        }
        return instance;
    }
	
    private FSCore()
    {
    }
	
	public String getImagePath(String name)
	{
        return FSCore.systemPath + "/images/" + name;
	}
	public HashMap getLocation(String name)
	{
		String result = FileHelper.readFile(FSCore.systemPath + "/locations/" + name + ".lct");
        JSONParser parser = new JSONParser();
        try
        {
            return (HashMap)parser.parse(result);
        }
        catch(Exception e)
        {
            System.out.println("JSONParser - " + e.getMessage());
        }
        return null;
	}
	public HashMap getTile(String name)
	{
		String result = FileHelper.readFile(FSCore.systemPath + "/tiles/" + name + ".tile");
        JSONParser parser = new JSONParser();
        try
        {
            return (HashMap)parser.parse(result);
        }
        catch(Exception e)
        {
            System.out.println("JSONParser - " + e.getMessage());
        }
        return null;
	}
	public HashMap getEmptyTile()
	{
		HashMap emptyTile = new HashMap<String, Object>();
		HashMap rect = new HashMap<String, Object>();
		rect.put("w", 1L);
		rect.put("h", 1L);
		emptyTile.put("rect", rect);
		emptyTile.put("type", -1);
		return emptyTile;
	}
}