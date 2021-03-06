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

package ch.idsia.scenarios;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.*;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.MarioAIOptions;

/*
 * customized by Yuki Tani for AI programming. 
 */
public final class Main
{
public static final int  	SEED 		= 0,
						  	DIFFICULTY 	= 2,
						  	REPEAT 		= 200,
						  	REPEAT_PLUS = 1;
public static int goCount = 0,
				  winCount = 0;

public static void main(String[] args)
{
    MarioAIOptions marioAIOptions;
    goCount = 0; winCount = 0;
    for(int i=0;i<REPEAT;i++){
    marioAIOptions = new MarioAIOptions(args);

    //ステージパラメータ
    marioAIOptions.setLevelRandSeed(SEED+i*REPEAT_PLUS);
    marioAIOptions.setLevelDifficulty(DIFFICULTY);

  marioAIOptions.setDeadEndsCount(true);		//dead_ends
//  marioAIOptions.setHiddenBlocksCount(true);	//hidden_blocks
//  marioAIOptions.setFlatLevel(true);			//flat
    
//  marioAIOptions.setCoinsCount(false);		//coins
//  marioAIOptions.setHillStraightCount(false);	//hill
//  marioAIOptions.setBlocksCount(false);		//blocks
  marioAIOptions.setTubesCount(false);		//tubes
//  marioAIOptions.setGapsCount(false); 		//gaps
//  marioAIOptions.setCannonsCount(false);		//cannons
    
    //敵の有無  
//  marioAIOptions.setEnemies("off");	//キラーとパックンのみ
  marioAIOptions.setEnemies("g"); 	//クリボー
//  marioAIOptions.setEnemies("g");

  //非表示
  marioAIOptions.setVisualization(false);
    
    // エージェントの追加
    final OwnAgent agent = new OwnAgent();
    marioAIOptions.setAgent(agent); 
    
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.doEpisodes(1,true,1);
    }
    System.out.println();
    System.out.println("Repeat: "+goCount);
    System.out.println("Win   : "+winCount);
    System.exit(0);
}

public static void counter(boolean isWin){
	if(isWin) winCount ++;
	goCount ++;
}

}
