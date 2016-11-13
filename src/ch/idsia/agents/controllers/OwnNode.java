package ch.idsia.agents.controllers;

import java.util.PriorityQueue;

public class OwnNode implements Comparable<OwnNode>{
	
	public int cx, cy;
	
	public static final int FILLED 	= 1,
							BLANK	= 0,
							THROUGH	= 5,
							NULL	= -1;
	
	public int cell;
	
	public boolean valid;
	
	private int searchCode;
	public int f,g,h;
	public OwnNode previous;
	
	public static final int OPEN = 1,
							CLOSE = 0,
							NON	 = -1;
	
	public int state;
	
	public OwnNode(int cx,int cy){
		this.cx = cx;
		this.cy = cy;
		cell = NULL;
		valid = false;
		searchCode = -1;
		f=0; g=0; h=0;
		state = NON;
	}
	
	public void prepareNode(int searchCode){
		this.searchCode = searchCode;
		h = Math.abs(codeToGoalX(searchCode) - cx)
				+ Math.abs(codeToGoalY(searchCode)-cy);
		state = NON;
	}
	
	public void setG(int g){
		this.g= g;
		f = g + h;
	}
	public void open(PriorityQueue<OwnNode> queue){
		queue.add(this);
		state = OPEN;
	}
	public void close(){
		state = CLOSE;
	}
	public boolean isOpen(int searchCode){
		return this.searchCode == searchCode && state == OPEN;
	}
	public boolean isClose(int searchCode){
		return this.searchCode == searchCode && state == CLOSE;
	}
	public boolean hasSameCode(OwnNode node){
		return searchCode == node.searchCode;
	}
	
	public static int getSearchCode(OwnNode start,OwnNode goal){
		return start.cx *1 + start.cy*1000 + goal.cx*100000 + goal.cy*100000000; 
	}
	public static int codeToStartX(int code){
		return code%1000;
	}
	public static int codeToStartY(int code){
		return code/1000%100;
	}
	public static int codeToGoalX(int code){
		return code/100000 % 1000;
	}
	public static int codeToGoalY(int code){
		return code/100000000;
	}
	
	@Override
	public int compareTo(OwnNode o) {
		return f-o.f;
	}
}
