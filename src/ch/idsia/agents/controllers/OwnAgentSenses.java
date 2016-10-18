package ch.idsia.agents.controllers;

import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;


public class OwnAgentSenses {
	private static final int SMALL =0,LARGE = 1,FIRE = 2;
	private BasicMarioAIAgent ai;
	public OwnAgentSenses(BasicMarioAIAgent aiAgent){
		ai = aiAgent;
	}
	
	public int catchObstacle(int row,int col){
		int height = 0;
		int tall;
		if(ai.marioMode==SMALL) tall = 1;
		else tall = 2;
		while(tall>0){
			switch(ai.getReceptiveFieldCellValue(row-height,col)){
			case GeneralizerLevelScene.BRICK:
			case GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH:
			case GeneralizerLevelScene.FLOWER_POT_OR_CANNON:
			case GeneralizerLevelScene.LADDER:
				height ++;
				if(ai.marioMode==SMALL) tall = 1;
				else tall = 2; break;		
			default: 
				height ++;
				tall --;
			}
		}	
		return (ai.marioMode==SMALL)? height-1 : height-2;
	}
	
	public boolean catchHole(int row,int col,int depth){
		if(!ai.isMarioOnGround) return false;
		for(int i=0;i<depth;i++){
			switch(ai.getReceptiveFieldCellValue(row+i,col)){
			case GeneralizerLevelScene.BRICK:
			case GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH:
			case GeneralizerLevelScene.FLOWER_POT_OR_CANNON:
			case GeneralizerLevelScene.LADDER:
			case GeneralizerLevelScene.BORDER_HILL:
						return false;
			default:
			}
		}
		return true;
	}
	public boolean feelLanding(){
		return !ai.isMarioAbleToJump && ai.isMarioOnGround;
	}
	
}
