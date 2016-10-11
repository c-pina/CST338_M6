import java.util.Arrays;

class Hand {
   
   public static int MAX_CARDS = 50;
   private Card[] myCards;
   private int numCards;
   
   //default Constructor
   public Hand()
   {
      myCards = new Card[MAX_CARDS];
      numCards = 0;
   }
   
   //clears array by creating a new array. Old array will eventually be removed by garbage collector
   public void resetHand()
   {
      myCards = new Card[MAX_CARDS];
      numCards = 0;
   }
   
   //takes a card as input and add is to the top of the myCard array if card is added returns true else it returns false
   public boolean takeCard(Card card)
   {
      if (numCards != MAX_CARDS)
      {
         myCards[numCards] = new Card(card);
         numCards ++;
         return true;
      }
      else
      {
         return false;
      }
   }
   
   //removes the Card in the nth position of the myCard array
   public Card playCard(int index)
   {
      if (numCards > 0)
      {
         Card tempCard = myCards[index];
         numCards--;

        for (int i = index; i < numCards; i++)
         {
           myCards[i] = myCards[i + 1];
         }
         
        myCards[numCards] = null;
         return tempCard;
      }
      else
      {
         Card tempCard = new Card('0', Card.Suit.clubs);
         return tempCard;
      }
   }
   
   public Card playCard()
   {
      return this.playCard(numCards - 1);
   }
   
   //Places the entire hand into a string for display
   public String toString()
   {
      String[] tempString = new String[numCards];
      
      for (int i = 0; i < numCards; i++)
      {
         tempString[i] = (myCards[i].toString() + " ");
      }
      
      return Arrays.toString(tempString);
   }
   
   //returns the number of cards in the deck
   public int getNumCards()
   {
      return numCards;
   }
   
   //removes the nulls 
   public Card[] validCards()
   {
      Card[] validCards = new Card[this.myCards.length];
      for (int i = 0; i < this.myCards.length; i++)
      {
         validCards[i] = this.myCards[i];
      }
      
      for (int i = 0; i < validCards.length; i++)
      {
         Card tempCard = validCards[i];
         int nextIndex = i + 1;
         if (nextIndex < validCards.length - 1)
         {
            if (tempCard == null)
            {
               validCards[i] = validCards[i + 1];
               validCards[i + 1] = tempCard;
            }
         }         
      }
      
      this.myCards = validCards;
      
      return validCards;
   }
   
   //returns the card at element k in the myCard array. If k is invalid an invalid card is created and returned.
   public Card inspectCard(int k)
   {
      if ((k >= 0) && (k < numCards ))
      {
         return myCards[k];
      }
      else
      {
         Card tempCard = new Card('0', Card.Suit.clubs);
         return tempCard;
      }
   }
   
   public void sort()
   {
      Card.arraySort(this.myCards, this.numCards);
   }
}