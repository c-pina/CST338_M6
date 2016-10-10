import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class CardTableController implements GameTimerEvent
{
   
   static int NUM_CARDS_PER_HAND = 7;
   static int  NUM_PLAYERS = 2;

   
   CardGameFramework highCardGame;
   CardTableModel myCardTableModel;
   CardTableView myCardTableView;
   GameTimer gameTimer;
   int elapsedGameTime = 0;
   Boolean gamePaused = false;

   public void startGame()
   {
      int numPacksPerDeck = 1;
      int numJokersPerPack = 0;
      int numUnusedCardsPerPack = 0;
      Card[] unusedCardsPerPack = null;    

      // establish main frame in which program will run
      this.myCardTableModel = new CardTableModel(NUM_CARDS_PER_HAND, NUM_PLAYERS, "High Card");
      this.myCardTableView = new CardTableView(myCardTableModel);
      
      this.myCardTableView.addTimerButton(this);
      this.addGameTimer();

      this.highCardGame = new CardGameFramework(
            numPacksPerDeck, 
            numJokersPerPack,  
            numUnusedCardsPerPack, 
            unusedCardsPerPack, 
            NUM_PLAYERS, 
            NUM_CARDS_PER_HAND);

      this.highCardGame.deal();      

      // CREATE LABELS ----------------------------------------------------
      for (int i = 0; i < myCardTableModel.getNumCardsPerHand(); i++)
      {
         JLabel computerLabel = new JLabel(GUICard.getBackCardIcon());
         myCardTableModel.computerLabels[i] = computerLabel;

         // create the player label
         Card playerCard = this.highCardGame.getHand(0).inspectCard(i);
         JLabel playerLabel = this.labelForCard(playerCard, i);
         myCardTableModel.humanLabels[i] = playerLabel;
      }
      
      // ADD LABELS TO PANELS -----------------------------------------
      for (int j = 0; j < myCardTableModel.getNumCardsPerHand(); j++)
      {
         myCardTableView.displayLabel(myCardTableView.pnlComputerHand, myCardTableModel.computerLabels[j]);
         myCardTableView.displayLabel(myCardTableView.pnlHumanHand, myCardTableModel.humanLabels[j]);
      }
      
      myCardTableView.displayTurnLabelForHuman(true, myCardTableModel.gameStatusText);

      // show everything to the user
      this.myCardTableView.setVisible(true);  
   }
   
   void handleAction(ActionEvent e)
   {  
      // get the tapped card from the human's hand and play it
      int cardIndex = Integer.parseInt(e.getActionCommand());
      myCardTableModel.lastPlayedHumanCard = this.highCardGame.playCard(0, cardIndex);
      
      // remove all cards from the playing area
      this.myCardTableView.pnlPlayArea.removeAll();

      // add card to the playing area
      JLabel playedCardLabel = new JLabel(GUICard.getIcon(myCardTableModel.lastPlayedHumanCard));
      this.myCardTableView.pnlPlayArea.add(playedCardLabel, JLabel.CENTER);
      this.reloadHumanHand();
      this.playComputerHand();
      if (highCardGame.getHand(0).getNumCards() == 0 && highCardGame.getHand(1).getNumCards() == 0)
      {
         String winLossMessage = myCardTableModel.checkGameResults(highCardGame.getHand(0), highCardGame.getHand(1));
         myCardTableView.displayTextOnLabel(winLossMessage, myCardTableModel.gameStatusText);
      }
   }
   
   private void determineWinningHand()
   {
      CardTableModel.HandResult result = myCardTableModel.handResultForHumanCard();
      
      // human won, add to the pot
      if (result == CardTableModel.HandResult.Win)
      {
         myCardTableView.displayTextOnLabel("Win", myCardTableModel.gameStatusText);
         myCardTableModel.cardsHumanHasCaptured.add(myCardTableModel.lastPlayedComputerCard);
         JLabel wonCard = new JLabel(GUICard.getIcon(myCardTableModel.lastPlayedComputerCard));
         myCardTableView.displayLabel(myCardTableView.pnlHumanPot, wonCard);
      } 
      else if (result == CardTableModel.HandResult.Lose)
      {
         myCardTableModel.cardsComputerHasCaptured.add(myCardTableModel.lastPlayedHumanCard);
         myCardTableView.displayTextOnLabel("Lose", myCardTableModel.gameStatusText);
      }
      else
      {
         myCardTableView.displayTextOnLabel("Tie", myCardTableModel.gameStatusText);
      }
   }
   
   private void playComputerHand()
   {
      Hand hand = this.highCardGame.getHand(1);
      myCardTableModel.lastPlayedComputerCard = this.highCardGame.playCard(1, 0);
      
      // add card to view
      JLabel playedCardLabel = new JLabel(GUICard.getIcon(myCardTableModel.lastPlayedComputerCard));
      this.myCardTableView.pnlPlayArea.add(playedCardLabel, JLabel.CENTER);
      
      // remove all the cards from the computer's hand and reload
      this.myCardTableView.pnlComputerHand.removeAll();
      
      Card[] validCards = hand.validCards();
      myCardTableModel.computerLabels = new JLabel[validCards.length];

      for (int i = 0; i < validCards.length; i++)
      {
         Card localCard = validCards[i];
         if (localCard != null)
         {
            JLabel computerLabel = new JLabel(GUICard.getBackCardIcon());
            myCardTableModel.computerLabels[i] = computerLabel;
            myCardTableView.displayLabel(myCardTableView.pnlComputerHand, computerLabel);
         }
      }
      
      this.determineWinningHand();
   }
    
   public void reloadHumanHand()
   {      
      // remove all the cards from the player's hand, except for the timer
      this.myCardTableView.reloadHumansHand();

      // clear out the labels array
      Hand hand = this.highCardGame.getHand(0);
      myCardTableModel.humanLabels = new JLabel[hand.getNumCards()];
      
      // re-index the cards onto the view
      Card[] validCards = hand.validCards();
 
      for (int i = 0; i < validCards.length; i++)
      {
         // create the label
         Card validCard = validCards[i];
         if (validCard == null)
         {
            break;
         }

         JLabel label = this.labelForCard(validCard, i);
         myCardTableModel.humanLabels[i] = label;

         // now add the card onto the view
         myCardTableView.displayLabel(myCardTableView.pnlHumanHand, myCardTableModel.humanLabels[i]);
      }      
   }
   
   public JLabel labelForCard(Card card, int index)
   {
      JLabel label = new JLabel(GUICard.getIcon(card));

      // add the button
      JButton btn1 = new JButton(Integer.toString(index));
      btn1.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            handleAction(e);
         }          
      });

      myCardTableView.displayButton(btn1, label);
      return label;
   }

   public void gameTimerEventTick(int seconds)
   {
      this.myCardTableView.updateTimerWithValueInSeconds(seconds);
   } 

   public void gameTimerEventToggle()
   {
      // pause the game
      if (this.gamePaused == true)
      {
         this.gamePaused = false;
         this.addGameTimer();
      }
      else
      {
         // resume the game
         this.gamePaused = true;
         this.elapsedGameTime += this.gameTimer.getGameTime();
         this.gameTimer.stopTimer();
         this.myCardTableView.updateTimerWithString("PAUSED");
      }
   }

   private void addGameTimer()
   {
      if (this.gameTimer != null)
      {
         this.gameTimer.stopTimer();
      }

      this.gameTimer = new GameTimer(this);
      this.gameTimer.start();
      this.gameTimer.gameTime = this.elapsedGameTime;
      this.elapsedGameTime = 0;
   }
}