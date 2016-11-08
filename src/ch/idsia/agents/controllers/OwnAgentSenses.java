package ch.idsia.agents.controllers;

import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;


public class OwnAgentSenses {
	private static final int SMALL =0,LARGE = 1,FIRE = 2;
	
	private BasicMarioAIAgent ag;
	
	public OwnAgentSenses(BasicMarioAIAgent aiAgent){
		ag = aiAgent;
	}
	
	// return height of obstacles
	public int catchObstacle(int row,int col){
		int height = 0;
		int tall;
		if(ag.marioMode==SMALL) tall = 1;
		else tall = 2;
		while(tall>0){
			switch(ag.getReceptiveFieldCellValue(row-height,col)){
			case GeneralizerLevelScene.BRICK:
			case GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH:
			case GeneralizerLevelScene.FLOWER_POT_OR_CANNON:
			case GeneralizerLevelScene.LADDER:
				height ++;
				if(ag.marioMode==SMALL) tall = 1;
				else tall = 2; break;		
			default: 
				height ++;
				tall --;
			}
		}	
		return (ag.marioMode==SMALL)? height-1 : height-2;
	}
	
	// return [depth,length,height]
	public int[] catchHole(int row,int col){
		int MAX_DEPTH = 6;
		int MAX_LENGTH = 5;
		int[] hole = new int[3];
		if(!ag.isMarioOnGround) return hole;
		
		depth:while(hole[0]<MAX_DEPTH){
			switch(ag.getReceptiveFieldCellValue(row+hole[0],col)){
			case GeneralizerLevelScene.BRICK:
			case GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH:
			case GeneralizerLevelScene.FLOWER_POT_OR_CANNON:
			case GeneralizerLevelScene.LADDER:
			case GeneralizerLevelScene.BORDER_HILL:
					break depth;
			default:
					hole[0]++;	
			}
		}
		length:while(hole[1]<MAX_LENGTH){ 
			switch(ag.getReceptiveFieldCellValue(row,col+hole[1])){
			case GeneralizerLevelScene.BRICK:
			case GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH:
			case GeneralizerLevelScene.FLOWER_POT_OR_CANNON:
			case GeneralizerLevelScene.LADDER:
			case GeneralizerLevelScene.BORDER_HILL:
					break length;
			default:
					hole[1]++;	
			}
		}
		hole[2] = catchObstacle(row-1,col +hole[1]+1); 
		return hole;
	}
	
	public boolean catchEnemy(int row,int col){
			switch(ag.getEnemiesCellValue(row,col)){
			case Sprite.KIND_BULLET_BILL:
			case Sprite.KIND_ENEMY_FLOWER:
			case Sprite.KIND_GOOMBA:
			case Sprite.KIND_GOOMBA_WINGED:
			case Sprite.KIND_GREEN_KOOPA:
			case Sprite.KIND_GREEN_KOOPA_WINGED:
			case Sprite.KIND_GREEN_MUSHROOM:
			case Sprite.KIND_RED_KOOPA:
			case Sprite.KIND_RED_KOOPA_WINGED:
			case Sprite.KIND_SPIKY:
			case Sprite.KIND_SPIKY_WINGED:
				return true;
			default: return false;
			}
	}
	
	public boolean feelLanding(){
		return !ag.isMarioAbleToJump && ag.isMarioOnGround;
	}
	
}
