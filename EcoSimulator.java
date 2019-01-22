/* [EcoSimulator.java]
 * Simulates an ecosystem that contains wolves, sheep and plants
 * By: Angus Tai
 * Date: May 1 2018
 */

//SUGGESTED NUMBERS
//Dimensions: 75, Initial Plants: 150, Plants Spawned per Round: 22, Total Sheep: 225, Total Wolves: 4, Health: 100, Health per Plant: 10
import java.util.Scanner;
class EcoSimulator {
  public static void main(String[] args) { 
    Scanner input = new Scanner(System.in);
    Life[][] map;//2d object array of type life
    int dimensions;
    int sheepNum, wolfNum, initialPlantNum,plantNum,healthNum,plantNutrition;
    int totalSheep=0, totalWolves=0, totalPlants=0;
    int y,x;
    int turnNum = 0;
    boolean nullPositions = false;
    boolean sheepAlive = true;
    boolean wolvesAlive = true;
    
    System.out.println ("What are the dimensions of your grid?");
    dimensions = input.nextInt();
    map= new Life[dimensions][dimensions];
    System.out.println ("How many plants do you want to initially start with?");
    initialPlantNum = input.nextInt();
    System.out.println ("How many plants would you like to spawn per round?");
    plantNum = input.nextInt();
    System.out.println ("How many sheep would you like?");
    sheepNum = input.nextInt();
    System.out.println ("How many wolves would you like?");
    wolfNum = input.nextInt();
    System.out.println ("How much health would you like?");
    healthNum = input.nextInt();
    System.out.println("How much nutrition would you like plants to have?");
    plantNutrition = input.nextInt();
    
    
    //creates number of sheep that user entered
    for (int i=0;i<sheepNum;i++){
      do{
        y= ((int)(Math.random()* dimensions));
        x = ((int)(Math.random()* dimensions));
      }while(map[y][x] instanceof Life);
      map[y][x]=new Sheep(healthNum);//new sheep at a position that is null
    }
    
    //creates number of wolves that user entered
    for (int i=0;i<wolfNum;i++){
      do{
        y= ((int)(Math.random()* dimensions));
        x = ((int)(Math.random()* dimensions));
      }while(map[y][x] instanceof Life);
      map[y][x]=new Wolf(healthNum);//new wolf at a position that is null
    }
    
    for (int i=0;i<(initialPlantNum-plantNum);i++){//spawns number of plants that user wants at the beginning
        plantSpawn(map,dimensions,plantNutrition);
      }
    DisplayGrid grid = new DisplayGrid(map);
    try{ Thread.sleep(2500); }catch(Exception e) {};
    
    while(wolvesAlive==true&&sheepAlive==true){//there is still more than one of each species
      grid.refresh(turnNum,totalPlants,totalSheep,totalWolves);
      try{ Thread.sleep(1); }catch(Exception e) {};
      totalPlants=totalSheep=totalWolves=0;
      sheepAlive = wolvesAlive = false;
      turnNum++;
      Animal animal;
      for (int i=0;i<plantNum;i++){//spawns number of plants that user entered each turn 
        plantSpawn(map,dimensions,plantNutrition);
      }
    
      for (int i=0;i<dimensions;i++){
        for(int j=0;j<dimensions;j++){
          nullPositions = false;
          for(int m=0;m<dimensions;m++){
            for(int n=0;n<dimensions;n++){
              if (map[m][n] ==null){//checks if there are empty spaces in the array
                nullPositions = true;
              }
            }
          }
          if (map[i][j] instanceof Animal){//if the space being checked contains an animal
            animal = ((Animal) map[i][j]);
            if (animal instanceof Wolf){//if the animal being checked is a wolf
              if ((i>0&&map[i-1][j] instanceof Sheep) || (i<dimensions-1&&map[i+1][j] instanceof Sheep) || (j>0 &&map[i][j-1] instanceof Sheep) || (j<dimensions-1&&map[i][j+1] instanceof Sheep)){//if there is a sheep adjacent to the wolf
                wolfEat(map,dimensions,i,j,animal);//run wolfEat method
              }
            }
            else if (animal instanceof Sheep){//if the animal being checked is a sheep
              if ((i>0&&map[i-1][j] instanceof Plant) || (i<dimensions-1&&map[i+1][j] instanceof Plant) || (j>0 &&map[i][j-1] instanceof Plant) || (j<dimensions-1&&map[i][j+1] instanceof Plant)){//if there is a plant adjacent to the sheep
                sheepEat(map,dimensions,i,j,animal,plantNutrition);//run sheepEat method
              }
            }
            if (animal.getHealth() >0){//if the animal has positive health
              moveItemsOnGrid(map,dimensions,i,j,animal,nullPositions,plantNutrition);//run moveItemsOnGrid method
            }else{
              map[i][j]=null;//the animal is dead
            }
          }
        }
      }
      
      for (int i=0;i<dimensions;i++){
        for(int j=0;j<dimensions;j++){
          if (map[i][j] instanceof Animal){
            animal = ((Animal) map[i][j]);
            animal.moved = false;//resets "moved" for next turn
            animal.loseHealthPerTurn();//every animal loses one health
          }
          //checks if there is at least one of every organism
          if (map[i][j] instanceof Plant){
            totalPlants+=1;
          }
          if (map[i][j] instanceof Sheep){
            sheepAlive = true;
            totalSheep+=1;
          }
          if (map[i][j] instanceof Wolf){
            wolvesAlive = true;
            totalWolves+=1;
          }
        }
      }
      grid.refresh(turnNum,totalPlants,totalSheep,totalWolves);
    }
  }
  
