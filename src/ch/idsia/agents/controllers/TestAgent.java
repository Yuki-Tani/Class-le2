package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

public class TestAgent extends OwnAgent implements Agent{

	int tag = 0;

	
	OwnAgentSenses senses;
	
	public TestAgent(){
		super("TestAgent");
		senses = new OwnAgentSenses(this);
	}
	public void reset(){
		jumpState = ACCEPT;
	    action = new boolean[Environment.numberOfKeys];
	    action[Mario.KEY_RIGHT] = true;
	    action[Mario.KEY_SPEED] = false;
	    tag = 0;
	}
	public boolean[] getAction(){
		speedProcedure();
		jump(8);
/*		if(jumpState==ACCEPT && isMarioAbleToJump){
			if(tag==1){
				jump(1);
				tick = 16;
				System.out.println(actionTime);
			}
		}
*/		
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
		jumpProcedure();
		hrzProcedure();
		totalTick ++;
		return action;
	}
}
