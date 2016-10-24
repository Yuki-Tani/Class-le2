package ch.idsia.agents.controllers;

public class OwnBasicBrain01 extends OwnAgentBrain{
	private static final int ACCEPT = OwnAgent.ACCEPT;
	
	
	public OwnBasicBrain01(OwnAgent agent){
		super(agent);
	}
	@Override
	public void prepare(){
	    ag.right(true);
	    ag.dash(true);
	}
	@Override
	public void direction(){
		//JUMP
		if(ag.jumpState > 0){
			ag.jumpState --;
			if(ag.jumpState == 0 || sn.feelLanding()){
				ag.jump(false);
				ag.jumpState = ACCEPT;
			}
		}else if(ag.jumpState == ACCEPT && ag.isMarioAbleToJump && ag.isMarioOnGround){
			ag.dash(true);
			ag.jump(false);
			//obstacle
			int height = 0;
			for(int i=1;i<=3;i++){
				height = Math.max(height,sn.catchObstacle(ag.marioEgoRow,ag.marioEgoCol+i));
			}
//			System.out.print("wall:"+height+" ");
			switch(height){
			case 0:	break;
			case 1: ag.jump(1);		break;
			case 2: ag.jump(2);		break;
			case 3: ag.jump(4);		break;
			case 4: ag.jump(5);		break;
			default: ag.jump(5);
			} 
			if(sn.catchEnemy(ag.marioEgoRow,ag.marioEgoCol+1)||
								sn.catchEnemy(ag.marioEgoRow,ag.marioEgoCol+2)){
				ag.jump(1);
			}
			//hole
			int[] hole = sn.catchHole(ag.marioEgoRow+1,ag.marioEgoCol+1);
//			System.out.print("hole:"+hole[0]+":"+hole[1]+":"+hole[2]+" ");
			if(hole[0]>4){
				ag.jump(4);
			}else if(hole[1]<=0){
			}else if(hole[1]<=3){
				ag.jump(1);
				if(hole[2]>=1){
					ag.jump(2);
				}
				for(int i=5;i<=6;i++){
					if(sn.catchHole(ag.marioEgoRow,ag.marioEgoCol+i)[0]>4){
						ag.dash(false);
					}
				}
			}
		}
//		System.out.println(ag.action[Mario.KEY_JUMP]+"/"+ag.action[Mario.KEY_SPEED]);
	}
}
