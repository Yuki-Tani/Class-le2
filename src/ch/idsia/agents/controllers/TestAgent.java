package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

public class TestAgent extends OwnAgent implements Agent{
	
	OwnAgentSenses senses;
	
	public TestAgent(){
		super("TestAgent");
	}
	public void reset(){
	    action = new boolean[Environment.numberOfKeys];
	    action[Mario.KEY_RIGHT] = true;
	    action[Mario.KEY_SPEED] = true;
	}
	public boolean[] getAction(){
		System.out.println("J"+action[Mario.KEY_JUMP]+" S"+action[Mario.KEY_SPEED]);
		return action;
	}
}
