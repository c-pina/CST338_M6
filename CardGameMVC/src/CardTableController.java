import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class CardTableController implements GameTimerEvent
{
   
   static int NUM_CARDS_PER_HAND = 7;
   static int  NUM_PLAYERS = 2;

   
   CardGameFramework buildGame;
   CardTableModel myCardTableModel;
   CardTableView myCardTableView;
   GameTimer gameTimer;
   int elapsedGameTime = 0;
   Boolean gamePaused = false, selectTopStack = false, selectBottomStack = false, 
         humanPass=false, computerPass=false;
   

   public void startGame()
   {
      int numPacksPerDeck = 1;
      int numJokersPerPack = 0;
      int numUnusedCardsPerPack = 0;
      Card[] unusedCardsPerPack = null;    
      String welcomeMessage = "Welcome to BUILD!\nJokers are 0 and\nAces are 1.\nYou go first.";
      
      // establish main frame in which program will run
      this.myCardTableModel = new CardTableModel(NUM_CARDS_PER_HAND, NUM_PLAYERS, "High Card");
      this.myCardTableView = new CardTableView(myCardTableModel);
      
      this.myCardTableView.addTimerButton(this);
      this.addGameTimer();
      this.myCardTableView.pnlPassArea.add(this.humanPass(), JLabel.CENTER);
      this.myCardTableView.updateScoreboard(myCardTableModel.getHumanPass(), myCardTableModel.getCompPass());

      this.buildGame = new CardGameFramework(
            numPacksPerDeck, 
            numJokersPerPack,  
            numUnusedCardsPerPack, 
            unusedCardsPerPack, 
            NUM_PLAYERS, 
            NUM_CARDS_PER_HAND);

      this.buildGame.deal();  
      

      // CREATE LABELS ----------------------------------------------------
      for (int i = 0; i < myCardTableModel.getNumCardsPerHand(); i++)
      {
         JLabel computerLabel = new JLabel(GUICard.getBackCardIcon());
         myCardTableModel.computerLabels[i] = computerLabel;

         // create the player label
         Card playerCard = this.buildGame.getHand(0).inspectCard(i);
         JLabel playerLabel = this.labelForCard(playerCard, i);
         myCardTableModel.humanLabels[i] = playerLabel;
      }
      
      // ADD LABELS TO PANELS -----------------------------------------
      for (int j = 0; j < myCardTableModel.getNumCardsPerHand(); j++)
      {
         myCardTableView.displayLabel(myCardTableView.pnlComputerHand, myCardTableModel.computerLabels[j]);
         myCardTableView.displayLabel(myCardTableView.pnlHumanHand, myCardTableModel.humanLabels[j]);
      }
      
      this.myCardTableModel.topStack = this.buildGame.getCardFromDeck();
      this.myCardTableView.pnlPlayArea.add(this.labelForTopStack(myCardTableModel.topStack));
      
     
      this.myCardTableModel.bottomStack = this.buildGame.getCardFromDeck();
      this.myCardTableView.pnlPlayArea.add(this.labelForBottomStack(myCardTableModel.bottomStack));
      
      myCardTableView.displayTextOnLabel(welcomeMessage, myCardTableModel.gameStatusText);
      //this.myCardTableView.displayTurnLabelForHuman(true, myCardTableModel.gameStatusText);

      // show everything to the user
      this.myCardTableView.setVisible(true);  
   }
   
   void handleAction(ActionEvent e)
   {     
     int cardIndex = Integer.parseInt(e.getActionCommand());
     Hand hand = this.buildGame.getHand(0);
    
      if (!selectTopStack && !selectBottomStack)
      {
            myCardTableView.displayTextOnLabel("Select top or bottom\nstack first!", myCardTableModel.gameStatusText); 
      }
      else if (myCardTableModel.validPlay(hand.inspectCard(cardIndex), this.selectTopStack, this.selectBottomStack))
     {
         // get the tapped card from the human's hand and play it
         
         myCardTableModel.lastPlayedHumanCard = this.buildGame.playCard(0, cardIndex);
      
         // add card to the playing area
            if(selectTopStack && !selectBottomStack)
            {
               myCardTableModel.topStack = myCardTableModel.lastPlayedHumanCard;
               this.reloadPlayArea();
            }
      
            else if(!selectTopStack && selectBottomStack)
            {
               myCardTableModel.bottomStack = myCardTableModel.lastPlayedHumanCard;
               this.reloadPlayArea();
            }
            humanPass = false;
            this.buildGame.takeCard(0);
            this.reloadHumanHand();
            this.playComputerHand();
            
            if (buildGame.getNumCardsRemainingInDeck() == 0)
            {
               myCardTableView.reloadHumansHand();
               myCardTableView.reloadPassArea();
               String winLossMessage = myCardTableModel.checkGameResults();
               myCardTableView.displayTextOnLabel(winLossMessage, myCardTableModel.gameStatusText);
            }
            
     }
      else
      {
         myCardTableView.displayTextOnLabel("Play is not valid", myCardTableModel.gameStatusText);
         this.reloadPlayArea();
         this.reloadHumanHand();
      }
   }
   
   private void playComputerHand()
   {  
     myCardTableView.displayTurnLabelForHuman(false, myCardTableModel.gameStatusText);
      Hand hand = this.buildGame.getHand(1);
      
      for (int j = 0; j < NUM_CARDS_PER_HAND; j++)
      {
         Card tempCard = hand.inspectCard(j);
         int tempInt = GUICard.valueAsInt(tempCard) - GUICard.valueAsInt(myCardTableModel.topStack);
         
         if(Math.abs(tempInt) == 1)
         {
            myCardTableModel.topStack = this.buildGame.playCard(1, j);
            computerPass = false;
            break;
         }
         tempInt = GUICard.valueAsInt(tempCard) - GUICard.valueAsInt(myCardTableModel.bottomStack);
         if(Math.abs(tempInt) == 1)
         {
            myCardTableModel.bottomStack = this.buildGame.playCard(1, j);
            computerPass = false;
            break;
         }
      }
      if (hand.getNumCards() == NUM_CARDS_PER_HAND)
      {
         myCardTableModel.increaseCompPass();
         computerPass = true;
         this.myCardTableView.clearScoreboard();
         this.myCardTableView.updateScoreboard(myCardTableModel.getHumanPass(), myCardTableModel.getCompPass());
         dealDoublePass();
      }
      else
      {
         this.buildGame.takeCard(1);
      }
      
      // add card to view
      this.reloadPlayArea();
      
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
      if (buildGame.getNumCardsRemainingInDeck() == 0)
      {
        myCardTableView.reloadHumansHand();
        myCardTableView.reloadPassArea();
         String winLossMessage = myCardTableModel.checkGameResults();
         myCardTableView.displayTextOnLabel(winLossMessage, myCardTableModel.gameStatusText);
      }
      myCardTableView.displayTurnLabelForHuman(true, myCardTableModel.gameStatusText);
      
   }
   
   //resets player card icons and loads hand
   public void reloadHumanHand()
   {      
      // remove all the cards from the player's hand, except for the timer
      this.myCardTableView.reloadHumansHand();

      // clear out the labels array
      Hand hand = this.buildGame.getHand(0);
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
   
   public JLabel labelForTopStack(Card card)
   {
      JLabel label = new JLabel(GUICard.getIcon(card));

         // add the button
         JButton btn1 = new JButton("Top");

         btn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               selectTopStack = true;
               selectBottomStack = false;
               //handleAction(e);
            }          
         });

         myCardTableView.displayButton(btn1, label);
         return label; 
   }

   public JLabel labelForBottomStack(Card card)
   {
      JLabel label = new JLabel(GUICard.getIcon(card));

         // add the button
         JButton btn1 = new JButton("Bottom");
         btn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               selectTopStack = false;
               selectBottomStack = true;
              // handleAction(e);
            }          
         });

         myCardTableView.displayButton(btn1, label);
         return label;
   }
   
   //human player skips turn
   public JLabel humanPass()
   {
      JLabel label = new JLabel("Pass");

         // add the button
         JButton btn1 = new JButton("Pass");
         btn1.setText("Skip Turn");
         btn1.setSize(385, 40);
         btn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               humanPass = true;
               
               myCardTableModel.increaseHumanPass();
               myCardTableView.clearScoreboard();
                   myCardTableView.updateScoreboard(myCardTableModel.getHumanPass(), myCardTableModel.getCompPass());
               myCardTableView.reloadPassArea();
               myCardTableView.pnlPassArea.add(humanPass(), JLabel.CENTER);
               dealDoublePass();
               playComputerHand();
              // handleAction(e);
            }          
         });

         label.add(btn1, JLabel.CENTER);
        // myCardTableView.displayButton(btn1, label);
         return label;
   }
   //checks if both human and cpu pass and then deals fresh card to both stacks
   public void dealDoublePass()
   {
      if(humanPass && computerPass)
      {
       this.myCardTableModel.topStack = this.buildGame.getCardFromDeck();    
       this.myCardTableModel.bottomStack = this.buildGame.getCardFromDeck();
       reloadPlayArea();
       this.humanPass = false;
       this.computerPass = false;
      } 
   }
   
   public void reloadPlayArea()
   {
      selectTopStack = false;
      selectBottomStack = false;
      this.myCardTableView.pnlPlayArea.removeAll();
      this.myCardTableView.pnlPlayArea.add(this.labelForTopStack(myCardTableModel.topStack));
      this.myCardTableView.pnlPlayArea.add(this.labelForBottomStack(myCardTableModel.bottomStack));   
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