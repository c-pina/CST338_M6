import java.util.ArrayList;

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
   
   //ADDED ------------------------------------------------------------------------------------------------------------------
   public JLabel[] computerLabels;
   public JLabel[] humanLabels;  
   public JLabel[] playedCardLabels; 
   public JTextArea[] gameStatusText;
   
   ArrayList<Card> cardsComputerHasCaptured = new ArrayList<Card>();
   ArrayList<Card> cardsHumanHasCaptured = new ArrayList<Card>();
   
   Card lastPlayedHumanCard = null;
   Card lastPlayedComputerCard = null;
      
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
   public String checkGameResults(Hand firstHand, Hand secondHand)
   {  
      String resultsString = "";
      
      if (this.cardsHumanHasCaptured.size() > this.cardsComputerHasCaptured.size())
      {
         resultsString = "Game Over -- You won\n by capturing " + this.cardsHumanHasCaptured.size()
            + "\n of the computer's cards!";  
      }
      else if (this.cardsHumanHasCaptured.size() < this.cardsComputerHasCaptured.size())
      {
         resultsString = "Game Over -- You lost! \n Computer captured " + this.cardsComputerHasCaptured.size()
            + "\n of your cards!";
      }
      else
      {
         resultsString = "Game over -- Tie Game!";
      }
      
      return resultsString;
   }
   
   public HandResult handResultForHumanCard(/*Card humanCard, Card computerCard*/)
   {
      char humanValue = lastPlayedHumanCard.getValue();
      char computerValue = lastPlayedComputerCard.getValue();
      
      // cards are equal, joker ties (only case), otherwise rank wins
      if (humanValue == computerValue)
      {
         if (humanValue == 'X')
         {
            return HandResult.Tie;
         }
         
         if (lastPlayedHumanCard.getSuit().getValue() > lastPlayedComputerCard.getSuit().getValue())
         {
            return HandResult.Win;
         }
         else
         {
            return HandResult.Lose;
         }
      }
      else
      {
         // cards aren't equal, joker wins, otherwise card value wins
         if (humanValue == 'X')
         {
            return HandResult.Win;
         }
         
         if (computerValue == 'X')
         {
            return HandResult.Lose;
         }
         
         // check for Ace, special case
         if (humanValue == 'A')
         {
            return HandResult.Win; 
         }
         
         if (computerValue == 'A')
         {
            return HandResult.Lose; 
         }
         
         // now all others
         if (humanValue > computerValue)
         {
            return HandResult.Win;
         }
         else
         {
            return HandResult.Lose;
         }
      }
   }
}
