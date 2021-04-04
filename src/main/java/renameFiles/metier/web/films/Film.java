package renameFiles.metier.web.films;

import renameFiles.metier.web.WebElement;

import java.util.Date;
import java.util.Locale;

public class Film implements WebElement
{
    private int id;
    private String title;
    private String original_title;
    private String overview;
    private Locale original_language;
    private Date release_date;

    @Override
    public String toString()
    {
        return "Film{" + "Title='" + title + '\'' + '}';
    }

    @Override
    public String getName()
    {
        return this.title;
    }

    @Override
    public String getName(Locale locale)
    {
        return locale.getLanguage().equals(this.original_language.getLanguage()) ? this.original_title : this.getName();
    }

    @Override
    public Date getReleaseDate()
    {
        return this.release_date;
    }
}
