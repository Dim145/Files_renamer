package renameFiles.metier.web;

import java.util.Date;
import java.util.Locale;

public interface WebElement
{
    String getName();
    String getName(Locale locale);
    Date getReleaseDate();
}
