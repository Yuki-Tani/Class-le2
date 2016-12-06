package ch.idsia.agents.controllers;

public class OwnDownwardBrain extends OwnAgentBrain{
	
	private int sameCellX=0;
	private int sameCellCount = 0;
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
		sameCellProcedure();
	}
	
	private void sameCellProcedure(){
		if((int)ag.marioFloatPos[0]/16==sameCellX) sameCellCount ++;
		else sameCellCount = 0;
		if(sameCellCount > 30){
			ag.removeBrain();
		}
		sameCellX = (int)ag.marioFloatPos[0]/16;
	}
}
