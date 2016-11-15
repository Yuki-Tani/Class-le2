package ch.idsia.agents.controllers;

public class OwnDownwardBrain extends OwnAgentBrain{
	
	
	public OwnDownwardBrain(OwnAgent agent){
		super(agent);
	}

	@Override
	public void prepare() {

	}

	@Override
	public void direction() {
		ag.left(false);
		ag.right(true);
		if(ag.marioFloatPos[1]/16>10)
			ag.removeBrain();
	}
}
