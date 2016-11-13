package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

public class TestAgent extends OwnAgent implements Agent{

	int tag = 0;
	
	public TestAgent(){
		super("TestAgent");
	}
	public void reset(){
		jumpState = ACCEPT;
	    action = new boolean[Environment.numberOfKeys];
	    action[Mario.KEY_RIGHT] = false;
	    action[Mario.KEY_SPEED] = false;
	    tag = 0;
	}
	public boolean[] getAction(){
		speedProcedure();
//		System.out.println("dpy:"+senses.catchSafetyCol()[9]);
//		System.out.println("tick:"+brain.fallTickToDPy(senses.catchSafetyCol()[9]));
		if(jumpState==ACCEPT && isMarioAbleToJump){
			if(tag==0){
				jump(8);
				System.out.println(totalTick);
			}
		}
		
/*		if(actionTime>=20){
			hrzControl(tick,aim-(int)marioFloatPos[0]);
			tick--;
			if(tick<0) tag=1;
		}
*/		
//		System.out.println("Jumpable:"+isMarioAbleToJump+" Ground:"+isMarioOnGround);
//		System.out.println("J:"+action[Mario.KEY_JUMP]+" S:"+action[Mario.KEY_SPEED]);
		
//		System.out.println(speed+" : "+distancePassedCells);
		System.out.println("<"+totalTick+"> "+(marioFloatPos[0])+"("+(int)(marioFloatPos[0]/16)+") / "
							+(marioFloatPos[1])+"("+(int)(marioFloatPos[1]/16)+")");

/*		if(distancePassedPhys>=640){
			if(tag==0)System.out.println("##########");
			tag = 1;
		}
*/		
/*		if(jumpState<1){
				int[] safety = senses.catchSafetyCol();
				for(int i=18;i>=0;i--){
					if (safety[i] == senses.DANGER) continue;
					int tick = brain.fallTickToDPy(safety[i]);
					System.out.println("try:"+tick+"/"+(i-9)+"Cell = "+brain.isAbleToMove(tick,brain.px(i-9)));
					if (brain.isAbleToMove(tick,brain.px(i-9))){
						hrzMove(tick,brain.px(i-9));
						break;
					}	
				}
		}
*/		
		jumpProcedure();
		hrzProcedure();
		totalTick ++;
		return action;
	}
}
