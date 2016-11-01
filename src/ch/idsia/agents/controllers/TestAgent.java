package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

public class TestAgent extends OwnAgent implements Agent{
	
	static int actionTime = 0;
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
	}
	public boolean[] getAction(){
		if(jumpState==ACCEPT && isMarioAbleToJump){
			jump(4);
			System.out.println(actionTime);
		}else if(jumpState == 0 || senses.feelLanding()){
			jumpState = ACCEPT;
			jump(false);
		}
//		System.out.println("Jumpable:"+isMarioAbleToJump+" Ground:"+isMarioOnGround);
		System.out.println("J:"+action[Mario.KEY_JUMP]+" S:"+action[Mario.KEY_SPEED]);
		if(jumpState>0) jumpState --;
		actionTime ++;
		return action;
	}
}
