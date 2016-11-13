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
	public int catchWall(int row,int col){
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
	
	public boolean catchObstacleOn(int row,int col){
		switch(ag.getReceptiveFieldCellValue(row,col)){
		case GeneralizerLevelScene.BRICK:
		case GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH:
		case GeneralizerLevelScene.FLOWER_POT_OR_CANNON:
		case GeneralizerLevelScene.LADDER:
		case GeneralizerLevelScene.BORDER_HILL:
			return true;
		default :
			return false;
		}	
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
		hole[2] = catchWall(row-1,col +hole[1]+1); 
		return hole;
	}
	
	public boolean isDangerStay(int row,int col){
		if(catchEnemy(row,col-1) ||	catchEnemy(row,col+1)) return true;
		for(int i=1;i<=3;i++){
			if(catchEnemy(row-i,col) || catchEnemy(row-i,col+1)) return true;
		}
		return false;
		
	}
	
	public boolean isDangerOn(int row,int col){
		if(catchUntouchableEnemy(row,col)) return true;
		if(catchStepableEnemy(row,col)) return false;  
		return isDangerStay(row,col);	
	}
	
	public boolean isDangerFromLeft(int row, int col){
		if(catchEnemy(row,col)) return true;
		return isDangerStay(row,col);
	}
	
	public static final int DANGER = -99;
	
	// return [length or DENGER]
	public int[] catchSafetyCol(){
		int[] safetyCol = new int[19];
		for(int col=0;col<=18;col++){
			r:for(int j=1;j<=9;j++){
				int row = j+ag.marioEgoRow;
				if(catchObstacleOn(row,col) || 
						(catchEnemy(row,col) && catchObstacleOn(row+1,col))){
					if(isDangerOn(row-1,col)){
						safetyCol[col] = DANGER;
					}else{
						safetyCol[col] = (j-1)*16
								+(16-(int)ag.marioFloatPos[1]%16)-1;
					}
					break r;	
				}
				safetyCol[col] = DANGER;
			}
		}
		return safetyCol;
	}
	
	public boolean catchStand(int row,int col){
		switch(ag.getReceptiveFieldCellValue(row,col)){
		case GeneralizerLevelScene.BRICK:
		case GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH:
		case GeneralizerLevelScene.FLOWER_POT_OR_CANNON:
		case GeneralizerLevelScene.BORDER_HILL:
			return true;
		default: 
			return false;
		}
	}
	public boolean catchBlank(int row,int col){
		switch(ag.getReceptiveFieldCellValue(row,col)){
		case GeneralizerLevelScene.BRICK:
		case GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH:
		case GeneralizerLevelScene.FLOWER_POT_OR_CANNON:
			return false;
		case GeneralizerLevelScene.BORDER_HILL: 
		default: 
			return true;		}
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
	public boolean catchStepableEnemy(int row,int col){
		switch(ag.getEnemiesCellValue(row,col)){
			case Sprite.KIND_BULLET_BILL:
			case Sprite.KIND_GOOMBA:
			case Sprite.KIND_GOOMBA_WINGED:
			case Sprite.KIND_GREEN_KOOPA:
			case Sprite.KIND_GREEN_KOOPA_WINGED:
			case Sprite.KIND_RED_KOOPA:
			case Sprite.KIND_RED_KOOPA_WINGED:
			return true;
		default: return false;
		}
	}
	public boolean catchUntouchableEnemy(int row,int col){
		switch(ag.getEnemiesCellValue(row,col)){
		case Sprite.KIND_ENEMY_FLOWER:
		case Sprite.KIND_GREEN_MUSHROOM:
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
