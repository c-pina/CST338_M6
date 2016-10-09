import java.util.Arrays;

public class Deck {

   private static int CARDS_PER_DECK = 56;
   private static int MAX_DECKS = 6;
   public static int MAX_CARDS = MAX_DECKS * CARDS_PER_DECK;
   private static Card masterPack[];   
   private Card cards[];
   private int topCard;
   private int numPacks;

   /**
    * Default constructor, uses 1 pack
    */
   public Deck()
   {
      this.init(1);
   }

   /**
    * Overloaded constructor, accepts the number of packs
    * @param numPacks
    */
   public Deck(int numPacks)
   {
      this.init(numPacks);
   }

   /**
    * Sets the number of packs, cards, master pack, and top card index
    * @param numberOfPacks
    */
   public void init(int numberOfPacks)
   {
      // handles out of bound decks
      if (numberOfPacks > MAX_DECKS)
      {
         System.out.printf("Too many decks, defaulting to [%d]", MAX_DECKS);
         this.numPacks = MAX_DECKS;
      }
      else
      {
         // if 0 is passed, set to 1
         this.numPacks = numberOfPacks > 0 ? numberOfPacks : 1;
      }

      // make sure we can't new an array with more than the max cards 
      this.cards = new Card[Math.min(MAX_CARDS, numPacks * CARDS_PER_DECK)];

      allocateMasterPack();

      // fill our cards[] using copies of the master pack
      for (int i = 0; i < this.numPacks; i++)
      { 
         for (Card card : masterPack)
         {  
            // flatten out the index so we have a continuous pack
            int indexOfCard = Arrays.asList(masterPack).indexOf(card) + (masterPack.length * i);
            this.cards[indexOfCard] = card;
            this.topCard = indexOfCard;
         }
      }
   }

   /**
    * deals card off the top of the deck and removes it from the stack
    * @return a Card if valid, or null of empty
    */ 
   public Card dealCard()
   {
      if (this.cards.length > 0) 
      {
         int lastCardIndex = this.cards.length - 1;
         Card card = this.cards[lastCardIndex];

         // trim the array
         this.cards = Arrays.copyOf(this.cards, lastCardIndex--);

         // handles the case where the top card is the last card on the stack 
         this.topCard = this.cards.length > 0 ? this.cards.length - 1 : 0;
         return card;
      }
      else
      {
         return null;
      }
   }

   /**
    * Gets the top card off the stack
    * @return Card, if valid, or null if empty
    */
   public Card getTopCard()
   {
      if (this.topCard < this.cards.length)
      {
         return this.cards[this.topCard];
      }
      else
      {
         return null;
      }
   }

   /**
    * Checks an index to see if the card is valid
    * @param k
    * @return Card, or Card with errorFlag if invalid
    */
   public Card inspectCard(int k)
   {
      Card card = new Card();
      if (k <= this.cards.length - 1)
      {
         card = this.cards[k];
      } 
      else
      {
         card = new Card('0', Card.Suit.clubs);
      }

      return card;
   }

   /**
    * Iterates through each card and randomly swapping it with another index
    */
   public void shuffle()
   {
      Card shuffled[] = Arrays.copyOf(this.cards, this.cards.length);
      for (int i = 0; i < shuffled.length; i++)
      {
         int rightIndex = this.randomInt();
         Card savedCard = shuffled[i];
         shuffled[i] = shuffled[rightIndex];
         shuffled[rightIndex] = savedCard;   
      }

      this.cards = shuffled;
   }
   
   /**
    * Creates a standard deck
    */
   private static void allocateMasterPack()
   {
      if (masterPack != null)
      {
         return;
      }

      int suitCounter = 0;
      masterPack = new Card[CARDS_PER_DECK];

      for (int i = 0; i < CARDS_PER_DECK; i++)
      {
         int moddedInteger = i % 14;

         // Once we get past 13 cards, we change the suit
         if (i > 0 && moddedInteger == 0)
         {
            suitCounter++;
         }

         // Creates the card with indexes 0-13, and suites 0-4
         Card card = new Card(charForInteger(moddedInteger), suitForInteger(suitCounter));
         masterPack[i] = card;
      }
   }

   /**
    * Helper to get a random integer from 0 - 52
    * @return int - the random int
    */
   private int randomInt()
   {
      return (int)Math.floor(Math.random() * this.cards.length);
   }

   /**
    * Gets a suit
    * @param int - 0 - 4
    * @return Card.Suit - clubs, diamonds, hearts, or spades
    */
   private static Card.Suit suitForInteger(int value)
   {
      switch (value)
      {
      case 0:
         return Card.Suit.clubs;
      case 1:
         return Card.Suit.diamonds;
      case 2:
         return Card.Suit.hearts;
      case 3:
         return Card.Suit.spades;
      }

      return null;
   }

   /**
    * Assigns a char value for an integer input.
    * Indexes 1-8 return the Char representation of the numeric input  
    * @param value
    * @return char
    */
   private static char charForInteger(int value)
   {  
      if (value == 0) 
      {
         return 'A';
      } 
      else if (value == 9)
      {
         return 'T';
      }
      else if (value == 10)
      {
         return 'J';
      }
      else if (value == 11)
      {
         return 'Q';
      }
      else if (value == 12)
      {
         return 'K';
      }
      else if (value == 13)
      {
         return 'X';
      }
      else
      {
         return Character.forDigit(value + 1, 10);
      }
   }
   
   //adds card to top of deck
   public boolean addCard(Card card)
   {
      int cardInstances = 0;
      if (cards.length != 0)
      {
         for(int i = 0; i >= topCard; i++)
         {
            if (card.toString().equals(cards[i].toString()))
            {
               cardInstances++;
            }
         }
      }
      if (cardInstances >= numPacks || topCard + 1 == numPacks * CARDS_PER_DECK)
      {
         return false;
      }
      else
      {
         int newLength = this.cards.length + 1;
         Card tempCard = new Card(card);

         // trim the array
         this.cards = Arrays.copyOf(this.cards, newLength);
         topCard += 1;

         this.cards[topCard] = tempCard;
         return true;
      }
   }
   
   //removes specified card from deck
   public boolean removeCard(Card card)
   {
      int cardIndex = 0;
      while (cardIndex < cards.length)
      {
        if (card.toString().equals(cards[cardIndex].toString()))
        {
           cards[cardIndex] = cards[topCard];
           this.cards = Arrays.copyOf(this.cards, this.cards.length - 1);
           topCard -= 1;
           return true;
        }
        cardIndex++;
      }
     
      return false;
   }
   
   //sort the deck in ascending order
   public void sort()
   {
      Card.arraySort(this.cards, this.topCard + 1);
   }
   
   //returns number of cards in deck
   public int getNumCards()
   {
      int tempInt = this.cards.length == 0 ? 0 : 1;
      return this.topCard + tempInt;
   }
}