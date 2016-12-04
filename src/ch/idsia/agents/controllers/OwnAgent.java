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

import java.util.LinkedList;

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

protected LinkedList<OwnAgentBrain> brainStack;
protected OwnAgentBrain brain;	
protected OwnAgentSenses senses;

protected static final int ACCEPT = -1;
protected static final int MAX_FUTURE_VISION = 50;
protected static float[] X_EFFECT;	// coefficient of speed in i ticks 
protected static float[] X_12_ADDITION;	// max addition in i ticks

protected int totalTick;

public OwnAgent(){this("OwnAgent");}
public OwnAgent(String name){
    super(name);
    brainStack = new LinkedList<OwnAgentBrain>();
    brain = new OwnBasicBrain02(this);
    senses = new OwnAgentSenses(this);
    setBrain(brain);
 
    setFixedNums();    
    reset();
}

public void reset(){
	action = new boolean[Environment.numberOfKeys];
	totalTick = 0;
	jumpState = ACCEPT;
	speedX = 0; speedY = 0;
	stockPx = 0; stockPy = 0;
	brain.prepare();
}

public boolean[] getAction(){
	brainProcedure();
	speedProcedure();
	brain.direction();
	totalTick ++;
	jumpProcedure();
	hrzProcedure();
    return action;
}

//procedure methods


public void setBrain(OwnAgentBrain brain){
	brain.connect(senses);
	brain.prepare();
	brainStack.addFirst(brain);
}
public void removeBrain(){
	if(brainStack.size()==1) return;
	brainStack.pollFirst();
}
protected void brainProcedure(){
	brain = brainStack.peekFirst();
}

protected float speedX;
protected float speedY;
protected float stockPx;
protected float stockPy;

protected void speedProcedure(){
	speedX = marioFloatPos[0] - stockPx;
	stockPx = marioFloatPos[0];
	speedY = marioFloatPos[1] - stockPy;
	stockPy = marioFloatPos[1];
}

protected int jumpState;

protected boolean jumpProcedure(){
	boolean fin = jumpState <= 0 || senses.feelLanding() || speedY>0;
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
	
	int tick = brain.jumpTickToMaxCell(jumpSize);
	
	int dPx = dCx*16 - (int)marioFloatPos[0]%16;
	if(!brain.isAbleToMove(tick,dPx)) return false;
//	System.out.println("rideOn::("+dCx+","+dCy+")");
	jump(jumpSize);
	hrzMove(tick,dPx);
	return true;
}

protected void jump(int jumpSize){
	if(isMarioAbleToJump){
		jumpState = jumpSize;
//		System.out.println("jump:"+jumpSize);
	}	
}
protected void hrzMove(int tickLeft, int dPx){
	this.tickLeft = tickLeft;
	this.aimPhx = (int)marioFloatPos[0]+dPx;
//	System.out.println("hrzMove:"+tickLeft+"tick_"+dPx);
}

protected void hrzControl(int tick,int dPx){
	if(tick<0) return;
	right(false); left(false); speed(false);
	float block = X_12_ADDITION[tick]/4.0f;
	float x0All = speedX*0.89f*X_EFFECT[tick];
//	System.out.println(x0All+"(b:"+block+") -> "+dPx);
	if(x0All<=dPx){
		if(x0All+block+0.5<dPx) right(true);
		if(x0All+block*2<dPx) speed(true); 
	}else{
		if(dPx<x0All-block-0.5) left(true);
		if(dPx<x0All-block*2) speed(true);
	}
}

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

}