package Generadores_de_Archivos;

import SLR.TablaDR;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Este archivo implementa el escritor que llevara a cabo la creacion del Parser.java
 * de la gramatica analizada.
 * 
 * @author Juan Antonio Contreras Fernandez
 */
public class Parser_Writer 
{
    private FileWriter Parser = null;
    
    public Parser_Writer() throws IOException
    {
        Parser = new FileWriter("Parser.java");
        
        Parser.write("public class Parser extends SLRParser implements TokenConstants,\n");
        Parser.write("SymbolConstants {\n\n");
        
        Parser.write("\tpublic Parser() {\n");
        Parser.write("\t\tinitRules();\n");
        Parser.write("\t\tinitActionTable();\n");
        Parser.write("\t\tinitGotoTable();\n}\n\n");
    }
    
    public void Escribir_Tabla(TablaDR tabla, ArrayList<String> Terminales, ArrayList<String> Noterminales) throws IOException
    {
        Escribir_Reglas(tabla.getReglas(), Noterminales);
        Escribir_Acciones(tabla.getAcciones(),Terminales);
        Escribir_Ir_a(tabla.getIra(), Noterminales);
        Parser.write("}");
        Parser.close();
    }
    
    private void Escribir_Reglas(int[][] Reglas, ArrayList<String> Noterminales) throws IOException
    {
        Parser.write("\tprivate void initRules() {\n");
        Parser.write("\t\tint[][] initRule = {\n");
        
        Parser.write("\t\t\t{ 0, 0 },\n");
        
        for(int i = 1; i < Reglas.length; i++)
        {
            Parser.write("\t\t\t{ " + Noterminales.get(Reglas[i][0]) + ", " + Reglas[i][1] + " },\n");
        }
        
        Parser.write("\t\t};\n\n");
        Parser.write("\t\tthis.rule = initRule;\n");
        Parser.write("\t}\n\n");
    }
    
    private void Escribir_Acciones(int[][] Acciones, ArrayList<String> Terminales) throws IOException
    {
        Parser.write("\tprivate void initActionTable() {\n");
        Parser.write("\t\tactionTable = new ActionElement[24][10];\n\n");
        
        for(int i = 0; i < Acciones.length; i++)
        {
            for(int j = 0; j < Acciones[i].length; j++)
            {
                //ActionELement aceptar.
                if(Acciones[i][j] == Integer.MIN_VALUE)
                    Parser.write("\t\t\tactionTable [" + i + "][" + Terminales.get(j) 
                                        + "] = new ActionElement(ActionElement.ACCEPT, 0);\n");
                if(Acciones[i][j] < 0 && Acciones[i][j] > Integer.MIN_VALUE)
                    Parser.write("\t\t\tactionTable [" + i + "][" + Terminales.get(j) 
                                        + "] = new ActionElement(ActionElement.REDUCE, " + -Acciones[i][j] + ");\n");
                if(Acciones[i][j] > 0)
                    Parser.write("\t\t\tactionTable [" + i + "][" + Terminales.get(j) + 
                            "] = new ActionElement(ActionElement.SHIFT, " + Acciones[i][j] + ");\n");                    
            }
            Parser.write("\n");
        }
        Parser.write("\t}\n\n");
    }
    
    private void Escribir_Ir_a(int[][] Go_to, ArrayList<String> Noterminales) throws IOException
    {
        boolean escrito = false;
        
        Parser.write("\tprivate void initGotoTable() {\n");
        Parser.write("\t\tgotoTable = new int[24][5];\n\n");
        
        for(int i = 0; i < Go_to.length; i++)
        {
            for(int j = 0; j < Go_to[i].length; j++)
            {
                if(Go_to[i][j] > 0)
                {
                    Parser.write("\t\t\tgotoTable [" + i + "][" + Noterminales.get(j) + 
                            "] = " + Go_to[i][j] + ";\n");
                    escrito = true;
                }                    
            }
            if(escrito)
            {
                Parser.write("\n");
                escrito = false;
            }
        }
        Parser.write("\t}\n");
    }
}

