package goldenshiners;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vector2d v,w,e1,e2,o;
		v=Vector2d.random();
		w=Vector2d.random();
		e1=new Vector2d(1,0);
		e2=new Vector2d(0,1);
		o=new Vector2d();
		System.out.println(v);
		System.out.println(w);
		System.out.println(e1);
		System.out.println(e2);
		System.out.println(o);
		System.out.println(v.dot(w));
		System.out.println(v.dot(e1));
		System.out.println(w.dot(o));
	}

}
