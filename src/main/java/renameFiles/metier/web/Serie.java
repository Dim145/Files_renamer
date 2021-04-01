package renameFiles.metier.web;

import java.net.URI;
import java.util.Date;

public class Serie
{
    private int id;
    private URI url;
    private String name;
    private String type;
    private Date premiered;

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

    public String getType()
    {
        return type;
    }

    public Date getPremiered()
    {
        return premiered;
    }

    @Override
    public String toString()
    {
        return "Serie{" + "id=" + id + ", url='" + url + '\'' + ", name='" + name + '\'' + ", type='" + type + '\'' + ", premiered='" + premiered + '\'' + '}';
    }
}
