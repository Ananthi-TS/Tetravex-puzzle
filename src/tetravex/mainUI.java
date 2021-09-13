package tetravex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class mainUI {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		BufferedReader inFile=new BufferedReader( new FileReader( "C:\\Users\\anant\\OneDrive\\Desktop\\puzzle-samples\\2\\2.txt" ) );
		BufferedReader inFile2=new BufferedReader( new FileReader( "C:\\Users\\anant\\OneDrive\\Desktop\\puzzle-samples\\2\\2.txt" ) );
		Tetravex objTetravex= new Tetravex();
		
		System.out.println(objTetravex.loadPuzzle(inFile));
		System.out.println(objTetravex.loadPuzzle(inFile2));
		//System.out.println(objTetravex.solve());
		System.out.println(objTetravex.print());
		//System.out.println(objTetravex.loadPuzzle(inFile2));
		System.out.println(objTetravex.placePiece(4, 0, 0));
		System.out.println(objTetravex.placePiece(3, 0, 1));
		System.out.println(objTetravex.placePiece(2, 1, 0));
		System.out.println(objTetravex.placePiece(1, 1, 1));
		//System.out.println(objTetravex.placePiece(5, 0, 1));
		System.out.println("solve::::::::::::"+objTetravex.solve());
		System.out.println("solve::::::::::::"+objTetravex.solve());
		System.out.println(objTetravex.print());
		System.out.println(objTetravex.choices());
	}

}