  /**
   * wolfEat
   * This method makes the wolf smart and always eat the sheep if there is one beside it
   * method is void and does not return anything
   * @param 2D array of objects, integer value of map dimensions, integers i and j represent the position of the object, Animal object
 */
  public static void wolfEat(Life[][]map,int dimensions,int i ,int j, Animal animal){
    boolean exit=false;
    if (animal.moved == false){//if the animal hasn't moved this turn
      do{
        int direction = ((int)(Math.random()* 4));//chooses random number between 0 and 3
        if(i>0 && direction ==0&&animal instanceof Wolf && map[i-1][j] instanceof Sheep){//sheep above animal
          animal.eat(map[i-1][j].getHealth());//wolf's health increases by health of eaten sheep
          map[i-1][j] = map[i][j];//wolf eats sheep
          map[i][j] = null;
          animal.moved();//animal has now been moved
          exit=true;
        }else if (i<dimensions-1 && direction == 1&&animal instanceof Wolf && map[i+1][j] instanceof Sheep){//sheep below wolf
          animal.eat(map[i+1][j].getHealth());
          map[i+1][j] = map[i][j];
          map[i][j] = null;
          animal.moved();
          exit=true;
        }else if (j>0 && direction==2&&animal instanceof Wolf && map[i][j-1] instanceof Sheep){//sheep left of wolf
          animal.eat(map[i][j-1].getHealth());
          map[i][j-1] = map[i][j];
          map[i][j] = null;
          animal.moved();
          exit=true;
        }else if (j<dimensions-1&&direction==3&&animal instanceof Wolf && map[i][j+1] instanceof Sheep){//sheep right of wolf
          animal.eat(map[i][j+1].getHealth());
          map[i][j+1] = map[i][j];
          map[i][j] = null;
          animal.moved();
          exit=true;
        }
      }while(exit==false);//stay in the loop until a wolf eats a sheep
    }
  }
  
