import junit.framework.TestCase;
import renameFiles.metier.Metier;

public class TestMetier extends TestCase
{
    private Metier     metier;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        this.metier = new Metier(null);
        
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();

        this.metier = null;
    }

    public void testAcceptedExtensions() throws Exception
    {
        String[] extensions = {null, "", }; // cas faux car "null"

        for ( String s : extensions )
            assertFalse(this.metier.setAcceptedExtensions(s));

        extensions = new String[]{",", ",,,", ", , , , , "}; // cas faux car vide

        for ( String s : extensions )
            assertFalse(this.metier.setAcceptedExtensions(s));
    }
}
