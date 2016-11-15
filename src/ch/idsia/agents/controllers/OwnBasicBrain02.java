package ch.idsia.agents.controllers;

import ch.idsia.benchmark.mario.engine.sprites.Mario;


public class OwnBasicBrain02 extends OwnAgentBrain{	
	
	public OwnBasicBrain02(OwnAgent agent){
		super(agent);
	}
		
	@Override
	public void prepare(){
	    ag.right(true);
	    ag.speed(true);
	}

	private final int VIEWPOINT = +6 ;
	
	@Override
	public void direction(){
		ag.left(false);
		ag.speed(true);
		ag.right(true);
		//JUMP
		if(ag.jumpState == OwnAgent.ACCEPT && ag.isMarioAbleToJump && ag.isMarioOnGround){
			ag.jump(false);
			//enemy
				for(int i=1;i<=VIEWPOINT;i++){
					if(sn.catchEnemy(ag.marioEgoRow,ag.marioEgoCol+i)){
						if(ag.rideOn(i,1)) break;
					}
				}
//				System.out.println("wall:"+"("+dCxAim+","+height+") ");
			
			//obstacle
			int height=-1;
			for(int len=VIEWPOINT ;len>0;len--){
				int dCxAim = 0;
				height = -1;
				for(int i=1;i<=len;i++){
					int wallHeight = sn.catchWall(ag.marioEgoRow,ag.marioEgoCol+i); 
					if(height<wallHeight){
						height = wallHeight;
						dCxAim = i;
					}
				}
//				System.out.println("wall:"+"("+dCxAim+","+height+") ");
				if(ag.rideOn(dCxAim,height)){
					if(sn.isDangerFromLeft(-height+ag.marioEgoRow,dCxAim+ag.marioEgoCol)){
						ag.jump(5); ag.hrzMove(2,-48);
					}
					break;
				}
			}
			
			int wall1 = sn.catchWall(ag.marioEgoRow,ag.marioEgoCol+1);
			System.out.println("wall1: "+wall1);
			if(wall1>4){
				ag.setBrain(new OwnUpwardBrain(ag));
				return;
			}else{
				for(int i=wall1-1;i>0;i--){
					if(!sn.catchBlank(ag.marioEgoRow-i,ag.marioEgoCol)){
						System.out.println("oh");
						OwnUpwardBrain newBrain = new OwnUpwardBrain(ag);
						ag.setBrain(newBrain);
						newBrain.setBan((int)ag.marioFloatPos[1]/16,(int)ag.marioFloatPos[0]/16);
					}
				}
			}
			//hole
			int[] hole = sn.catchHole(ag.marioEgoRow+1,ag.marioEgoCol+1);
//			System.out.print("hole:"+hole[0]+":"+hole[1]+":"+hole[2]+" ");
			if(hole[0]>4){
				ag.jump(8);
			}else if(hole[1]>0){
				ag.jump(2);
			}
			
		}else{
			if(ag.jumpState<1){
				int[] safety = sn.catchSafetyCol();
				for(int i=18;i>=0;i--){
					if (safety[i] == sn.DANGER) continue;
					int tick = fallTickToDPy(safety[i]);
//					System.out.println("try:"+tick+"/"+(i-9)+"Cell = "+isAbleToMove(tick,px(i-9)));
					if (isAbleToMove(tick,px(i-9))){
						ag.hrzMove(tick,px(i-9));
						break;
					}	
				}
			}	
			
		}
//		System.out.println("<"+ag.totalTick+"> "+(ag.marioFloatPos[0])+"("+(int)(ag.marioFloatPos[0]/16)+") / "
//				+(ag.marioFloatPos[1])+"("+(int)(ag.marioFloatPos[1]/16)+")");
//		System.out.println(ag.action[Mario.KEY_JUMP]+"/"+ag.action[Mario.KEY_SPEED]);
	}
}