  /**
   * sheepEat
   * This method makes the sheep smart and always eat the plant if there is one beside it
   * method is void and does not return anything
   * @param 2D array of objects, integer value of map dimensions, integers i and j represent the position of the object, Animal object, nutrition value of plants
 */
  public static void sheepEat(Life[][]map,int dimensions,int i ,int j, Animal animal,int plantNutrition){
    boolean exit = false;
    if (animal.moved == false){//if the animal hasn't moved this turn
      do{
        int direction = ((int)(Math.random()* 4));//random number between 0 and 3
        if(i>0&&direction==0&&animal instanceof Sheep && map[i-1][j] instanceof Plant){// plant above sheep
          animal.eat(plantNutrition);//sheep's health goes up by nutrition of the plant
          map[i-1][j] = map[i][j];//sheep eats plant and takes its space
          map[i][j] = null;//sheep's original space becomes empty
          animal.moved();//animal has now been moved
          exit=true;
        }else if (i<dimensions-1&&direction==1&&animal instanceof Sheep && map[i+1][j] instanceof Plant){//plant below sheep
          animal.eat(plantNutrition);
          map[i+1][j] = map[i][j];
          map[i][j] = null;
          animal.moved();
          exit=true;
        }else if (j>0&&direction==2&&animal instanceof Sheep && map[i][j-1] instanceof Plant){//plant left of sheep
          animal.eat(plantNutrition);
          map[i][j-1] = map[i][j];
          map[i][j] = null;
          animal.moved();
          exit=true;
        }else if (j<dimensions-1&&direction==3&&animal instanceof Sheep && map[i][j+1] instanceof Plant){//plant right of sheep
          animal.eat(plantNutrition);
          map[i][j+1] = map[i][j];
          map[i][j] = null;
          animal.moved();
          exit=true;
        }
      }while(exit==false);// loop continues as long as the sheep hasn't eaten a plant
    }
  }
  
