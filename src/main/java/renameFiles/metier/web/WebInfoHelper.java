package renameFiles.metier.web;

import com.google.gson.*;
import renameFiles.metier.web.films.Film;
import renameFiles.metier.web.series.Episode;
import renameFiles.metier.web.series.Serie;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class WebInfoHelper
{
    //todo API saisie par l'utilisateur
    private static final String FILM_API = "118d40394571ac6c8474eb5dd4ac10f0";

    /*
        %% = series ID
        %s = season number
        %e = episode number
     */
    private static final String BASE_INFO_SERIE_URL = "http://api.tvmaze.com/shows/%%";

    private static final String BASE_SEARCH_SERIE_URL = "http://api.tvmaze.com/singlesearch/shows?q=";
    private static final String EPISODE_URL    = BASE_INFO_SERIE_URL + "/episodes";
    private static final String EPISODE_SEARCH = BASE_INFO_SERIE_URL + "/episodebynumber?season=%s&number=%e";
    private static final String OTHER_NAME_SERIE_URL = BASE_INFO_SERIE_URL + "/akas";

    private static  final String BASE_SEARCH_FILM_URL = "https://api.themoviedb.org/3/search/movie?api_key=" + FILM_API + "&query=";

    private static final Gson GSON = getGsonDateSafe();

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public static Serie getWebSerie(renameFiles.metier.types.series.Serie serie)
    {
        String urlComplet    = BASE_SEARCH_SERIE_URL + serie.getSerieName().replaceAll(" ", "%20");
        String donnees       = getJsonResponseFromURL(urlComplet);
        JsonElement webSerie = JsonParser.parseString(donnees);

        Serie result = GSON.fromJson(webSerie, Serie.class);

        if( result != null && !setOtherLanguages(result) )
            System.out.println("Languages non ajouter");

        return result;
    }

    public static boolean setEpisodesList( Serie series )
    {
        if( series == null ) return false;

        series.clearEpisodes();

        String url = EPISODE_URL.replaceAll("%%", String.valueOf(series.getId()));

        JsonElement elements = JsonParser.parseString(getJsonResponseFromURL(url));

        try
        {
            JsonArray array = elements.getAsJsonArray();

            for (int i = 0; i < array.size(); i++)
                if (!series.addEpisode(GSON.fromJson(array.get(i), Episode.class)))
                {
                    series.clearEpisodes();
                    return false;
                }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean setOtherLanguages(Serie serie )
    {
        if( serie == null ) return false;

        String url = OTHER_NAME_SERIE_URL.replaceAll("%%", String.valueOf(serie.getId()));

        JsonElement element = JsonParser.parseString(getJsonResponseFromURL(url));

        try
        {
            JsonArray array = element.getAsJsonArray();

            for (int i = 0; i < array.size(); i++)
            {
                JsonObject elt = array.get(i).getAsJsonObject();

                String name = elt.get("name").getAsString();

                JsonElement langELT = elt.get("country");
                JsonObject language = langELT.isJsonNull() ? null : langELT.getAsJsonObject();

                Locale locale = Locale.forLanguageTag(language == null ? "" : language.get("code").getAsString());

                serie.addOtherName(locale, name);
            }

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static Film getWebFilm(renameFiles.metier.types.series.Serie film)
    {
        String urlComplet    = BASE_SEARCH_FILM_URL + film.getSerieName().replaceAll(" ", "%20");
        String donnees       = getJsonResponseFromURL(urlComplet);
        JsonObject webSerie  = JsonParser.parseString(donnees).getAsJsonObject();

        Film result = null;

        if( webSerie.get("total_results").getAsInt() > 0 )
        {
            result = GSON.fromJson(webSerie.get("results").getAsJsonArray().get(0), Film.class); // 1 resultat = plus pertinent
        }

        return result;
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

    private static Gson getGsonDateSafe()
    {
        return new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>()
        {
            final DateFormat serieDF = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
            {
                try
                {
                    return serieDF.parse(json.getAsString());
                }
                catch (ParseException e)
                {
                    return null;
                }
            }
        }).create();
    }
}
