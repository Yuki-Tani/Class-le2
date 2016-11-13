package ch.idsia.agents.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;

public class OwnAStarBrain extends OwnAgentBrain{
	
	public static final int X_MAX = 256;
	public static final int Y_MAX = 15;
	public static OwnNode[][] map = new OwnNode[Y_MAX+1][X_MAX+1];
	
	public PriorityQueue<OwnNode> open;
	
	public OwnAStarBrain(OwnAgent agent){
		super(agent);
	}
	
	public OwnAgentBrain optional;
	
	public static void writeData(){
		for(int col=0;col<=X_MAX;col++){
			for(int row=Y_MAX;row>=0;row--){
				System.out.print(map[row][col].cell+"("+map[row][col].valid+") ");
			}
			System.out.println();
		}
	}
	
	@Override
	public void prepare(){
		for(int row=0;row<=Y_MAX;row++){
			for(int col=0;col<=X_MAX;col++){
				map[row][col] = new OwnNode(row,col);
			}
		}
		optional = new OwnBasicBrain02(ag);
		optional.connect(sn);
	}
	
	@Override
	public void direction(){
		memoryMap();
		
		optional.direction();
	}
	
	public ArrayList<OwnNode> aStarSearch(OwnNode start,OwnNode goal){
		open = new PriorityQueue<OwnNode>();
		ArrayList<OwnNode> route = new ArrayList<OwnNode>();
		int searchCode = OwnNode.getSearchCode(start, goal);
		start.prepareNode(searchCode);
		start.setG(0);
		start.open(open);
		
		OwnNode now;
		while(true){
			if(open.size()==0) return null;
			now = open.poll();
			if(now == goal) break;
			else now.close();
			// 隣接操作
			
			
		}
		return route;
		
	}
	
/*	public boolean canGo(OwnNode now,OwnNode aim){
		if(now.cy<aim.cy){
			
		}else if(now.cy==aim.cy){
			
		}else{
			
		}
	}
*/	
	public void memoryMap(){
		int x = (int)ag.marioFloatPos[0]/16;
		int y = (int)ag.marioFloatPos[1]/16;
		OwnNode node = null;
		int cell = OwnNode.NULL;
		int nowX,nowY;
		for(int row=18;row>=0;row--){
			nowY = y+row-ag.marioEgoRow;
			if(nowY<0 || nowY>Y_MAX) continue;
			for(int col=0;col<=18;col++){
				nowX = x+col-ag.marioEgoCol;
				if(nowX<0 || nowX>X_MAX) continue;
				node = map[nowY][nowX];
				switch(ag.getReceptiveFieldCellValue(row,col)){
				case GeneralizerLevelScene.BRICK:
				case GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH:
				case GeneralizerLevelScene.FLOWER_POT_OR_CANNON:
					node.cell = OwnNode.FILLED; break;
				case GeneralizerLevelScene.BORDER_HILL:
					node.cell = OwnNode.THROUGH; break;
				default: 
					node.cell = OwnNode.BLANK;
				}
				
				if(node.cell==OwnNode.FILLED || row==18 || nowY+1>Y_MAX) continue;
				if(map[nowY+1][nowX].cell == OwnNode.FILLED ||
					map[nowY+1][nowX].cell == OwnNode.THROUGH){
						node.valid = true;
				}
				
			}
		}
	}
}
