import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import renameFiles.metier.Metier;
import renameFiles.metier.types.series.Serie;
import renameFiles.metier.web.WebInfoHelper;

import java.util.Date;

public class TestMetier
{
    private Metier     metier;

    @Before
    public void setUp()
    {

        this.metier = new Metier(null);
        
    }

    @After
    public void tearDown()
    {

        this.metier = null;
    }

    @Test
    public void testAcceptedExtensions()
    {
        String[] extensions = {null, "", }; // cas faux car "null"

        for ( String s : extensions )
            Assert.assertFalse(this.metier.setAcceptedExtensions(s));

        extensions = new String[]{",", ",,,", ", , , , , "}; // cas faux car vide

        for ( String s : extensions )
            Assert.assertFalse(this.metier.setAcceptedExtensions(s));
    }

    @Test
    public void testWebHelper()
    {
        Serie s = new Serie("Dr. Stone");

        renameFiles.metier.web.series.Serie serie = WebInfoHelper.getWebSerie(s);

        Assert.assertNotNull(serie);
        Assert.assertEquals("Dr. Stone", serie.getName());
        Assert.assertEquals("Animation", serie.getType());
        Assert.assertEquals(new Date(1562277600000L), serie.getPremiered()); // 2019-07-05
    }
}
