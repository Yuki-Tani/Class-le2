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
 * Coded by Yuki Tani
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Apr 8, 2009
 * Time: 4:03:46 AM
 */
public class OwnAgent extends BasicMarioAIAgent implements Agent{
	
private OwnAgentBrain brain;	
private OwnAgentSenses senses;

protected static final int ACCEPT = -1;
protected static final int MAX_FUTURE_VISION = 50;
protected static float[] X_EFFECT;	// coefficient of speed in i ticks 
protected static float[] X_12_ADDITION;	// max addition in i ticks

protected int totalTick;

public OwnAgent(){this("OwnAgent");}
public OwnAgent(String name){
    super(name);
    brain = new OwnBasicBrain02(this);
    senses = new OwnAgentSenses(this);
    brain.connect(senses);
 
    setFixedNums();    
    reset();
}

public void reset(){
	action = new boolean[Environment.numberOfKeys];
	totalTick = 0;
	jumpState = ACCEPT;
	speed = 0;
	stockPhy = 0;
	brain.prepare();
}

public boolean[] getAction(){
	speedProcedure();
	brain.direction();
	totalTick ++;
	jumpProcedure();
	hrzProcedure();
    return action;
}

//procedure methods
protected float speed;
protected float stockPhy;

protected void speedProcedure(){
	speed = marioFloatPos[0] - stockPhy;
	stockPhy = marioFloatPos[0];
}

protected int jumpState;

protected boolean jumpProcedure(){
	boolean fin = jumpState <= 0 || senses.feelLanding();
	if(fin){
		jump(false);
		jumpState = ACCEPT;
		return false;
	}else{
		jump(true);
		jumpState --;
		return true;
	}
}

protected int tickLeft = ACCEPT;
protected int aimPhx = ACCEPT;

protected boolean hrzProcedure(){
	if(tickLeft == 0){
		aimPhx = ACCEPT; tickLeft = ACCEPT;
		right(false); left(false);
	}
	if(aimPhx == ACCEPT || tickLeft == ACCEPT){
		return false;
	}
	hrzControl(tickLeft,aimPhx-(int)marioFloatPos[0]);
	tickLeft --;
	return true;
}

//control methods
protected boolean rideOn(int dCx,int dCy){
	int jumpSize;
	if(dCy<=0 || jumpState != ACCEPT || !isMarioAbleToJump) return false;
	else if(dCy==1) jumpSize=1;
	else if(dCy==2) jumpSize=2;
	else if(dCy==3) jumpSize=4;
	else if(dCy==4) jumpSize=7;
	else return false;
	
	int tick = jumpTickToMaxCell(jumpSize);
	float dPxMax = speed*0.89f*X_EFFECT[tick] +X_12_ADDITION[tick];
	int dPx = dCx*16 - (int)marioFloatPos[0]%16;
	System.out.println(dPx+"::"+dPxMax);
	if(dPx>dPxMax) return false;
	
	System.out.println("rideOn::("+dCx+","+dCy+")");
	jump(jumpSize);
	hrzMove(tick,dPx);
	return true;
}

protected void jump(int jumpSize){
	if(isMarioAbleToJump){
		jumpState = jumpSize;
		System.out.println("jump:"+jumpSize);
	}	
}
protected void hrzMove(int tickLeft, int dPx){
	this.tickLeft = tickLeft;
	this.aimPhx = (int)marioFloatPos[0]+dPx;
	System.out.println("hrzMove:"+tickLeft+"tick_"+dPx);
}

protected void hrzControl(int tick,int dPx){
	if(tick<0) return;
	right(false); left(false); speed(false);
	float block = X_12_ADDITION[tick]/4.0f;
	float x0All = speed*0.89f*X_EFFECT[tick];
//	System.out.println(x0All+"(b:"+block+") -> "+dPx);
	if(x0All<=dPx){
		if(x0All+block+0.5<dPx) right(true);
		if(x0All+block*2<dPx) speed(true); 
	}else{
		if(dPx<x0All-block-0.5) left(true);
		if(dPx<x0All-block*2) speed(true);
	}
}
/*protected void move(int speed){
	if(speed>0){
		if(this.speed>speed){
			right(false); left(false); speed(false);
		}else{
			right(true); left(false); speed(true);
		}
	}else if(speed<0){	
		if(this.speed<speed){
			right(false); left(false); speed(false);
		}else{
			right(false); left(true); speed(true);
		}
	}else if(speed == 0){
		if(this.speed>speed){
			right(false); left(true); speed(true);
		}else if(this.speed<speed){
			right(true); left(false); speed(true);
		}else{
			right(false); left(false); speed(false);
		}
	}
}*/
protected void jump(boolean push){
	action[Mario.KEY_JUMP] = push;
}
protected void right(boolean push){
	action[Mario.KEY_RIGHT] = push;
}
protected void left(boolean push){
	action[Mario.KEY_LEFT] = push;
}
protected void speed(boolean push){
	action[Mario.KEY_SPEED] = push;
}
protected void down(boolean push){
	action[Mario.KEY_DOWN] = push;
}

// private methods
private void setFixedNums(){
	X_EFFECT = new float[MAX_FUTURE_VISION+1];
	X_12_ADDITION = new float[MAX_FUTURE_VISION+1];
	X_EFFECT[0] = 1.0f;
	for(int i=1;i<=MAX_FUTURE_VISION;i++){
	    X_EFFECT[i] = X_EFFECT[i-1]*0.89f;
	}
	for(int i=0;i<=MAX_FUTURE_VISION;i++){
	    X_EFFECT[i]= (1.0f-X_EFFECT[i])/0.11f;
//	    System.out.println(i+": "+X_EFFECT[i]);
	}
	X_12_ADDITION[0] = 0;
	for(int i=1;i<=MAX_FUTURE_VISION;i++){
		X_12_ADDITION[i] = X_12_ADDITION[i-1] + 1.2f*X_EFFECT[i];
//		System.out.println("max"+i+": "+X_12_ADDITION[i]);
	}
//	System.out.println("->"+X_12_ADDITION[50]);
}
private int jumpTickToMaxCell(int jumpSize){
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
private int jumpTickToMaxPhy(int jumpSize){
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
}

}