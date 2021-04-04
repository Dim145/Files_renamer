package renameFiles.metier.web.series;

import java.net.URI;
import java.util.Date;

public class Episode
{
    private int id;
    private URI url;
    private String name;
    private int season;
    private int number;
    private String type;
    private Date airdate;


    public int getId()
    {
        return id;
    }

    public URI getUrl()
    {
        return url;
    }

    public String getName()
    {
        return name;
    }

    public int getSeason()
    {
        return season;
    }

    public int getNumber()
    {
        return number;
    }

    public String getType()
    {
        return type;
    }

    public Date getAirdate()
    {
        return airdate;
    }
}
