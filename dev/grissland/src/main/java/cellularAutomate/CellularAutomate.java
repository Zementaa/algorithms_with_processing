package cellularAutomate;

import java.util.Random;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import processing.core.PGraphics;

/**
 * @author grissland
 *
 */
public class CellularAutomate {

	//creating global variables
	public boolean[] CA;
	boolean active = false;
	
	int size1;
	int dimblocks;
	int ystartpoint;
	long seeder;
	Random r = new Random();
		
	public CellularAutomate(int size1, int dimBlocks, int yPoint, CooperationContext context) {
		this.size1=size1;
		dimblocks = dimBlocks;
		ystartpoint = yPoint;
		
		//get seed for coorperation
		r.setSeed(context.getSeed());
	}
	
	//setup of the CA
	public void setup() {
		CA = new boolean[size1/dimblocks];
		
		//rondomize values for first generation
		for(int i = 0; i < CA.length; i++) {
			if (r.nextDouble() < 0.5) {
				CA[i] = false;
			} else {
				CA[i] = true;
			}
		}
	}
	
	//creating new line in CA with pixel
	public void showCA(PGraphics pg) {
		int strokeColor = 1 + r.nextInt();
		for(int i = 0; i < CA.length; i++) {
			if (r.nextDouble() < 0.5) {
				pg.fill(0);
				pg.stroke(0);
			} else {
				pg.noFill();
				pg.noStroke();
			}
			if (i%2 == 0) {
				pg.stroke( strokeColor * 2);
			} else {
				pg.stroke( strokeColor + ystartpoint);
			}
			
			pg.rect(i*dimblocks, this.ystartpoint, dimblocks-1, dimblocks-1);
		}
		
	}
	
	//setting the condition for every pixel in the next line 
	public void nextgen() {
		boolean[] CaNext = new boolean[size1/dimblocks];
		for(int i = 0; i < CA.length; i++) {
			boolean left = CA[((i-1)+CA.length)%CA.length];
			boolean center = CA[i];
			boolean right = CA[(i+1)%CA.length];
			
			//rule 30
			if(left) {
				if(center) {
					if(right) {
						//111
						CaNext[i] = false;
					} else {
						//110
						CaNext[i] = false;
					}
				} else {
					if(right){
						//101
						CaNext[i] = false;
					} else {
						//100
						CaNext[i] = true;
					}
					
				}
			}else {if(center) {
				if(right) {
					//011
					CaNext[i] = true;
				} else {
					//010
					CaNext[i] = true;
				}
			} else {
				if(right){
					//001
					CaNext[i] = true;
				} else {
					//000
					CaNext[i] = false;
				}
				
			}
			
			
			}
		}
		//override the old CA
			this.CA = CaNext;
			this.ystartpoint += this.dimblocks;
	}
}
