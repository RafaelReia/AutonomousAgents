package main;

public class Pair<A extends Comparable <A>,B extends Comparable<B>> implements Comparable<Pair<A, B>> {
    public A a;
    public B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

	@Override
	public int compareTo(Pair<A, B> o) {
		int cmp = this.a.compareTo(o.a);
		if (cmp == 0)
			cmp = this.b.compareTo(o.b);
		return cmp;
	}
    
};
