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
	
//	abstract methods
	public abstract void prepare();
	public abstract void direction();
	
// common methods
	protected int px(int cell){
		if(cell>0){
			return cell*16-(int)ag.marioFloatPos[0]%16;
		}else if(cell<0){
			return cell*16+(16-(int)ag.marioFloatPos[0]%16);
		}else{
			return 0;
		}
	}
	
	private int WIDTH_ERROR = 5;
	
	protected boolean isAbleToMove(int tick,int dPx){
		float dPxMax = ag.speedX*0.89f*OwnAgent.X_EFFECT[tick] +OwnAgent.X_12_ADDITION[tick];
		float dPxMinusMax = ag.speedX*0.89f*OwnAgent.X_EFFECT[tick] -OwnAgent.X_12_ADDITION[tick];
		return dPxMinusMax-WIDTH_ERROR<=dPx && dPx<=dPxMax+WIDTH_ERROR;
	}
	
	protected int fallTickToDPy(int dPy){
		if (dPy<0) return -1;
		float ay = ag.speedY;
		int tick = 0;
		while(dPy>0){
			ay = ay*0.85f+3.0f;
			dPy -= ay;
			tick ++;
		}
		return tick;
	}
	
	protected int jumpTickToMaxCell(int jumpSize){
		switch(jumpSize){
		case 1: return 6; //1
		case 2: return 7; //2
		case 3: return 8;
		case 4: return 8; //3
		case 5: return 9;
		case 6: return 10;
		case 7: return 8; //4
		case 8: return 9;
		}
		return -1;
	}
	/*private int jumpTickToMaxPhy(int jumpSize){
		switch(jumpSize){
		case 1: return 4;
		case 2: return 5;
		case 3: return 5;
		case 4: return 6;
		case 5: return 6;
		case 6: return 7;
		case 7: return 8;
		case 8: return 8;
		}
		return -1;
	}*/
	private int jump8TickToCellOf(int dCy){
		switch(dCy){
		case 0:	return 16;
		case 1: return 15;
		case 2: return 14;
		case 3: return 12;
		case 4: return 10;
		}
		return -1;
	}
}
