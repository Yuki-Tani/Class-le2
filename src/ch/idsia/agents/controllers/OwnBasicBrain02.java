package ch.idsia.agents.controllers;

import ch.idsia.benchmark.mario.engine.sprites.Mario;


public class OwnBasicBrain02 extends OwnAgentBrain{
	private static final int ACCEPT = OwnAgent.ACCEPT;
	
	
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
		ag.speed(true);
		ag.right(true);
		//JUMP
		if(ag.jumpState == ACCEPT && ag.isMarioAbleToJump && ag.isMarioOnGround){
			ag.jump(false);
			
			//enemy
				for(int i=1;i<=VIEWPOINT;i++){
					if(sn.catchEnemy(ag.marioEgoRow,ag.marioEgoCol+i)){
						if(ag.rideOn(i,1)) break;
					}
				}
//				System.out.println("wall:"+"("+dCxAim+","+height+") ");
			
			//obstacle
			for(int len=VIEWPOINT ;len>0;len--){
				int dCxAim = 0;
				int height = -1;
				for(int i=1;i<=len;i++){
					if(height<sn.catchObstacle(ag.marioEgoRow,ag.marioEgoCol+i)){
						height = sn.catchObstacle(ag.marioEgoRow,ag.marioEgoCol+i);
						dCxAim = i;
					}
				}
//				System.out.println("wall:"+"("+dCxAim+","+height+") ");
				if(ag.rideOn(dCxAim,height)) break;
			}
			
			//hole
			int[] hole = sn.catchHole(ag.marioEgoRow+1,ag.marioEgoCol+1);
//			System.out.print("hole:"+hole[0]+":"+hole[1]+":"+hole[2]+" ");
			if(hole[0]>4){
				ag.jump(8);
			}else if(hole[1]<=0){
			}else if(hole[1]<=3){
				ag.jump(1);
				if(hole[2]>=1){
					ag.jump(2);
				}
				for(int i=5;i<=6;i++){
					if(sn.catchHole(ag.marioEgoRow,ag.marioEgoCol+i)[0]>4){
						ag.speed(false);
					}
				}
			}
			
		}else{
			if(ag.jumpState<1){
				int[] safety = sn.catchSafetyCol();
				for(int i=18;i>=0;i--){
					if (safety[i] == sn.DANGER) continue;
					int tick = fallTickToDPy(safety[i]);
					System.out.println("try:"+tick+"/"+(i-9)+"Cell = "+isAbleToMove(tick,px(i-9)));
					if (isAbleToMove(tick,px(i-9))){
						ag.hrzMove(tick,px(i-9));
						break;
					}	
				}
			}	
			
		}
		System.out.println("<"+ag.totalTick+"> "+(ag.marioFloatPos[0])+"("+(int)(ag.marioFloatPos[0]/16)+") / "
				+(ag.marioFloatPos[1])+"("+(int)(ag.marioFloatPos[1]/16)+")");
//		System.out.println(ag.action[Mario.KEY_JUMP]+"/"+ag.action[Mario.KEY_SPEED]);
	}
}
