package SLR;

import ASA.Simbolo;
import java.util.ArrayList;

/**
 * Esta clase implementa un estado del automata de prefijos viables.
 * @version 1.0
 * @author Juan Antonio Contreras Fernández
 */
public class Estado 
{
    private int nEstado = 0; //Indica el numero del estado.
    private ArrayList<Estado> Siguientes = new ArrayList<>(); //Estados siguientes del estado actual.
    private final Simbolo transicion; //Transicion que lleva a este estado.
    private final ArrayList<ReglaAPV> Reglas; //Lista de reglas del actual estado. 
    private ArrayList<Simbolo> proximos = new ArrayList<>(); //proximos simbolos que se pueden analizar.
    
    public Estado(ArrayList<ReglaAPV> _Reglas, Simbolo _transicion)
    {
        Reglas = _Reglas;
        transicion = _transicion;
        
        //Añadimos todas las posibles transiciones
        for(ReglaAPV re:Reglas)
            if(re.getSiguiente() != null)
                if(proximos.indexOf(re.getSiguiente()) == -1)
                    proximos.add(re.getSiguiente()); 
        proximos.remove(new Simbolo("Lambda",1));
    }
    
    /**
     * Una vez aceptado el estado este tendra un numero propio para la tabla
     * @param n 
     */
    public void setNumero(int n)
    {
        nEstado = n;
    }
    
    /**
     * Devuelve la transicion que lleva a este estado
     *  @return Simbolo
     */
    public Simbolo getTransicion()
    {
        return transicion;
    }
    
    /**
     * Devuelve la lista de estados hijos.
     * @return Estado
     */
    public ArrayList<Estado> getListaSiguientes()
    {
        return (ArrayList<Estado>) Siguientes.clone();
    }
    
    /**
     * Genera un estado nuevo con el primer simbolo de su lista de proximos y consume dicho simbolo.
     * @param globales
     * @return Estado
     */
    public Estado generaSiguiente(ArrayList<ReglaAPV> globales)
    {
        //Clonamos las reglas globales del APV
        ArrayList<ReglaAPV> reglasBase = (ArrayList<ReglaAPV>) globales.clone();
        
        //Si el estado dispone de simbolos para consumir.
        if(proximos.size() > 0)
        {
            //Cogemos el siguiente simbolo.
            Simbolo consumir = proximos.remove(0);
            ArrayList<ReglaAPV> nuevas = new ArrayList<>();
            
            //Para cada una de las reglas de este estado.
            for(ReglaAPV r:Reglas)
            {
                //Genera una transicion si es posible consumir dicho simbolo
                ReglaAPV siguiente = r.transicion(consumir);
                if(siguiente != null)
                {
                    //Si se consiguio generar regla se añade a las del nuevo estado.
                    nuevas.add(siguiente);                   
                }
            }
            
            /**
             * Tenemos que añadir las reglas de los simbolos no temrinales
             * que estan como proximos en el estado de la lista de reglas iniciales.
             */
            for(int i = 0; i < nuevas.size(); i++)//Para cada regla del estado
            {
                if(!nuevas.get(i).esFinal())
                {
                    if(nuevas.get(i).getSiguiente().tipo() == 0)
                    {
                        for(int j = 0; j < reglasBase.size(); j++)//Buscamos las reglas producidas
                        {
                            if(nuevas.get(i).getSiguiente().equals(reglasBase.get(j).getInicial()))
                            {
                                nuevas.add(reglasBase.remove(j));
                                j--;//AL usar remove la lista reduce en 1 su capacidad y necesitamos volver.
                            }
                        }
                    }
                }
            }
        //Si hemos generado alguna regla devolvemos un nuevo estado con estas.
        if(!nuevas.isEmpty())
            return new Estado(nuevas, consumir);
        else
            return null;
        }
    return null;        
    }
    
    /**
     * Devuelve verdadero si quedan producciones.
     * @return boolean
     */
    public boolean getProducciones()
    {
       return proximos.isEmpty();
    }
    
    /**
     * Retorna el numero de estado.
     * @return int
     */
    public int getNestado()
    {
        return nEstado;
    }
    
    /**
     * Devuelve un int que simboliza si el estado provoca una accion de 
     * mover/reducir.
     * 
     * @return int 
     */
    public int getTipoEstado()
    {
        for(ReglaAPV r: Reglas)
        {
            if(r.esFinal() && r.getInicial().equals(new Simbolo("X",0)))
                return Integer.MIN_VALUE;
            if(r.esFinal())
                return -r.getNRegla();
        }
        return 0;
    }
    
    /**
     * Añade un estado a la lista de siguientes de dicho estado.
     * @param siguiente 
     */
    public void addSiguiente(Estado siguiente)
    {
        Siguientes.add(siguiente);
    }
    
    /**
     * Sobrecarga del metodo equals para la busqueda de estados repetidos dentro del automata
     * @param _o
     * @return 
     */
    @Override
    public boolean equals(Object _o)
    {
        if(_o instanceof Estado)
        {
            Estado aux = (Estado) _o;
            if(this.transicion == aux.transicion)
            {
                if(this.Reglas.size() == aux.Reglas.size())
                    for(int i = 0; i < this.Reglas.size(); i++)
                    {
                        if(!this.Reglas.get(i).equals(aux.Reglas.get(i)))
                            return true;
                    }
            }
        }
        return false;
    }
}
