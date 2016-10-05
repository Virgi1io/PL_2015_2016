package Analizador_Lexico;



/**
 * Interfaz que define los c�digos de las diferentes categor�as l�xicas
 *  
 * * @author Francisco Jose Moreno Velo
 *
 */
public interface TokenConstants {

	/**
	 * Final de fichero
	 */
	public int EOF = 0;        
        
        //--------------------------------------------------------------//
	// Identificadores
	//--------------------------------------------------------------//
        
        /**
	 * Simbolo terminal "[a-z],[A-Z]"
         * 
	 */
	public int NOTERMINAL = 1;
        
        /**
	 * Punto y coma ";"
	 */
	public int TERMINAL = 2;
	
	//--------------------------------------------------------------//
	// Separadores
	//--------------------------------------------------------------//
	
        /**
	 * Punto y coma ";"
	 */
	public int SEMICOLON = 3;
		
	//--------------------------------------------------------------//
	// Operadores
	//--------------------------------------------------------------//

	/**
	 * Igualdad "::="
	 */
	public int EQ = 4;
        
        /**
         * OR "|"
         */
        public int BAR = 5;
}
