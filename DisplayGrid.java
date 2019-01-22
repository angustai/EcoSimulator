/* [DisplayGrid.java]
 * A Small program for Display a 2D String Array graphically
 * Author: Mr. Mangat, Edited by Angus Tai
 * Date: May 1st 2018
 */

// Graphics Imports
import javax.swing.*;
import java.awt.*;
import java.awt.image.*; 
import javax.imageio.*; 

class DisplayGrid { 

  private JFrame frame;
  private int maxX,maxY, GridToScreenRatio;
  private Life[][] world;
  private int turnNum;
  private int totalPlants, totalSheep, totalWolves;
  
  DisplayGrid(Life[][] w) { 
    this.world = w;

    
    maxX = Toolkit.getDefaultToolkit().getScreenSize().width;
    maxY = Toolkit.getDefaultToolkit().getScreenSize().height;
    GridToScreenRatio = maxY / (world.length+1);  //ratio to fit in screen as square map
    
    System.out.println("Map size: "+world.length+" by "+world[0].length + "\nScreen size: "+ maxX +"x"+maxY+ " Ratio: " + GridToScreenRatio);
    
    this.frame = new JFrame("Map of World");
    
    GridAreaPanel worldPanel = new GridAreaPanel();
    
    frame.getContentPane().add(BorderLayout.CENTER, worldPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    frame.setVisible(true);
  }
  
  
  public void refresh(int turnNum,int totalPlants, int totalSheep, int totalWolves) { 
    this.turnNum = turnNum;
    this.totalPlants = totalPlants;
    this.totalSheep = totalSheep;
    this.totalWolves = totalWolves;
    frame.repaint();
  }
  
  
  
  class GridAreaPanel extends JPanel {
    public void paintComponent(Graphics g) {   
      Font font = new Font("Segoe UI Semibold", Font.BOLD, 34);
      Image sheep = Toolkit.getDefaultToolkit().getImage("sheep.png");
      Image wolf = Toolkit.getDefaultToolkit().getImage("wolf.png");
      Image plant = Toolkit.getDefaultToolkit().getImage("plant.png");
      g.setFont(font);
      g.drawString("Turn Number: "+turnNum,1400,200);
      g.drawString("Number of Plants: "+totalPlants,1400,230);
      g.drawString("Number of Sheep: "+totalSheep,1400,260);
      g.drawString("Number of Wolves: "+totalWolves, 1400, 290);
      
      for(int i = 0; i<world.length;i=i+1)
      { 
        for(int j = 0; j<world.length;j=j+1) 
        { 
          if (world[i][j] instanceof Animal){
          Animal animal = ((Animal)world[i][j]);
          if (world[i][j] instanceof Sheep&&animal.gender()==0){
                         g.setColor(Color.BLUE);
                   g.fillRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
            g.drawImage(sheep,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
          }else if (world[i][j] instanceof Sheep&&animal.gender()==1){
                 g.setColor(Color.RED);
                   g.fillRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
                   g.drawImage(sheep,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
          }else if (world[i][j] instanceof Wolf&&animal.gender()==0){
            g.setColor(Color.BLUE);
            g.fillRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
            g.drawImage(wolf,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
          }else if (world[i][j] instanceof Wolf && animal.gender()==1){
            g.setColor(Color.RED);
            g.fillRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
            g.drawImage(wolf,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
          }
          }else{
            if (world[i][j] instanceof Plant){
              g.setColor(Color.BLACK);
              g.fillRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
              g.drawImage(plant,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            }else if (world[i][j] == null){ 
              g.setColor(Color.GREEN);  
              g.fillRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
              g.setColor(Color.BLACK);
              g.drawRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
          }
          }
      }
    }
    }
  }//end of GridAreaPanel
  
} //end of DisplayGrid
