package Common;

import androidx.annotation.NonNull;

import java.util.Iterator;
import java.util.List;

public class ReadOnlyList<T> implements Iterable<T> {
    List<T> _list;

    public ReadOnlyList(List<T> list){
        _list = list;
    }

    public T get(int index){
        return _list.get(index);
    }

    public int size(){
        return _list.size();
    }

    public boolean isEmpty(){
        return _list.isEmpty();
    }

    public int indexOf(T element){
        return _list.indexOf(element);
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return _list.iterator();
    }
}
