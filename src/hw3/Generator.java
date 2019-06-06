package hw3;
import java.util.Random;

import api.Dot;

/**
 * Class encapsulating a mechanism for producing new Dot objects
 * for a game.
 * @author JackGuzzetta
 * @version 11/2/16
 */
public class Generator
{
	int x;
	Dot optionDot;

   
  /**
   * Constructs a Generator whose generate() method always
   * returns a Dot of the given type.  (This method is intended
   * for testing.)
   * @param givenType
   *   type of Dot objects to be generated
   */
  public Generator(int givenType)
  {
	   optionDot = new Dot(givenType);
  }
 
  /**
   * Constructs a Generator that will create
   * random Dots with types 0 through numTypes - 1,
   * using the given Random instance.
   * @param numTypes
   *   number of types of dots
   * @param rand
   *   random number generator to use
   */
  public Generator(int numTypes, Random rand)
  {
	  x=numTypes;
	  int randomNum=rand.nextInt(numTypes);
    optionDot = new Dot(randomNum);
  }
  
  /**
   * Returns an instance of Dot according to this generator's rules
   * (Random or fixed value).
   * @return
   *   a new Dot object
   */
  public Dot generate()
  {
    return new Generator(x,new Random()).optionDot;
  }

  /**
   * Initializes the given 2D array of Dot objects
   * with values produced by this generator.
   * @param grid
   *   a 2D array to be initialized
   */
  public void initialize(Dot[][] grid)
  { 
    for (int row = 0; row < grid.length; ++row)
    {
      for (int col = 0; col < grid[0].length; ++col)
      {
        grid[row][col] = generate();
      }
    }
  }

}
