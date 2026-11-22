package valor.data;

// turns one row of a data file into an object (a weapon, a potion, ...)
public interface RecordParser<T> {
    T parse(String[] row);
}
