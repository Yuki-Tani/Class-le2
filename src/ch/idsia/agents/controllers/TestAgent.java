package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

public class TestAgent extends OwnAgent implements Agent{
	
	static int actionTime = 0;
	static int phy = 0;
	static int cell = 0;
	int tag = 0;
	OwnAgentSenses senses;
	
	public TestAgent(){
		super("TestAgent");
		senses = new OwnAgentSenses(this);
	}
	public void reset(){
		jumpState = ACCEPT;
	    action = new boolean[Environment.numberOfKeys];
	    action[Mario.KEY_RIGHT] = false;
	    action[Mario.KEY_SPEED] = false;
	}
	public boolean[] getAction(){
		speedProcedure();
		if(jumpState==ACCEPT && isMarioAbleToJump){
			jump(7);
			if(tag==0) move(11);
			else move(-11);
			System.out.println(actionTime);
		}
		if(jumpProcedure()){
			if(tag==0) move(11);
		}
//		System.out.println("Jumpable:"+isMarioAbleToJump+" Ground:"+isMarioOnGround);
//		System.out.println("J:"+action[Mario.KEY_JUMP]+" S:"+action[Mario.KEY_SPEED]);
		
		System.out.println(speed+" : "+distancePassedCells);

/*		if(distancePassedPhys>=640){
			if(tag==0)System.out.println("##########");
			tag = 1;
		}
*/
		phy = distancePassedPhys;
		cell = distancePassedCells;
		if(jumpState>0) jumpState --;
		actionTime ++;
		return action;
	}
}
