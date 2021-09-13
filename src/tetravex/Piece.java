package tetravex;

import java.util.ArrayList;
import java.util.List;

public class Piece {

	int name,top,left,bottom,right=0;

	List<Piece> topList,bottomList,leftList,rightList= new ArrayList<Piece>();
	
	/**
     * Constructor to set the piece values
     *
     * @param name -- name of thr piece
     * @param top -- top value
     * @param left -- left value
     * @param bottom -- bottom value
     * @param right -- right value
     */
	public Piece(int name, int top, int left, int bottom, int right) {
		super();
		this.name = name;
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}

	public int getTop() {
		return top;
	}

	public int getLeft() {
		return left;
	}

	public int getBottom() {
		return bottom;
	}

	public int getRight() {
		return right;
	}

	public int getName() {
		return name;
	}

	public List<Piece> getTopList() {
		return topList;
	}

	public void setTopList(List<Piece> topList) {
		this.topList = topList;
	}

	public List<Piece> getBottomList() {
		return bottomList;
	}

	public void setBottomList(List<Piece> bottomList) {
		this.bottomList = bottomList;
	}

	public List<Piece> getLeftList() {
		return leftList;
	}

	public void setLeftList(List<Piece> leftList) {
		this.leftList = leftList;
	}

	public List<Piece> getRightList() {
		return rightList;
	}

	public void setRightList(List<Piece> rightList) {
		this.rightList = rightList;
	}

	@Override
	public String toString() {
		return top+","+left+","+bottom+","+right;
	}


	
}
