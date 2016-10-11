import javax.swing.JLabel;
import javax.swing.JTextArea;

public class CardTableModel
{
   public enum HandResult {Lose, Win, Tie}
   
   static int MAX_CARD_PER_HAND = 56;
   static int MAX_PLAYERS = 2;

   private int numCardsPerHand = 7;
   private int numPlayers = 2;
   private String title;
   private int compNumPass = 0;
   private int humanNumPass = 0;
   
   //ADDED ------------------------------------------------------------------------------------------------------------------
   public JLabel[] computerLabels;
   public JLabel[] humanLabels;  
   public JLabel[] playedCardLabels; 
   public JTextArea[] gameStatusText;
   
   Card lastPlayedHumanCard = null;
   Card lastPlayedComputerCard = null;
   Card topStack = null;
   Card bottomStack = null;
      
   CardTableModel(int numCardsPerHand, int numPlayers, String title)
   {
      this.numCardsPerHand = numCardsPerHand;
      this.numPlayers = numPlayers;
      this.title = title;
      
      computerLabels = new JLabel[numCardsPerHand];
      humanLabels = new JLabel[numCardsPerHand];  
      playedCardLabels  = new JLabel[numPlayers]; 
      gameStatusText  = new JTextArea[numPlayers];
   }
   
   CardTableModel()
   {
      this(7, 2, "Card Game");
   }
   
   public int getNumCardsPerHand()
   {
      return numCardsPerHand;
   }

   public int getNumPlayers()
   {
      return numPlayers;
   }
   
   public String getTitle()
   {
      return title;
   }
   
   //ADDED ------------------------------------------------------------------------------------------------------------------
   public String checkGameResults()
   {  
      String resultsString = "";
      
      if (this.humanNumPass < this.compNumPass)
      {
         resultsString = "Game Over -- You won!!!\nFinal Score:\nHuman " + this.humanNumPass + " to CPU "
            + this.compNumPass;  
      }
      else if (this.humanNumPass > this.compNumPass)
      {
         resultsString = "Game Over -- You Lost.\nFinal Score:\nHuman " + this.humanNumPass + " to CPU "
             + this.compNumPass;
      }
      else
      {
         resultsString = "Game over -- Tie Game!\nFinal Score:\nHuman " + this.humanNumPass + " to CPU "
             + this.compNumPass;
      }
      
      return resultsString;
   }
   
   
   public boolean validPlay(Card card, boolean top, boolean bottom)
   {
      if (top && !bottom)
      {
         if(Math.abs(GUICard.valueAsInt(card) - GUICard.valueAsInt(topStack)) == 1)
         {
            return true;
         }
      }
      else if (!top && bottom)
      {
         if(Math.abs(GUICard.valueAsInt(card) - GUICard.valueAsInt(bottomStack)) == 1)
         {
            return true;
         }
      }
      return false;
   }
   
   //increases value of human passes when called
   public void increaseHumanPass()
   {
     humanNumPass++;   
   }
   //increases value of computer passes when called
   public void increaseCompPass()
   {
      compNumPass++;
   }
   //return number of computer passes
   public int getCompPass()
   {
     return compNumPass;   
   }
   //return number of human passes
   public int getHumanPass()
   {
      return humanNumPass;
   }
   
}
