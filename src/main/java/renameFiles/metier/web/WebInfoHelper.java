package renameFiles.metier.web;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebInfoHelper
{
    /*
        %% = series ID
        %s = season number
        %e = episode number
     */
    private static final String BASE_SEARCH_URL = "http://api.tvmaze.com/singlesearch/shows?q=";
    private static final String EPISODE_URL = "http://api.tvmaze.com/shows/%%/episodes";
    private static final String EPISODE_SEARCH = "http://api.tvmaze.com/shows/%%/episodebynumber?season=%s&number=%e";

    private static final Gson GSON = new Gson();

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public static void selectInfosTests(renameFiles.metier.types.series.Serie serie)
    {
        String urlComplet    = BASE_SEARCH_URL + serie.getSerieName().replaceAll(" ", "%20");
        String donnees       = getJsonResponseFromURL(urlComplet);
        JsonElement webSerie = JsonParser.parseString(donnees);

        Serie result = GSON.fromJson(webSerie, Serie.class);

        if( result != null )
            System.out.println(result.getName() + ": " + result.getId());
        else
            System.out.println(urlComplet + " => serie inconnue\n" + donnees);
    }

    public static String getJsonResponseFromURL(String urlString)
    {
        try
        {
            URL url = URI.create(urlString).toURL();

            URLConnection connect = url.openConnection();

            connect.connect();

            InputStream stream = connect.getInputStream();

            return new String(readAllBytes(stream));
        }
        catch (IOException e)
        {
            return "";
        }
    }

    private static byte[] readAllBytes(InputStream stream) throws IOException
    {
        int len = Integer.MAX_VALUE;

        List<byte[]> bufs = null;
        byte[] result = null;
        int total = 0;
        int remaining = len;
        int n;
        do
        {
            byte[] buf = new byte[Math.min(remaining, DEFAULT_BUFFER_SIZE)];
            int nread = 0;

            // read to EOF which may read more or less than buffer size
            while ((n = stream.read(buf, nread, Math.min(buf.length - nread, remaining))) > 0)
            {
                nread += n;
                remaining -= n;
            }

            if (nread > 0)
            {
                if (Integer.MAX_VALUE - 8 - total < nread)
                {
                    throw new OutOfMemoryError("Required array size too large");
                }
                total += nread;
                if (result == null)
                {
                    result = buf;
                }
                else
                {
                    if (bufs == null)
                    {
                        bufs = new ArrayList<>();
                        bufs.add(result);
                    }
                    bufs.add(buf);
                }
            }
            // if the last call to read returned -1 or the number of bytes
            // requested have been read then break
        } while (n >= 0 && remaining > 0);

        if (bufs == null)
        {
            if (result == null)
            {
                return new byte[0];
            }
            return result.length == total ? result : Arrays.copyOf(result, total);
        }

        result = new byte[total];
        int offset = 0;
        remaining = total;
        for (byte[] b : bufs)
        {
            int count = Math.min(b.length, remaining);
            System.arraycopy(b, 0, result, offset, count);
            offset += count;
            remaining -= count;
        }

        return result;
    }
}
