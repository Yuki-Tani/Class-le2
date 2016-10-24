package ch.idsia.agents.controllers;

public abstract class OwnAgentBrain {
	protected OwnAgent ag;
	protected OwnAgentSenses sn;
	
	
	
	public OwnAgentBrain(OwnAgent agent){
		ag = agent;
	}
	public void connect(OwnAgentSenses senses){
		sn = senses;
	}
	public abstract void prepare();
	public abstract void direction();
}
