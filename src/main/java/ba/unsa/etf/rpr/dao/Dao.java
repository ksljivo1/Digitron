package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.exceptions.DigitronException;

import java.util.List;

/**
 * Root interface for all DAO classes
 *
 * @author Dino Keco
 */

public interface Dao<T> {
    T getById(int id) throws DigitronException;
    T add(T item) throws DigitronException;
    T update(T item) throws DigitronException;
    void delete(int id) throws DigitronException;
    List<T> getAll() throws DigitronException;
}
