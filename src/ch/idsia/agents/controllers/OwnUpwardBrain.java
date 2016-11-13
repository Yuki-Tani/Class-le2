package ch.idsia.agents.controllers;

import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;

public class OwnUpwardBrain extends OwnAgentBrain{
	private static final int NOMAL = 0,
							 RUN_UP = 1;
	
	private int state;

	public OwnUpwardBrain(OwnAgent agent) {
		super(agent);
	}

	@Override
	public void prepare() {
		state = NOMAL;
	}

	@Override
	public void direction() {
//		System.out.println("<"+ag.totalTick+"> "+(ag.marioFloatPos[0])+"("+(int)(ag.marioFloatPos[0]/16)+") / "
//				+(ag.marioFloatPos[1])+"("+(int)(ag.marioFloatPos[1]/16)+")");
		switch(state){
		case NOMAL: nomalRoutine();	break;
//		case RUN_UP: runUpRoutine(); break;
		}	
	}
	
	private int aimPx;
	
	private void nomalRoutine(){
		System.out.println("aimX:"+aimPx/16);
		if(ag.jumpState == OwnAgent.ACCEPT && ag.isMarioAbleToJump && ag.isMarioOnGround){
			for(int row=ag.marioEgoRow-4;row<ag.marioEgoRow;row++){
				for(int col=ag.marioEgoCol+5;col>=ag.marioEgoCol-5;col--){
					if(sn.catchBlank(row, col)&&sn.catchStand(row+1, col)){
						aimPx = (col-ag.marioEgoCol + ag.distancePassedCells)*16+8;
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
			System.out.println("//");
			ag.hrzControl(1,aimPx-ag.distancePassedPhys);
		}
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
