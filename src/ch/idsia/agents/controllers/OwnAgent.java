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
	
private OwnAgentBrain brain;	
private OwnAgentSenses senses;

protected static final int ACCEPT = -1;
protected int jumpState;
protected int speed;
protected int stockPhy;


public OwnAgent(){this("OwnAgent");}
public OwnAgent(String name){
    super(name);
    brain = new OwnBasicBrain02(this);
    senses = new OwnAgentSenses(this);
    brain.connect(senses);
    reset();
}

public void reset(){
	action = new boolean[Environment.numberOfKeys];
	jumpState = ACCEPT;
	speed = 0;
	stockPhy = 0;
	brain.prepare();
}

public boolean[] getAction(){
	speedProcedure();
	brain.direction();
	if(jumpState>0) jumpState --;
    return action;
}

//procedure methods
protected void speedProcedure(){
	speed = distancePassedPhys - stockPhy;
	stockPhy = distancePassedPhys;
}
protected boolean jumpProcedure(){
	boolean during = jumpState == 0 || senses.feelLanding();
	if(during){
		jump(false);
		jumpState = ACCEPT;
	}
	return during;
}

//control methods
protected void jump(boolean push,int jumpSize){
	if(push) jumpState = jumpSize;
	action[Mario.KEY_JUMP] = push;
}
protected void jump(boolean push){jump(push,4);}
protected void jump(int jumpSize){jump(true,jumpSize);}

protected void move(int speed){
	if(speed>0){
		if(this.speed>speed){
			right(false); left(false); dash(false);
		}else{
			right(true); left(false); dash(true);
		}
	}else if(speed<0){	
		if(this.speed<speed){
			right(false); left(false); dash(false);
		}else{
			right(false); left(true); dash(true);
		}
	}else if(speed == 0){
		if(this.speed>speed){
			right(false); left(true); dash(true);
		}else if(this.speed<speed){
			right(true); left(false); dash(true);
		}else{
			right(false); left(false); dash(false);
		}
	}
}
protected void right(boolean push){
	action[Mario.KEY_RIGHT] = push;
}
protected void left(boolean push){
	action[Mario.KEY_LEFT] = push;
}
protected void dash(boolean push){
	action[Mario.KEY_SPEED] = push;
}
protected void squat(boolean push){
	action[Mario.KEY_DOWN] = push;
}
}