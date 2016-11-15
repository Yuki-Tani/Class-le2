package ch.idsia.agents.controllers;

import java.awt.Point;
import java.util.HashSet;

public class OwnUpwardBrain extends OwnAgentBrain{
	private static final int NOMAL = 0,
							 RUN_UP = 1,
							 GO_OUT = 2;
	
	private int state;

	public OwnUpwardBrain(OwnAgent agent) {
		super(agent);
	}

	@Override
	public void prepare() {
		state = NOMAL;
		banCell = new HashSet<Point>();
	}

	@Override
	public void direction() {
//		System.out.println("<"+ag.totalTick+"> "+(ag.marioFloatPos[0])+"("+(int)(ag.marioFloatPos[0]/16)+") / "
//				+(ag.marioFloatPos[1])+"("+(int)(ag.marioFloatPos[1]/16)+")");
		if(isBan((int)ag.marioFloatPos[1]/16,(int)ag.marioFloatPos[0]/16)){
			state = GO_OUT; 
			aimPx = (-1 + ag.distancePassedCells)*16+8;
		}else{
			state = NOMAL;
		}
		System.out.println(state);
		switch(state){
		case NOMAL: nomalRoutine();	break;
//		case RUN_UP: runUpRoutine(); break;
		case GO_OUT: goOutRoutine(); break;
		}	
	}
	
	public HashSet<Point> banCell;
	
	public void setBan(int cy,int cx){
		banCell.add(new Point(cx,cy));
	}
	public boolean isBan(int cy,int cx){
		return banCell.contains(new Point(cx,cy));
	}
	
// routine methods	
	private int aimPx;
	
	private void nomalRoutine(){
		if(ag.jumpState == OwnAgent.ACCEPT && ag.isMarioAbleToJump && ag.isMarioOnGround){
			for(int row=-4;row<0;row++){
				for(int col=5;col>=-5;col--){
					if(sn.catchBlank(ag.marioEgoRow+row,ag.marioEgoCol+col) 
					    && sn.catchStand(ag.marioEgoRow+row+1,ag.marioEgoCol+col)
						&& !isBan((int)ag.marioFloatPos[1]/16+row,(int)ag.marioFloatPos[0]/16+col)){
							aimPx = (col + ag.distancePassedCells)*16+8;
							ag.jump(8);  
							ag.hrzControl(1,aimPx-ag.distancePassedPhys);
							return;
					}
				}
			}
			ag.hrzControl(1,0);
			ag.removeBrain();
			ag.setBrain(new OwnDownwardBrain(ag));
		}else{
			ag.hrzControl(1,aimPx-ag.distancePassedPhys);
		}
	}
	
	private void goOutRoutine(){
		ag.hrzControl(1,aimPx-ag.distancePassedPhys);
	}
	
	/*
	private static final int BACK_TICK = 10;
	private int phase = 0;
	private int nowCx = 0;
	private int dCx = 0;
	private int tickLeft = -1;
	
	private void setRunUp(){
		phase = 0;
	}
	
	private void runUpRoutine(){
		if(phase == 0){ 
			nowCx = ag.distancePassedCells;
			int backPx = (dCx>0)? -ag.distancePassedPhys%16
								 : 16 - ag.distancePassedPhys%16;
			ag.hrzMove(BACK_TICK,backPx);
			tickLeft = BACK_TICK;
			phase = 1;
			System.out.println("backPx("+dCx+"//"+ag.distancePassedCells+") "+backPx);
		}else if(phase == 1){
			tickLeft --;
			if(tickLeft==0){
				tickLeft = -1;
				phase = 2;
			}
		}else if(phase == 2){
			System.out.println("dCx:"+dCx);
			if(dCx>0){
				if(ag.isMarioOnGround && nowCx+dCx<=ag.distancePassedCells){
					state = NOMAL;
					ag.hrzControl(1,0);
					return;
				}
				if(ag.distancePassedPhys>=nowCx*16+16){
					ag.jump(8); ag.hrzControl(1,(nowCx+dCx)*16-ag.distancePassedPhys);
					return;
				}else{
					ag.right(true);
				}
			}else{
				if(ag.isMarioOnGround && nowCx+dCx>=ag.distancePassedCells){		
					state = NOMAL;
					ag.hrzControl(1,0);
					return;
				}
				if(ag.distancePassedPhys<=nowCx*16){
					ag.jump(8); ag.hrzControl(1,(nowCx+dCx)*16+16-ag.distancePassedPhys);
					return;
				}else{
					ag.left(true);
				}
			}
			ag.speed(true);
		}
	}
	*/
}
