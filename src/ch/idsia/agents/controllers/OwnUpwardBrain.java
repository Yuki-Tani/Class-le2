package ch.idsia.agents.controllers;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;

public class OwnUpwardBrain extends OwnAgentBrain{
	private static final int NOMAL = 0,
							 RUN_UP = 1,
							 GO_OUT = 2;
	
	private int startCellX=0;
	private int state;
	public boolean fromHoleDanger = false;

	public OwnUpwardBrain(OwnAgent agent) {
		super(agent);
	}

	@Override
	public void prepare() {
		state = NOMAL;
		challengedCell = new HashMap<Point,Integer>();
		startCellX = ag.distancePassedCells;
	}

	@Override
	public void direction() {
		if(ag.distancePassedCells>startCellX){
			ag.hrzControl(1,0);
			ag.removeBrain();
			if(!fromHoleDanger){
				OwnDownwardBrain newBrain = new OwnDownwardBrain(ag);
				ag.setBrain(newBrain);
			}
		}
		if(isBan((int)ag.marioFloatPos[1]/16,(int)ag.marioFloatPos[0]/16)){
			state = GO_OUT; 
			aimPx = (-1 + ag.distancePassedCells)*16+8;
		}else{
			state = NOMAL;
		}
		switch(state){
		case NOMAL: nomalRoutine();	break;
//		case RUN_UP: runUpRoutine(); break;
		case GO_OUT: goOutRoutine(); break;
		}	
		if((int)ag.marioFloatPos[1]/16<=0){
			ag.jump(true);
			ag.right(true);
			ag.speed(true);
		}
	}
	
	public static final int BAN_COUNT = 4;
	public HashMap<Point,Integer> challengedCell;
	
	public void addChallenged(int cy,int cx){
		Integer count = challengedCell.get(new Point(cx,cy));
		if(count == null) challengedCell.put(new Point(cx,cy),1);
		else challengedCell.put(new Point(cx,cy),count+1);
	}
	public int getChallenged(int cy,int cx){
		return challengedCell.get(new Point(cx,cy));
	}
	
	public void setBan(int cy,int cx){
		challengedCell.put(new Point(cx,cy),BAN_COUNT);
	}
	public boolean isBan(int cy,int cx){
		Integer count = challengedCell.get(new Point(cx,cy));
		return count != null && count == BAN_COUNT;
	}
	
// routine methods	
	private int aimPx;
	
	private void nomalRoutine(){
		int nowRow = (int)ag.marioFloatPos[1]/16;
		int nowCol = (int)ag.marioFloatPos[0]/16;
		
		if(isBan(nowRow,nowCol)){
			ag.hrzControl(1,-16);
		}
		
		if(ag.jumpState == OwnAgent.ACCEPT && ag.isMarioAbleToJump && ag.isMarioOnGround){
			for(int row=-4;row<0;row++){
				for(int col=5;col>=-5;col--){
					if(sn.catchBlank(ag.marioEgoRow+row,ag.marioEgoCol+col) 
					    && sn.catchStand(ag.marioEgoRow+row+1,ag.marioEgoCol+col)
						&& !isBan(nowRow+row,nowCol+col)){
							aimPx = (col + ag.distancePassedCells)*16 + 8;
							if(Math.abs(col)!=5 || (col>0 && ag.speedX>=4) || (col<0 && ag.speedX<=4)){
								ag.jump(8);
								addChallenged(nowRow,nowCol);
							}
							ag.hrzControl(1,aimPx-ag.distancePassedPhys);
							return;
					}
				}
			}
			ag.hrzControl(1,0);
			ag.removeBrain();
			if(!fromHoleDanger){
				OwnDownwardBrain newBrain = new OwnDownwardBrain(ag);
				ag.setBrain(newBrain);
			}	
		}else{
			ag.hrzControl(1,aimPx-ag.distancePassedPhys);
		}
	}
	
	private void goOutRoutine(){
		ag.jump(1);
		ag.hrzControl(1,aimPx-ag.distancePassedPhys);
	}
	
	private int nowCell = -1;
}
