package ASA;


/**
 * Esta clase implementa un simbolo de la gramatica, terminal o no terminal.
 * 
 * @author Juan Antonio Contreras Fernández
 */
public class Simbolo {
    
    private final String id;
    private final int tipo;//0 noterminal 1 terminal
    
    /**
     * Constructor de la clase que asigna la cadena que da nombre al simbolo
     * y el tipo de este.
     * @param _id
     * @param _tipo 
     */
    public Simbolo(String _id, int _tipo)
    {
        id = _id;
        tipo = _tipo;
    }
    
    /**
     * Devuelve un string con el identificador del simbolo.
     * @return
     */
    public String getNombre()
    {
        return id;
    }
    
    /**
     * Devuelve un int con el tipo del simbolo:
     * 0 =  no terminal.
     * 1 =  terminal.
     * @return int
     */
    public int tipo()
    {
        return tipo;
    }
    
    /**
     * Sobrecarga del metodo equals para la comparacion de simbolos de la
     * gramatica.
     * @param simbolo
     * @return boolean
     */
    @Override
    public boolean equals(Object simbolo)
    {
        if( simbolo instanceof Simbolo)
        {
            Simbolo aux = (Simbolo) simbolo;
            return id.equals(aux.id);
        }
        return false;
    }
    
}
