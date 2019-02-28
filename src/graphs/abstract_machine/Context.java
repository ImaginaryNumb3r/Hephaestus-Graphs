package graphs.abstract_machine;

/**
 * Creator: Patrick
 * Created: 28.02.2019
 * Purpose:
 */
public interface Context<Acc, Data> {

    Iterable<Acc> getInputStream();

    Data getBuffer();
}
