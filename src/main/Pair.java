package main;

import java.util.ArrayList;

public class Pair<A,B> {
    public final A a;
    public final B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pair<?,?>) {
			Pair<Integer, Integer> aux = (Pair<Integer, Integer>) obj;
			return (aux.a.equals(this.a) && aux.b.equals(this.b));
		}
		return false;
	}
    
    
};
