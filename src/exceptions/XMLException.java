package exceptions;

/**
 * Exception a lever lors d'un probleme durant le parsing de fichiers XML
 */
public class XMLException extends Exception {

    public XMLException(String message) {
        super(message);
    }

}