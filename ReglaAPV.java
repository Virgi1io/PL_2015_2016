package SLR;

import ASA.Simbolo;
import java.util.ArrayList;

/**
 * Clase encargada del conjunto de reglas de cada estado del APV.
 * @author Juan Antonio Contreras Fernández
 */
public class ReglaAPV 
{
    private final Simbolo inicial; //Simbolo inicial de la regla
    private final ArrayList<Simbolo> Produccion; //Simbolos producidos por dicha regla
    private int punto; //Punto que indica el actual avance de la regla en el APV
    private int nRegla;

    /**
     * Coonstructor de la clase ReglaAPV
     * @param _inicial
     * @param producido
     * @param pActual
     **/    
    public ReglaAPV(Simbolo _inicial, ArrayList<Simbolo> producido, int pActual, int numeroR)
    {
        this.inicial = new Simbolo(_inicial.getNombre(), _inicial.tipo());
        this.Produccion = (ArrayList<Simbolo>) producido.clone();
        this.punto = pActual;
        nRegla = numeroR;
        
        //Si no tiene lista de producidos es que es lambda y se le añade el simbolo.
        if(Produccion.isEmpty())
        {
            Produccion.add(new Simbolo("Lambda", 1));
        }            
    }
    
    /**
     * Devuelve el lugar donde se encuentra actualmente el punto en la expresion punteada
     * @return int 
     */
    public int getPunto()
    {
        return punto;
    }
    
    /**
     * Devuelve el simbolo actual en el que se encuentre la produccion, sirve 
     * para desarrollar los estados del APV
     * @return Simbolo 
     */
    public Simbolo getSiguiente()
    {
        if(punto < Produccion.size())
            return Produccion.get(punto);
        else 
            return null;
    }
    
    /**
     * Devuelve el simbolo inicial de la regla.
     * @return Simbolo
     */
    public Simbolo getInicial()
    {
        return inicial;
    }
    
    public void setNumero(int numeroR)
    {
        nRegla = numeroR;
    }
    
    /**
     * avanza un lugar el punto en la expresion punteada, si llega al final(es igual al tamaño)
     * este devuelve true señalando que se llego al final de la expresion productora.
     * @return boolean 
     */
    public boolean avance()
    {
        if(punto < Produccion.size())
            punto++;
        return(punto == Produccion.size());
    }
    
    /**
     * Devuelve verdadero si la regla es una regla punteada al final
     * @return boolean
     */
    public boolean esFinal()
    {
        return (punto == Produccion.size());
    }
    
    /**
     * Devuelve una regla nueva con la transicion hecha y los valores cambiados.
     * @param trans
     * @return Regla
     */
    public ReglaAPV transicion(Simbolo trans)
    {
        ReglaAPV aux = new ReglaAPV(this.inicial, this.Produccion, punto, nRegla);
        if(!aux.esFinal())
        {
            if(aux.getSiguiente().equals(trans))
            {
                aux.avance();
                return aux;
            }
            else
            {
                return null;
            }
        }
        return null;
    }   
    
    /**
     * Compara dos reglas y devuelve true si estas son iguales.
     * @param r
     * @return boolean
     */
    @Override
    public boolean equals(Object r)
    {
        if(r instanceof ReglaAPV)
        {
            ReglaAPV aux = (ReglaAPV) r;
            if(this.inicial.equals(aux.inicial))
            {
                if(this.punto == aux.punto)
                {
                    if(this.Produccion.size() == aux.Produccion.size())
                    {
                        for(int i = 0; i < Produccion.size(); i++)
                        {
                            if(this.Produccion.get(i).equals(aux.Produccion.get(i)))
                                return false;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    int getNprod() 
    {
        return Produccion.size();
    }

    int getNRegla() 
    {
        return this.nRegla;
    }
    
    
    /**
     * Devuelve el simbolo siguiente al simbolo dado:
     * devuelve el simbolo inicial si la regla se acaba, el simbolo siguiente
     * si este existe y el simbolo "vacio" si no hay coincidencias.
     * 
     * @param simb
     * @return Simbolo
     */
    public Simbolo getSiguienteA(Simbolo simb)
    {
        for(int i = 0; i < Produccion.size(); i++)
        {
            if(Produccion.get(i).equals(simb))
            {
                if(Produccion.size()-1 == i)//Si es el ultimo devuelve el simbolo incial.
                    return inicial;
                else
                    return Produccion.get(i+1);//Si no devuelve el siguiente simbolo.
            }
        }
        return new Simbolo("vacio",1);
    }
}
