package edu.rutmiit.demo.booksapicontract.exception;

/**
 * Исключение, выбрасываемое при попытке создать релиз с уже существующим кодом UPC.
 * UPC (Universal Product Code) — уникальный штрих-код альбома или сингла.
 */
public class UpcAlreadyExistsException extends RuntimeException {
    public UpcAlreadyExistsException(String upc) {
        super("Release with UPC=" + upc + " already exists in the system");
    }
}