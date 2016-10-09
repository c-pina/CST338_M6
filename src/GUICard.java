import javax.swing.Icon;
import javax.swing.ImageIcon;

public class GUICard
{   
   private static Icon[][] iconCards = new ImageIcon[14][4]; // 14 = A thru K + joker
   private static Icon iconBack;
   
   //loads cards icon in 2D array iconCars if iconCards is null
   static void loadCardIcons()
   {
      if(iconCards[0][0] == null)
      {  
         //Add back of card
         iconBack = new ImageIcon("images/BK.gif");
         
         //Create and add all other cards
         String cardName = "";
         for(int j = 0; j < 4; j++)
         {
            for (int k = 0; k < 14; k++)
            {
               cardName = turnIntIntoCardValue(k) + turnIntIntoCardSuit(j) + ".gif";
               iconCards[k][j] = new ImageIcon("images/" + cardName);
            }
         }
      }
   }
   
 //returns icon for given card. call loadCardIcons to ensure array is not empty
   static public Icon getIcon(Card card)
   {
      return iconCards[valueAsInt(card)][suitAsInt(card)];
   }
   
   //returns iconBack
   static public Icon getBackCardIcon()
   {
      loadCardIcons();
      return iconBack;
   }
   
   static String turnIntIntoCardValue(int k)
   {
      switch(k)
      {
         case 0: 
            return "X";
         case 1:
            return "A";
         case 10:
            return "T";
         case 11:
            return "J";
         case 12:
            return "Q";
         case 13:
            return "K";
         default:
            return Integer.toString(k);
      }
   }
   
   public static int valueAsInt(Card card)
   {
      char value = card.getValue();
      switch(value)
      {
         case 'X':
            return 0;
         case 'A':
            return 1;
         case 'T':
            return 10;
         case 'J':
            return 11;
         case 'Q':
            return 12;
         case 'K':
            return 13;
         default:
            return Character.getNumericValue(value);
      }
   }
   
   static String turnIntIntoCardSuit(int j)
   {
      switch(j)
      {
         case 0:
            return "C";
         case 1:
            return "D";
         case 2:
            return "H";
         case 3:
            return "S";
         default:
            return null;
      }
      
   }
   
   public static int suitAsInt(Card card)
   {
      Card.Suit suit = card.getSuit();
      
      switch(suit)
      {
         case clubs:
            return 0;
         case diamonds:
            return 1;
         case hearts:
            return 2;
         case spades:
            return 3;
         default:
            return 10;      
      } 
   }
}
