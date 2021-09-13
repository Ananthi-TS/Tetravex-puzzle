package tetravex;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Tetravex {

	private Piece[][] puzzleArr=null;
	private Map<Integer,Piece> puzzleMap = null;
	private int rowSize,colSize,choiceCount=0;
	private Set<Integer> piecesInLst=null;
	private Piece pieceToStart = null;
	
	
	/**
	 * This method is used add load the puzzle.
	 * The method reads the puzzle information from the input stream.
	 * Puzzle array is initialized with the given row and column size.
	 * Puzzle pieces are stored in a map, with key as puzzle name and value as puzzle piece
	 * 
	 * @param - Input stream with puzzle information as BufferedReader object.
	 * @return - Returns true is puzzle information is stored and return false in case of any errors.
	 */
	public boolean loadPuzzle(BufferedReader stream) {
		if(stream!=null) {
			try {
				String firstLine = stream.readLine(); 				//first line contains row and column size.
				
				String sizeArr[]=firstLine.trim().split("\\s+"); 	// splitting them and storing the values in global variable
				rowSize=Integer.parseInt(sizeArr[0]);  
				colSize=Integer.parseInt(sizeArr[1]);
				
				puzzleArr=new Piece[rowSize][colSize]; 				//Initializing the puzzle array
				
				puzzleMap = new HashMap<Integer,Piece>(); //Initializing the global map
				piecesInLst=new HashSet<>();
				
				String nextLine=stream.readLine();
				while ((nextLine!=null) && !(nextLine.trim().equals(""))) {  // saving the puzzle pieces in a global map
					String arr[]=nextLine.split("\\s+");
					int name=Integer.parseInt(arr[0]);
					int top=Integer.parseInt(arr[1]);
					int left=Integer.parseInt(arr[2]);
					int bottom=Integer.parseInt(arr[3]);
					int right=Integer.parseInt(arr[4]);
					
					if( (name>=0) && (top>=0) && (left>=0) && (bottom>=0) && (right>=0)   ) { //pieces contains positive numbers. Validating the pieces
						puzzleMap.put(name,new Piece(name,top,left,bottom,right));   // storing the pieces in the global map. Key- Piece name, value- Piece
						nextLine = stream.readLine();
					}else { 
						// any errors, the values stored in the global variables are deleted.
						puzzleArr = null;  
						puzzleMap=null;
						return false;  //if  validation fails method will return false;
					}
				}
				
				// Validation. Number of pieces added should match number of cells
				if( !(  (puzzleMap.size())==(rowSize * colSize) )){
					// any errors, the values stored in the global variables are deleted.
					puzzleArr = null;  
					puzzleMap =null;
					return false;
				}
				
				mapAdjacentPieces();
				return true;
				
			} catch (Exception e) {
				//If file is empty, or file misses any value, when accessing the array, after using split(), it will throw exception
				// any errors, the values stored in the global variables are deleted.
				puzzleArr = null;  
				puzzleMap = null;
				return false;
			}
			
		}
		return false;
	}
	
	/**
	 * This method is used to solve the puzzle.
	 * If puzzle is partially solved, if will solve with the remaining pieces or else it will solve entirely
	 * The method in turn calls two private methods which places the puzzle pieces in the grid using recursion.
	 *
	 * @return - Returns true is puzzle can be solved and return false in case of any errors or if puzzle cannot be solved.
	 */
	public boolean solve() {
		
		if( (puzzleArr!=null)  &&  (puzzleMap!=null)) {
			choiceCount=0; //re-initializing choice count
			
			if(piecesInLst.size()==puzzleMap.size()) { //puzzle already solved
				return true;
			}
			
			// if there is no piece placed in the puzzle by user
			if((piecesInLst.isEmpty()) || (piecesInLst.size()==0)) {
				
				return startSolve(); //private method which solves the puzzle automatically
				
			}else {
				//if user has already placed some pieces in the puzzle
				int xPos=0;
				int yPos=0;
				boolean flag=false;
				
				for(int x=0;x<rowSize;x++) {
					for(int y=0;y<colSize;y++) {
						if(puzzleArr[x][y]==null) {
							xPos=x;	yPos=y; flag=true; //calculating the first free position
							break;
						}
					}
					if(flag) break;
				}
				
				for(Entry<Integer,Piece> mapEntry: puzzleMap.entrySet()) {
					if( !(piecesInLst.contains(mapEntry.getKey())) ) {  //Filling the remaining places in the  grid with pieces left
						if(solvePuzzle(xPos,yPos,mapEntry.getValue())) {
							return true;
						}
					}
					
				}
			}
		}
		return false;
		
	}
	
	/**
	 * This method is used place a puzzle piece in the puzzle grid at the given positions
	 * The method calls an internal private method, to check if the piece can be placed at the given position
	 * Gets the puzzle piece from the global map by passing piece name as key
	 * 
	 * @param pieceName- Name of the puzzle piece to be placed
	 * @param xPosition- specifies the row value in which the puzzle should be placed
	 * @param yPosition- specifies the column value in which the puzzle should be placed
	 * @return - Returns true is piece can be placed at the given position and return false otherwise.
	 */
	public boolean placePiece( int pieceName, int xPosition, int yPosition ) {
		
		if( (puzzleArr!=null)  &&  (puzzleMap!=null)) {
			
			Piece pieceToPlace= puzzleMap.get(pieceName);
			
			if(pieceToPlace!=null) {
				if(checkPieceFit(xPosition, yPosition, pieceToPlace)) { //checks if the piece can be placed at the given position
					piecesInLst.add(pieceToPlace.getName());
					puzzleArr[xPosition][yPosition]= pieceToPlace; //places the piece in the given position
					return true;
				}
			}
		}
		
		return false;
		
	}
	
	/**
	 * This method is used to print the puzzle
	 * If a position is empty,the method prints xxx at that particular position
	 * The method also prints the pieces that are not placed in the puzzle
	 * 
	 * @return - Returns the string which contains the puzzle.
	 */
	public String print( ) {
		String puzzleString="";
		if(puzzleArr!=null) {
			for(int x=0;x<rowSize;x++) {
				for(int y=0;y<colSize;y++) {
					if(puzzleArr[x][y]==null) { // if the spot is empty
						puzzleString+="xxx\t";
					}else {				
					puzzleString+=puzzleArr[x][y].getName()+"\t"; //if piece exists, print the piece name
					}
				}
				puzzleString+="\n";
			}
			
			if( (puzzleMap!=null) && (piecesInLst!=null)) {
				if(piecesInLst.size()!=puzzleMap.size()) {
					puzzleString+="--------------------\n";
					for(Entry<Integer,Piece> mapEntry: puzzleMap.entrySet()) { //prints the pieces which are not placed in the puzzle
						if( !(piecesInLst.contains(mapEntry.getKey())) ) {
							Piece piece=mapEntry.getValue();
							puzzleString+=piece.getName()+"\t"+piece.getTop()+"\t"+piece.getLeft()+"\t"+piece.getBottom()+"\t"+piece.getRight()+"\n";
						}
						
					}
				}
			}
		}
		return puzzleString;
		
	}
	
	/**
	 * This method returns the number of trials that occurred while solving the puzzle
	 * 
	 * @return - Returns number of choices made in solve() method.
	 */
	public int choices( ) {
		return choiceCount; //global variable, updated each time whenever backtrack occurs
		
	}
		
	
	/*********************Private methods**********************/
	
	/**
	 * This is a private method used to match a piece with it's adjacent pieces
	 * For each piece, the list of possible top, left, bottom, right pieces are updated in a list
	 * 
	 * If a piece as any of the lists(top/bottom/left/right) empty, that piece is considered as start piece
	 */
	private void mapAdjacentPieces() {
		List<Piece> pieceList = new ArrayList<Piece>(puzzleMap.values());   //List to store all the pieces in the puzzle
		for(Entry<Integer,Piece> mapEntry:puzzleMap.entrySet()) {
			
			Piece piece = mapEntry.getValue();
			
			List<Piece> topList=new ArrayList<Piece>();
			List<Piece> bottomList=new ArrayList<Piece>();
			List<Piece> leftList=new ArrayList<Piece>();
			List<Piece> rightList=new ArrayList<Piece>();
			
			//for each piece it finds the possible top, left, bottom and right pieces
			
			for(Piece tmpPiece:pieceList) {    //Iterates over the pieceList and  find the list 
				
				if(!(piece.equals(tmpPiece))){	
				
				if((piece.getTop()==tmpPiece.getBottom()) ){
					topList.add(tmpPiece);
				}
				
				if((piece.getBottom()==tmpPiece.getTop()) ){
					bottomList.add(tmpPiece);
				}
				
				if((piece.getLeft()==tmpPiece.getRight()) ){
					leftList.add(tmpPiece);
				}
				if((piece.getRight()==tmpPiece.getLeft()) ){
					rightList.add(tmpPiece);
				}
				}
			}
			piece.setTopList(topList);
			piece.setBottomList(bottomList);
			piece.setLeftList(leftList);
			piece.setRightList(rightList);
			
			if(topList.size()==0 || leftList.size()==0 || bottomList.size()==0 || rightList.size()==0) { //if atleast one list is empty, start with that piece
				pieceToStart=piece;
			}
			
		}
	}
	
	/**
	 * This is a private method used to solve the puzzle
	 * If a piece as any of the lists(top/bottom/left/right) empty, that piece is considered as start piece
	 * According to the start piece, a loop is iterated over a list of possible positions and the remaining pieces are placed accordingly
	 * If there is no start piece, the first element in the map is taken and brute-force is applied
	 * 
	 * @return returns true if the puzzle is solved, else returns false
	 */
	private boolean startSolve() {
		
		if(pieceToStart!=null) { //if there is start piece, which hs empty top/bottom/left/righht piece
			
			if(pieceToStart.getTopList().size()==0) { //if picece has no top, then it will come in the first row. so iterating only first row
				//i=0; j= 0 to colSize-1
				
				for(int j=0;j<=colSize-1;j++) {
					if(solvePuzzle(0,j,pieceToStart)) {
						return true;
					}
				}
				
			}else if(pieceToStart.getLeftList().size()==0) { // if piece as no left, means it will come in tthe first column
				//i= 0 to rowSize-1; j=0
				
				for(int i=0;i<=rowSize-1;i++) {
					if(solvePuzzle(i,0,pieceToStart)) {
						return true;
					}
				}
				
			}else if(pieceToStart.getBottomList().size()==0) { //if piece has no bottom, iterating the last row
				//i=rowSize-1 ; j= 0 to colSize-1
				for(int j=0;j<=colSize-1;j++) {
					if(solvePuzzle(rowSize-1,j,pieceToStart)) {
						return true;
					}
				}
				
			}else if(pieceToStart.getRightList().size()==0) { //if piece has no right, iterating the last column
				//i = 0 to rowSize-1; j=colSize-1
				for(int i=0;i<=rowSize-1;i++) {
					if(solvePuzzle(i,colSize-1,pieceToStart)) {
						return true;
					}
				}
			}
		}
		else {
			//bruteForce
			for(Entry<Integer,Piece> mapEntry:puzzleMap.entrySet()) { //if there is no start piece, start from the first puzzle piece in the map
				if(solvePuzzle(0,0,mapEntry.getValue())) {
					return true;
				}
			}
			
		}
		return false;
	}

	/**
	 * This is a private method used to place the first piece and then place the remaining pieces recursively
	 * If puzzle is already filled, it returns true
	 * After placing the first piece, it makes a recursive call, to place the remaining pieces
	 * If the recursion fails, and puzzle cannot be solved by placing the particular piece in the specified position, then it performs backtracking
	 * 
	 * @param x- Row position in which the piece should be placed
	 * @param y- column position in which the piece should be placed
	 * @param pieceToPlace- Piece object to be placed
	 * @return  returns true if the puzzle can be solved by placing the piece at the specified position
	 */
	private boolean solvePuzzle(int x,int y,Piece pieceToPlace) {
		
		if(piecesInLst.size()<puzzleMap.size()) { // if puzzle is not filled and has empty place
			puzzleArr[x][y]=pieceToPlace; //adds piece in the location
			piecesInLst.add(pieceToPlace.getName()); //stores in the global set, to keep track
			if(addPieceRecursively(x,y,pieceToPlace)) { //recursively add other pieces
				return true;
			}else {
				choiceCount++; // if the piece doesn't fit in any position, backtrack
			puzzleArr[x][y]=null;
			piecesInLst.remove((Integer)pieceToPlace.getName()); //remove the piece from set
			}
		}else {
			return true;
		}
		
	return false;
	}
	
	/**
	 * This is a private method used to place the next piece in the adjacent positions (next top/left/bottom/right) 
	 * 
	 * @param x- Row position in which the piece should be placed
	 * @param y- column position in which the piece should be placed
	 * @param pieceToPlace- Piece object to be placed
	 * @return  returns true if the puzzle can be solved by placing the piece at the specified position
	 */
	private boolean addPieceRecursively(int x,int y,Piece piece) {
		
		if(piecesInLst.size()<puzzleMap.size()) { //if the puzzle is not filled and has empty place
		
			if(setPieceAsTop(x,y,piece)) return true; //checks if piece can be  placed at the top
			if(setPieceAsLeft(x,y,piece)) return true; //checks if piece can be  placed at the left
			if(setPieceAsBottom(x,y,piece)) return true; //checks if piece can be  placed at the bottom
			if(setPieceAsRight(x,y,piece)) return true; //checks if piece can be  placed at the right
		
		}else {
			return true;
		}
		
		return false; // if piece can't be placed at any location, return false
	}
	
	/**
	 * This is a private method used to place the next piece in the top position
	 * It fetches the top list from the piece and passes that value to the setPiece() method
	 * 
	 * @param x- Row position in which the piece should be placed
	 * @param y- column position in which the piece should be placed
	 * @param pieceToPlace- Piece object to be placed
	 * @return  returns true if piece can be placed at the specified position
	 */
	private  boolean setPieceAsTop(int i,int j,Piece piece) {
		
		if(i-1>=0) {
			return setPiece(i-1,j,piece.getTopList()); //list contains possible pieces that can be placed at the top
		}
		return false;
	}
	
	/**
	 * This is a private method used to place the next piece in the left position
	 * It fetches the left list from the piece and passes that value to the setPiece() method
	 * 
	 * @param x- Row position in which the piece should be placed
	 * @param y- column position in which the piece should be placed
	 * @param pieceToPlace- Piece object to be placed
	 * @return  returns true if piece can be placed at the specified position
	 */
	private  boolean setPieceAsLeft(int i,int j,Piece piece) {
		
		if(j-1>=0) {
				return setPiece(i,j-1,piece.getLeftList()); //list contains possible pieces that can be placed at the left
		}
		return false;
	}
	
	/**
	 * This is a private method used to place the next piece in the bottom position
	 * It fetches the bottom list from the piece and passes that value to the setPiece() method
	 * 
	 * @param x- Row position in which the piece should be placed
	 * @param y- column position in which the piece should be placed
	 * @param pieceToPlace- Piece object to be placed
	 * @return returns true if piece can be placed at the specified position
	 */
	private  boolean setPieceAsBottom(int i,int j,Piece piece) {
		
		if(i+1<=rowSize-1) {
				return setPiece(i+1,j,piece.getBottomList()); //list contains possible pieces that can be placed at the bottom
		}
		return false;
	}

	/**
	 * This is a private method used to place the next piece in the right position
	 * It fetches the right list from the piece and passes that value to the setPiece() method
	 * 
	 * @param x- Row position in which the piece should be placed
	 * @param y- column position in which the piece should be placed
	 * @param pieceToPlace- Piece object to be placed
	 * @return  returns true if piece can be placed at the specified position
	 */
	private  boolean setPieceAsRight(int i,int j,Piece piece) {
	
	if(j+1<=colSize-1) {
		return setPiece(i,j+1,piece.getRightList()); //list contains possible pieces that can be placed at the right
	}
	return false;
	}

	
	/**
	 * This is a private method used to place the piece in the given position
	 * The method iterates over the passed list and checks if any one of the piece in the list can  be placed at the specified position
	 * 
	 * @param x- Row position in which the piece should be placed
	 * @param y- column position in which the piece should be placed
	 * @param pieceToPlace- Piece object to be placed
	 * @return  returns true if any one of the piece in the list can be placed in the specified position
	 */
	private boolean setPiece(int i,int j,List<Piece> valueList) {
		
		for(Piece mapKey: valueList) {  //iterating over the list, to place the piece
			if(!(piecesInLst.contains(mapKey.getName()))){
				if(checkPieceFit(i,j,mapKey)) {
					piecesInLst.add(mapKey.getName());
					puzzleArr[i][j]=mapKey;
					if(addPieceRecursively(i,j,mapKey)) { //recursive call to place the remaining pieces
						return true;
					}else {
						choiceCount++;
						puzzleArr[i][j]=null;
						piecesInLst.remove((Integer) mapKey.getName());
					}
				}
			}
		}
		return false;
	}


	/**
	 * This is a private method used to checkk if the piece can be placed at the particular position in the array
	 * 
	 * @param x- Row position in which the piece should be placed
	 * @param y- column position in which the piece should be placed
	 * @param pieceToPlace- Piece object to be placed
	 * @return  returns true if piece can be placed in the specified position
	 */
	private  boolean checkPieceFit(int i, int j,Piece currentPiece) {
			
		if((i<0) || (j<0) || (i>=rowSize) || (j>=colSize)){ // validation
			return false;
		}
		if(puzzleArr[i][j]!=null) {  // if the  position is not already filled
			return false;
		}
		
			//top check
			if(i-1>=0) {
				if(puzzleArr[i-1][j]!=null) {
					if(currentPiece.getTop()!=puzzleArr[i-1][j].getBottom()) { //checks if the piece matches with the top piece
						return false;
					}
				}
			}
			//left check
			if(j-1>=0) {
				if(puzzleArr[i][j-1]!=null) {
					if(currentPiece.getLeft()!=puzzleArr[i][j-1].getRight()) {  //checks if the piece matches with the left piece
						return false;
					}
				}
			}
			
			//bottom check
			if(i+1<=rowSize-1) {
				if(puzzleArr[i+1][j]!=null) {
					if(currentPiece.getBottom()!=puzzleArr[i+1][j].getTop()) {  //checks if the piece matches with the bottom piece
						return false;
					}
				}
			}
			
			
			//right check
			if(j+1<=colSize-1) {
				if(puzzleArr[i][j+1]!=null) {
					if(currentPiece.getRight()!=puzzleArr[i][j+1].getLeft()) {  //checks if the piece matches with the right piece
						return false;
					}
				}
			}
			return true;
	}
	
}
