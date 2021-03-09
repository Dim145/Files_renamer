package renameFiles.metier.types;

import renameFiles.ihm.dialogs.DialogAvancement;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class BaseFileListe implements ListeInterface
{
    private final ArrayList<BaseFile> list;
    private final boolean             blockIfNotMathPatern;

    public BaseFileListe(boolean blockIfNotMathPatern )
    {
        this.list                 = new ArrayList<>();
        this.blockIfNotMathPatern = blockIfNotMathPatern;
    }

    public BaseFileListe(Collection<BaseFile> baseFiles, boolean blockIfNotMathPatern )
    {
        this(blockIfNotMathPatern);

        this.addBaseFiles(baseFiles);
    }

    private boolean addBaseFiles(Collection<BaseFile> baseFiles)
    {
        for (BaseFile baseFile : baseFiles)
            if( !this.add(baseFile))
                return false;

        return true;
    }

    @Override
    public String traitement(DialogAvancement dialog)
    {
        StringBuilder sRet = new StringBuilder();

        for (BaseFile baseFile : this.list)
        {
            if( !baseFile.replaceFullFormatedName(blockIfNotMathPatern) )
            {
                sRet.append("<font color=\"red\">file: ").append(baseFile.getFullname()).append(
                        " ne contient pas le meme nombres de %% que le patern.</font>\n");
                continue;
            }

            if( baseFile.getFile().renameTo(new File(baseFile.getFile().getParent() + "/" + baseFile.toString())) )
                sRet.append("file: ").append(baseFile.getFullname()).append(baseFile.getExtension()).append(
                        " -> <font color=\"rgb(0, 255, 255)\">").append(baseFile.toString()).append("</font>\n");
            else
                sRet.append("<font color=\"red\">file: ").append(baseFile.getFullname()).append(
                        " not renamed</font>\n");
        }

        return sRet.toString();
    }

    @Override
    public boolean add(Object o)
    {
        if( !(o.getClass() == BaseFile.class) ) return false;

        return this.list.add((BaseFile) o);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseFileListe that = (BaseFileListe) o;
        return blockIfNotMathPatern == that.blockIfNotMathPatern && list.equals(that.list);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(list, blockIfNotMathPatern);
    }
}
