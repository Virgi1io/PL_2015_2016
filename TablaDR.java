/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SLR;

import java.util.ArrayList;

/**
 * Clase para la creacion y manejo de la tabla generada por algoritmo slr.
 * Incluye 3 tablas, reglas, acciones e ir_a;
 * @author Juan A. Contreras Fernández
 */
public class TablaDR 
{
    /**
     * tabla de ir_a tiene tantas columnas como simbolos no terminales y tantas
     * filas como estados.
     */
    private int[][] go_to;
    
    /**
     * Tabla de acciones, tiene tantas columnas como simbolos terminales y 
     * tantas filas como estados generados.
     */ 
    private int[][] Action;
    
    /**
     * Tabla de reglas, tiene tantas columnas como reglas y dos culumnas una
     * indica las reglas y otra la cantidad de simbolos producidas por esta.
     */
    private int[][] Rules;
    
    /**
     * Constructor de la clase, inicia las tablas a sus tamaños necesarios.
    *
     * @param Terminales
     * @param Noterminales
     * @param reglas
     * @param nEstados
    */
    
    //Listas de simbolos terminales y no terminales.
    private final ArrayList<String> Term;
    private final ArrayList<String> nTerm;
    
        
    public TablaDR(ArrayList<String> Terminales, ArrayList<String> Noterminales, ArrayList<ReglaAPV> reglas, int nEstados)
    {
        Term = Terminales;
        nTerm = Noterminales;
        
        go_to = new int[nEstados][Noterminales.size()];
        Action = new int[nEstados][Terminales.size()];
        Rules  = new int[reglas.size()+1][2];
        
        //Iniciamos ya las reglas.
        Rules[0][0] = 0;
        Rules[0][1] = 0;
        for(int i = 1; i < reglas.size()+1; i++)
        {
            Rules[i][0] = Noterminales.indexOf(reglas.get(i-1).getInicial().getNombre());
            Rules[i][1] = reglas.get(i-1).getNprod();
        }
    }
    
    /**
     * Añade un nuevo valor a la transicion desde el estado dado con el simbolo 
     * dado al estado estipulado
     * @param origen
     * @param simbolo
     * @param destino
     */
    public void addGoTo(int origen, String simbolo, int destino)
    {
        go_to[origen][nTerm.indexOf(simbolo)] = destino;
    }
    
    /**
     * Añade una accion a la tabla de acciones. 
     * -1 = aceptar; x = desplazar a x; 1x = reducir por x;
     * @param Estado
     * @param simbolo
     * @param accion 
     */
    public void addAction(int Estado, String simbolo, int accion)
    {
        Action[Estado][Term.indexOf(simbolo)] = accion;
    }
    
    /**
     * Devuelve la tabla de reglas.
     * @return int[][]
     */
    public int[][] getReglas()
    {
        return Rules;
    }
    
    /**
     * Devuelve la tabla de acciones.
     * @return int[][]
     */
    public int[][] getAcciones()
    {
        return Action;
    }
    
    /**
     * Devuelve la tabla de ir_a.
     * @return int[][]
     */
    public int[][] getIra()
    {
        return go_to;
    }
}
