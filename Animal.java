 /*
  * [Animal.java]
  * Creates a class called Animal that extends Life
  * Author: Angus Tai
  * Date: May 1st 2018
  */
abstract class Animal extends Life{
   boolean moved = false;
   private int gender =((int)(Math.random()* 2));//gender is a random number between 0 and 1
  Animal(int health){
    super(health);
  }
  
  /**
   * loseHealthPerTurn
   * This method makes the animal lose 1 health every turn  
 */
  public void loseHealthPerTurn(){
    this.setHealth(this.getHealth() - 1);
  }
  
  /**
   * eat
   * This method takes the nutrition of the eaten object and adds it to the animal's health
   * @param An int holding the nutrition of the eaten object
   */
  public void eat(int nutrition){
    this.setHealth(this.getHealth() + nutrition);
  }
  
  /**
   * reproduce
   * This method makes the animal lose 10 health as a result of reproducing
   */
  public void reproduce(){
    this.setHealth(this.getHealth()-10);
  }
  
  /**
   * moved
   * method to keep track of whether animal has moved in a particular turn
   */
  public void moved(){
    moved = true;
  }
  
  /**
   * gender
   * returns the gender of the animal
   * @return int, returns animal's gender
   */
  public int gender(){
    return this.gender;
  }
}
