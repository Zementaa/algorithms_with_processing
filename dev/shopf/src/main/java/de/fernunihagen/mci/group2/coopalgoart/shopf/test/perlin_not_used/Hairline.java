package de.fernunihagen.mci.group2.coopalgoart.shopf.test.perlin_not_used;

import processing.core.PVector;

public class Hairline {

    PerlinNoise noise;
    PVector position;
    PVector speed;
    PVector acceleration;
    PVector force;
    int maxSpeed = 2;

    Hairline (PerlinNoise noise){
       this.noise = noise; 
       this.position = new PVector(noise.random(noise.width), noise.random(noise.height));
       this.speed = new PVector(0,0);
       this.acceleration = new PVector(0,0);
      
    }
    
    

    
    void move (){
    speed.add(acceleration);
    speed.limit(maxSpeed);
    position.add(speed);
    acceleration.mult(0);
  }


  //sucht den Vektor im Grid, der meiner Position am nächsten ist und nimmt sich diese Kraft
  void searchVector (PVector[] vectors){
    int x = noise.floor(this.position.x/noise.scale);
    if(x == noise.cols){
      x = x-1;
    }
    int y = noise.floor(this.position.y/noise.scale);
    if(y == noise.cols){
      y = y-1;
    }
    int index = x + y * noise.cols;
    force = vectors[index];
    acceleration.add(force);
    
  }

  void shapes () {
    //stroke(0, 5);
    
    noise.colorMode(noise.HSB, 360);
      noise.stroke(noise.frameCount*0.1f%360, 360, 360, 8);
      noise.strokeWeight(4);
     noise.ellipse(position.x, position.y, 1, 1);
  }

  void edges () {
    if (this.position.x > noise.width){
      this.position.x = 0;
    }
    if (this.position.x < 0){
      this.position.x = noise.width;
    }
    if (this.position.y > noise.height){
      this.position.y = 0;
    } 
    if (this.position.y < 0){
      this.position.y = noise.height;
    }
  }
    
}
