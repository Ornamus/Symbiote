package symbiote.misc;

public class Pair<T1, T2> {
    
    public T1 one;
    public T2 two;
    
    public Pair(T1 t1, T2 t2) {
        one = t1;
        two = t2;
    }
    
    public boolean contains(Object o) {
        return (one == o || two == o);
    }
    
    public boolean equals(Pair<T1, T2> pair) {
        return (contains(pair.one) && contains(pair.two));
    }
}