  /**
   * moveItemsOnGrid
   * This method changes the coordinates of animals based on actions that they can make
   * method is void and does not return anything
   * @param 2D array of objects, integer value of map dimensions, integers i and j represent the position of the object, Animal object, boolean holding whether there are empty positions, nutrition of the plant
 */
  public static void moveItemsOnGrid(Life[][]map,int dimensions,int i, int j,Animal animal,boolean nullPositions,int plantNutrition){
    boolean moveAgain=true;
    int direction;
    int y=-1,x=-1;
    if (nullPositions ==true){//if there is an empty spot
      do{
        y= ((int)(Math.random()* dimensions));
        x = ((int)(Math.random()* dimensions));
      }while(map[y][x] != null);//loops until coordinates are an empty position
    }
    if (animal.moved == false){//animal has not moved this turn
      do{
        moveAgain = true;
        direction = ((int)(Math.random()* 5));
        if ((i>0)&&(direction==0)){//checks space above current animal
          Life above = map[i-1][j];
          if (map[i-1][j]==null){//empty space above current animal
            map[i-1][j] = map[i][j];//animal moves up a space
            map[i][j] = null;
          }else if (nullPositions==true&&animal instanceof Sheep && above instanceof Sheep&&animal.getHealth()>=20&&above.getHealth()>=20&&animal.gender()!=((Animal)above).gender()){//sheep reproduction if both parents are different genders and have more than 20 health
            map[y][x] = new Sheep(20);//spawns new sheep at random null position
            animal.reproduce();//current sheep loses 10 health
            ((Animal)above).reproduce();//sheep above loses 10 health
          }else if (nullPositions ==true&&animal instanceof Wolf && above instanceof Wolf&&animal.getHealth()>=20&&above.getHealth()>=20&&animal.gender()!=((Animal)above).gender()){//wolf reproduction if both parents are different genders and have more than 20 health
            map[y][x] = new Wolf(20);//spawns new wolf at random null position
            animal.reproduce();//current wolf loses 10 health
            ((Animal)above).reproduce();//wolf below loses 10 health
          }else if (animal instanceof Wolf && above instanceof Wolf && animal.gender()==((Animal)above).gender()){//two wolves of the same gender adjacent
            if (((Wolf)animal).compareTo((Wolf)above)>0){
              above.setHealth(above.getHealth()-10);//wolf above loses 10 health
            }else if (((Wolf)animal).compareTo((Wolf)above)<0){
              animal.setHealth(animal.getHealth()-10);//current wolf loses 10 health
            }else{//both wolves have the same health
              animal.setHealth(animal.getHealth()-5);//current wolf loses 5 health
              above.setHealth(above.getHealth()-5);//wolf above loses 5 health
            }
          }else{
            moveAgain = false;
          }
          
          
        }else if ((i<dimensions-1)&&(direction==1)){//checks space below the current animal
          Life below = map[i+1][j];
          if (map[i+1][j]==null){//empty space below animal
            map[i+1][j] = map[i][j];//animal moves down one space
            map[i][j] = null;//original space is empty
          }else if (nullPositions ==true&&animal instanceof Sheep && below instanceof Sheep&&animal.getHealth()>=20&&below.getHealth()>=20&&animal.gender()!=((Animal)below).gender()){//sheep reproduction
            map[y][x] = new Sheep(20);
            animal.reproduce();
            ((Animal)below).reproduce();//sheep in space below loses 10 health
          }else if (nullPositions ==true&&animal instanceof Wolf && below instanceof Wolf&&animal.getHealth()>=20&&below.getHealth()>=20&&animal.gender()!=((Animal)below).gender()){//wolf reproduction
            map[y][x] = new Wolf(20);
            animal.reproduce();
            ((Animal)below).reproduce();//wolf in space below loses 10 health
          }else if (animal instanceof Wolf && below instanceof Wolf && animal.gender()==((Animal)below).gender()){//wolves attack each other
            if (((Wolf)animal).compareTo((Wolf)below)>0){//wolf in current space has more health than wolf below
              below.setHealth(below.getHealth()-10);
            }else if (((Wolf)animal).compareTo((Wolf)below)<0){//wolf in current space has less health than wolf below
              animal.setHealth(animal.getHealth()-10);
            }else{//both wolves have the same health
              animal.setHealth(animal.getHealth()-5);
              below.setHealth(below.getHealth()-5);
            }
          }else{
            moveAgain = false;
          }
          
        }else if ((j>0)&&(direction==2)){//checks space left of current animal
          Life left = map[i][j-1];
          if(map[i][j-1]==null){//empty space left of animal
            map[i][j-1] = map[i][j];//animal moves to empty space
            map[i][j] = null;//original space becomes null
          }else if (nullPositions ==true&&animal instanceof Sheep && left instanceof Sheep&&animal.getHealth()>=20&&left.getHealth()>=20&&animal.gender()!=((Animal)left).gender()){//sheep reproduction
            map[y][x] = new Sheep(20);
            animal.reproduce();
            ((Animal)left).reproduce();//sheep in space to the left loses 10 health
          }else if (nullPositions ==true&&animal instanceof Wolf && left instanceof Wolf&&animal.getHealth()>=20&&left.getHealth()>=20&&animal.gender()!=((Animal)left).gender()){//wolf reproduction
            map[y][x] = new Wolf(20);
            animal.reproduce();
            ((Animal)left).reproduce();//wolf in space to the left loses 10 health
          }else if (animal instanceof Wolf && left instanceof Wolf && animal.gender()==((Animal)left).gender()){//wolves attack each other
            if (((Wolf)animal).compareTo((Wolf)left)>0){//current wolf has more health than wolf to the left
              left.setHealth(left.getHealth()-10);
            }else if (((Wolf)animal).compareTo((Wolf)left)<0){//left wolf has more health than current wolf
              animal.setHealth(animal.getHealth()-10);
            }else{//both wolves have the same health
              animal.setHealth(animal.getHealth()-5);
              left.setHealth(left.getHealth()-5);
            }
          }else{
            moveAgain = false;
          }
        }else if ((j<dimensions-1)&&(direction==3)){//checks space right of current animal
          Life right = map[i][j+1];
          if (map[i][j+1]==null){//empty space right of animal
            map[i][j+1] = map[i][j];//animal moves to the right
            map[i][j] = null;//original space becomes empty
          }else if (nullPositions ==true&&animal instanceof Sheep && right instanceof Sheep&&animal.getHealth()>=20&&right.getHealth()>=20&&animal.gender()!=((Animal)right).gender()){//sheep reproduction
            map[y][x] = new Sheep(20);
            animal.reproduce();
            ((Animal)right).reproduce();
          }else if (nullPositions ==true&&animal instanceof Wolf && right instanceof Wolf&&animal.getHealth()>=20&&right.getHealth()>=20&&animal.gender()!=((Animal)right).gender()){//wolf reproduction
            map[y][x] = new Wolf(20);
            animal.reproduce();
            ((Animal)right).reproduce();
          }else if (animal instanceof Wolf && right instanceof Wolf && animal.gender()==((Animal)right).gender()){//wolves attack each other
            if (((Wolf)animal).compareTo((Wolf)right)>0){//current wolf has more health than wolf to the right
              right.setHealth(right.getHealth()-10);
            }else if (((Wolf)animal).compareTo((Wolf)right)<0){//wolf to the right has more health than current wolf
              animal.setHealth(animal.getHealth()-10);
            }else{
              animal.setHealth(animal.getHealth()-5);
              right.setHealth(right.getHealth()-5);
            }
          }else{
            moveAgain = false;
          }
        }else if (direction==4){//no movement
        }else{
          moveAgain = false;
        }             
      }while (moveAgain==false);//moveAgain set to false if no condition was met in method, loop runs again
      animal.moved();//the animal has made its move this turn
    }
  }
  
