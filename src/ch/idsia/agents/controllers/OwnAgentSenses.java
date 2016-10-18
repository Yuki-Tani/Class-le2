package ch.idsia.agents.controllers;

import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;


public class OwnAgentSenses {
	private BasicMarioAIAgent ai;
	public OwnAgentSenses(BasicMarioAIAgent aiAgent){
		ai = aiAgent;
	}
	public boolean catchObstacle(int row,int col){
		switch(ai.getReceptiveFieldCellValue(row,col)){
		case GeneralizerLevelScene.BRICK:
		case GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH:
		case GeneralizerLevelScene.FLOWER_POT_OR_CANNON:
		case GeneralizerLevelScene.LADDER:
					return true;
		default: 	return false;
		}	
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
