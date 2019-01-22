/*
  * [Life.java]
  * Creates a superclass called Life
  * Author: Angus Tai
  * Date: May 1st 2018
  */
abstract class Life{
  private int health;
  Life(int health){//initializes health
    this.health = health;
  }
  /**
   * setHealth
   * setter for variable health
   * @param int holding the health of the animal
   */
  public void setHealth(int health){
    this.health = health;
  }
  
  /**
   * getHealth
   * getter for variable health
   * @return int holding the health of the animal
   */
  public int getHealth(){//retrieves health
    return health;
  }
}
    