  /**
   * plantSpawn
   * This method spawns a plant in an empty spot
   * method is void and does not return anything
   * @param 2D array of objects, integer value of map dimensions, integer nutrition of plant
 */
  public static void plantSpawn(Life[][]map, int dimensions,int plantNutrition){
    boolean moveAgain = true;
    int y=-1,x=-1;
    boolean nullPositions = false;
    boolean plantsOnGrid = false;
    for(int m=0;m<dimensions;m++){
      for(int n=0;n<dimensions;n++){
        if (map[m][n] ==null){//checks if there are empty positions on the grid
          nullPositions = true;
        }
        if (map[m][n] instanceof Plant){//checks if there are any plants present on the grid
          plantsOnGrid = true;
        }
      }
    }
    if (nullPositions==true){
      do{
        moveAgain = true;
        int spawnPosition = ((int)(Math.random()*10));//random number between 0 and 9
        int direction = ((int)(Math.random()*4));//random direction between 0 and 3
        if (nullPositions==true&&spawnPosition<7&&plantsOnGrid==true){//when there are empty positions and plants already on the grid
          do{
            y= ((int)(Math.random()* dimensions));
            x = ((int)(Math.random()* dimensions));
          }while(!(map[y][x] instanceof Plant));//finds position of an existing plant
          if (direction==0&&y>0&&map[y-1][x]==null){//empty space above
            map[y-1][x] = new Plant(plantNutrition);//new plant in space above
          }else if (direction==1&&y<dimensions-1&&map[y+1][x]==null){//empty space below
            map[y+1][x] = new Plant(plantNutrition);//new plant in space below
          }else if (direction==2&&x>0&&map[y][x-1]==null){//empty space to the left
            map[y][x-1] = new Plant(plantNutrition);//new plant in left space
          }else if (direction==3&&x<dimensions-1&&map[y][x+1]==null){//empty space to the right
            map[y][x+1] = new Plant(plantNutrition);//new plant in right space
          }else{
            moveAgain=false;
          }
        }else if (nullPositions==true&&spawnPosition>=7){
          do{
            y= ((int)(Math.random()* dimensions));
            x = ((int)(Math.random()* dimensions));
          }while(map[y][x] != null);//finds empty space
          map[y][x] = new Plant (plantNutrition);//creates new plant in empty space
        }else{
          moveAgain=false;
        }
      } while(moveAgain==false);//moveAgain set to false if no new plant was created in the method, loop runs again    
    }
  }
}
