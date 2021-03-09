package renameFiles.metier.types;

import renameFiles.ihm.dialogs.DialogAvancement;

public interface ListeInterface
{
    String  traitement(DialogAvancement dialog);
    boolean add(Object o);
}
