using System;
using System.Diagnostics;

// compile: csc renameFile.cs
// execute: renameFile [SERIES,AUTRES,ALEANAME]
public class renameFile
{
    public static void Main( string[] arg )
    {
        // syntaxe de la commande a executer
        ProcessStartInfo commande = new ProcessStartInfo("java","-jar \"C:/Path/To/Jar\" ./ " + (arg.Length > 0 ? arg[0] : ""));

        // permet de cacher le terminal de commande
        commande.WindowStyle = ProcessWindowStyle.Hidden;

        // execute la commande definit
        Process proc = Process.Start(commande);
    }
}
