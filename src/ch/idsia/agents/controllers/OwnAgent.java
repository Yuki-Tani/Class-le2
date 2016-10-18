/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Mario AI nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Apr 8, 2009
 * Time: 4:03:46 AM
 */

public class OwnAgent extends BasicMarioAIAgent implements Agent{
private static final int ACCEPT = -1;
	
private OwnAgentSenses senses;
private int jumpState;
//private int jumpWdT;

public OwnAgent(){
	
    super("OwnAgent");
    senses = new OwnAgentSenses(this);
    jumpState = ACCEPT;
    reset();
}

public void reset(){
	
    action = new boolean[Environment.numberOfKeys];
    action[Mario.KEY_RIGHT] = true;
    action[Mario.KEY_SPEED] = false;
    jumpState = ACCEPT;
}

public boolean[] getAction(){
	
	//JUMP
	if(jumpState > 0){
		jumpState --;
		if(jumpState == 0 || senses.feelLanding()){
			jump(false);
			jumpState = ACCEPT;
		}
	}else if(jumpState == ACCEPT && isMarioAbleToJump){
		int height = senses.catchObstacle(marioEgoRow,marioEgoCol+1);
		System.out.print(height+" ");
		switch(height){
		case 0: jump(false); 	break;
		case 1: jump(1);		break;
		case 2: jump(2);		break;
		case 3: jump(4);		break;
		case 4: jump(5);		break;
		default: jump(5);
		}
		if(senses.catchHole(marioEgoRow+1,marioEgoCol+1,5)){
			System.out.print("hole ");
			jump(true,5);
		}
	}
	
	System.out.println(action[Mario.KEY_JUMP]);
    return action;
}

//private methods
private void jump(boolean push,int jumpSize){
	if(push) jumpState = jumpSize;
	action[Mario.KEY_JUMP] = push;
}
private void jump(boolean push){jump(push,5);}
private void jump(int jumpSize){jump(true,jumpSize);}
}
/*
 * 10/18
 * 高い壁も、壁がある限りジャンプボタンを離さない
 * 壁際に着地した場合trueのままとなる
 */
