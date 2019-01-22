/*
  * [Wolf.java]
  * Creates a class called Wolf that extends Animal
  * Author: Angus Tai
  * Date: May 1st 2018
  */
public class Wolf extends Animal implements Comparable<Wolf>{
  Wolf(int health){
    super(health);
  }
  
  /**
   * compareTo
   * compares health of this wolf and another wolf
   * @param Object of type Wolf
   * @return int, positive if this wolf has more health than another wolf, negative if this wolf has less
   */
  public int compareTo(Wolf wolf){//compares health of this wolf and another wolf
    return Integer.compare(this.getHealth(), wolf.getHealth());
}
}