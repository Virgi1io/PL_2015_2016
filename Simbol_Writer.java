package Generadores_de_Archivos;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase para escribir los archivos TokenConstants y SymbolConstans.java.
 * 
 * @author Juan A. Contreras Fernández
 */
public class Simbol_Writer 
{
    private FileWriter tokenConstants = null;
    private FileWriter SymbolConstans = null;
    
    /**
    *  Constructor de la clase, enlaza los dos archivos y ademas escribe las
    *  cabeceras de las clases.
    * 
     * @throws java.io.IOException
    */
    public Simbol_Writer() throws IOException
    {
        tokenConstants = new FileWriter("TokenConstants.java");
        SymbolConstans = new FileWriter("SymbolConstants.java");
        
        tokenConstants.write("public interface TokenConstants {\n");
        tokenConstants.write("\n");
        
        SymbolConstans.write("public interface SymbolConstants {\n");
        SymbolConstans.write("\n");
    }
    
    /**
     * Escribe la lista de simbolos en los respectivos archivos con sus valores
     * @param Terminal
     * @param Noterminales
     * @throws IOException 
     */
    public void escribe(ArrayList<String> Terminal, ArrayList<String> Noterminales) throws IOException
    {
        for(int i = 0; i < Terminal.size(); i++)
        {
            tokenConstants.write("public int " + Terminal.get(i) + " = " + (i) + ";\n");
        }
        tokenConstants.write("}");
        tokenConstants.close();
        
        for(int i = 0; i < Noterminales.size(); i++)
        {
            SymbolConstans.write("public int " + Noterminales.get(i) + " = " + i + ";\n");
        }
        SymbolConstans.write("}");
        SymbolConstans.close();
    }
    
}
