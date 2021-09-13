package tetravex;

import java.util.ArrayList;

public class Main {

	public static void main(String a[]) {
		
		ArrayList<Piece> n=new ArrayList<Piece>();
		
		n.add(new Piece(1,1,1,1,1));
		n.add(new Piece(2,2,2,2,2));
		n.add(new Piece(3,3,3,3,3));
		n.add(new Piece(4,4,4,4,4));
		n.add(new Piece(5,5,5,5,5));
		
		System.out.println(n.toString()); 
		
		
	}
	
}
