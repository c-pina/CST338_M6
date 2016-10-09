
public class Card
{
   public enum Suit { 
      clubs(0), diamonds(1), hearts(2), spades(3);

      private final int value;
      private Suit(int value) {
          this.value = value;
      }

      int getValue() {
          return value;
      }
   };
   
   public static char[] valueRanks = {'A','2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'X'};
   
   private char value;
   private Suit suit;
   private boolean errorFlag;
   
   //default constructor. Sets card to ace of spades
   public Card()
   {
      this.set('A', Suit.spades);
   }
   
   //overloaded constructor. Takes in value and suit parameters
   public Card (char value, Suit suit)
   {
      this.set(value, suit);
   }

   //Copy constructor
   public Card (Card card)
   {
      this.set(card.value, card.suit);
   }
   
   //formats value and suit data members to a readable string format. If errorFlag is true the method returns "illegal" as the value.
   public String toString()
   {
      if (errorFlag)
      {
         return ("** illegal **");   
      }
      else
      {
         return (value + " of " + suit);
      }
   }
   
   //evaluates the parameters for validity and sets the errorFlag accordingly. data members are set regardless of input
   public boolean set(char value, Suit suit)
   {
      if (isValid(value, suit))
      {
         this.errorFlag = false;
      }
      else
      {
         this.errorFlag = true;
      }
      
      this.value = value;
      this.suit = suit;
      return true;
   }
   
   //returns the suit data member
   public Suit getSuit()
   {
      return suit;
   }
   
   //returns the value data member
   public char getValue()
   {
      return value;
   }
   
   //returns the error flag
   public boolean getErrorFlag()
   {
      return errorFlag;
   }
   
   //checks to see if another card is equal to this
   public boolean equals(Card card)
   {
      if ((card.getSuit() == this.suit) && (card.getValue() == this.value))
      {
         return true;
      }
      else
      {
         return false;
      }
   }
   
   //check the validity of an entry. Verifies the suit is in the enum then check if value is valid. returns true if both cases are met
   private boolean isValid(char value, Suit suit)
   {
      for (Suit type : Suit.values())
      {
         if (type == suit)
         {
            switch (value)
            {
            case 'A':
               return true;
            case '2':
               return true;
            case '3':
               return true;
            case '4':
               return true;
            case '5':
               return true;
            case '6':
               return true;
            case '7':
               return true;
            case '8':
               return true;
            case '9':
               return true;
            case 'T':
               return true;
            case 'J':
               return true;
            case 'Q':
               return true;
            case 'K':
               return true;
            case 'X':
               return true;
            }
         }
      }
      
      return false;
   }
   
   //calls cardValueSort and cardSuitSort to sort an array of cards into ascending order
   static void arraySort(Card[] cardArray, int arraySize)
   {
      cardValueSort(cardArray, arraySize);
      cardSuitSort(cardArray, arraySize);
   }
   
   //uses a bubble sort to sort an array of cards in ascending order by card value
   static void cardValueSort(Card[] cardArray, int arraySize)
   {
      Card tempCard;
      String stringArray = new String(valueRanks);
     
      for (int i = 0; i < arraySize; i++)
      {
         for (int j = 1; j < arraySize; j++)
         {
            if (stringArray.indexOf(cardArray[j-1].getValue()) > stringArray.indexOf(cardArray[j].getValue()))
            {   
               tempCard = cardArray[j-1];
               cardArray[j-1] = cardArray[j];
               cardArray[j] = tempCard;
            }
         }
      }
   }
   
   //uses bubble sort to sort array of cards by suit in ascending order
   static void cardSuitSort (Card[] cardArray, int arraySize)
   {
      Card tempCard;
      for (int i = 0; i < arraySize; i++)
      {
         for (int j = 1; j < arraySize; j++)
         {
            if (cardArray[j-1].getSuit().ordinal() > cardArray[j].getSuit().ordinal())
            {   
               tempCard = cardArray[j-1];
               cardArray[j-1] = cardArray[j];
               cardArray[j] = tempCard;
            }
         }
      }
   }
}