import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardTableView extends JFrame {
   
   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea, pnlGameStatus, pnlScoreBoard, pnlPassArea, pnlHumanArea;
   public JButton timerButton;

   CardTableView(CardTableModel tableModel)
   {
      super(tableModel.getTitle());
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLayout(new BorderLayout());
      setSize(800, 600);
      setLocationRelativeTo(null);

      pnlComputerHand = new JPanel();
      pnlComputerHand.setLayout(new GridLayout(1, tableModel.getNumCardsPerHand()));
      TitledBorder cpuBorder = new TitledBorder("Computer");
      cpuBorder.setTitleJustification(TitledBorder.LEFT);
      cpuBorder.setTitlePosition(TitledBorder.TOP);
      pnlComputerHand.setBorder(cpuBorder);
      pnlComputerHand.setPreferredSize(new Dimension(750, 125));
      this.add(pnlComputerHand, BorderLayout.NORTH);
      
      pnlGameStatus = new JPanel();
      pnlGameStatus.setLayout(new GridLayout(1,1));
     
      TitledBorder gameStatusBorder = new TitledBorder("Game Status");
      gameStatusBorder.setTitleJustification(TitledBorder.LEFT);
      gameStatusBorder.setTitlePosition(TitledBorder.TOP);
      pnlGameStatus.setBorder(gameStatusBorder);
      pnlGameStatus.setPreferredSize(new Dimension(160, 320));
      this.add(pnlGameStatus, BorderLayout.WEST);
      
      pnlPlayArea = new JPanel();
      pnlPlayArea.setLayout(new GridLayout(tableModel.getNumPlayers(), tableModel.getNumPlayers()));
      TitledBorder playBorder = new TitledBorder("Last Played Cards");
      playBorder.setTitleJustification(TitledBorder.LEFT);
      playBorder.setTitlePosition(TitledBorder.TOP);
      pnlPlayArea.setBorder(playBorder);
      pnlPlayArea.setPreferredSize(new Dimension(140, 320));
      this.add(pnlPlayArea, BorderLayout.CENTER);
      
      pnlScoreBoard = new JPanel();
      pnlScoreBoard.setLayout(new GridLayout(2, tableModel.getNumCardsPerHand()/2));
      pnlScoreBoard.setPreferredSize(new Dimension(500, 320));
      TitledBorder humanPotBorder = new TitledBorder("Score Board");
      humanPotBorder.setTitleJustification(TitledBorder.LEFT);
      humanPotBorder.setTitlePosition(TitledBorder.TOP);
      pnlScoreBoard.setBorder(humanPotBorder);
      this.add(pnlScoreBoard, BorderLayout.EAST);
      
      pnlHumanArea = new JPanel();
      pnlHumanArea.setLayout(new BorderLayout());
      TitledBorder humanAreaBorder = new TitledBorder("You");
      humanAreaBorder.setTitleJustification(TitledBorder.LEFT);
      humanAreaBorder.setTitlePosition(TitledBorder.TOP);
      pnlHumanArea.setBorder(humanAreaBorder);
      pnlHumanArea.setPreferredSize(new Dimension(750, 185));
      this.add(pnlHumanArea, BorderLayout.SOUTH);
      
      pnlHumanHand = new JPanel();
      pnlHumanHand.setLayout(new GridLayout(1, tableModel.getNumCardsPerHand()));
      TitledBorder humanBorder = new TitledBorder("");
      humanBorder.setTitleJustification(TitledBorder.LEFT);
      humanBorder.setTitlePosition(TitledBorder.TOP);
      pnlHumanHand.setBorder(humanBorder);
      pnlHumanHand.setPreferredSize(new Dimension(750, 125));
      this.pnlHumanArea.add(pnlHumanHand, BorderLayout.NORTH);
      
      pnlPassArea = new JPanel();
      pnlPassArea.setLayout(new GridLayout(1,2));
      TitledBorder passBoarder = new TitledBorder("");
      passBoarder.setTitleJustification(TitledBorder.LEFT);
      passBoarder.setTitlePosition(TitledBorder.TOP);
      pnlPassArea.setBorder(passBoarder);
      pnlPassArea.setPreferredSize(new Dimension(750, 40));
      this.pnlHumanArea.add(pnlPassArea, BorderLayout.SOUTH);
      
      this.setVisible(true);
   }
   
   public void displayTextOnLabel(String text, JTextArea[] gameStatusText)
   {
      if (gameStatusText[0] != null) 
      {
         this.pnlGameStatus.remove(gameStatusText[0]);
      }
      JLabel label = new JLabel(text, JLabel.CENTER);
      label.setVerticalAlignment(JLabel.TOP);
      JTextArea textArea = new JTextArea(text);
      textArea.setLineWrap(true);
      textArea.setEditable(false);
      gameStatusText[0] = textArea;
      pnlGameStatus.add(gameStatusText[0]);
      this.setVisible(true);
   }
   
   public void displayTurnLabelForHuman(Boolean isHuman, JTextArea[] gameStatusText)
   {
      // add it again, designating turn
      if (isHuman)
      {
         displayTextOnLabel("Your Turn", gameStatusText);
      }
      else
      {
         displayTextOnLabel("Computer's Turn", gameStatusText);
      }
   }
   
   public void displayLabel(JPanel panel, JLabel label)
   {
      panel.add(label);
   }
   
   public void displayButton(JButton button, JLabel label)
   {
      button.setBounds(0, 65, 30, 30);
      label.add(button, JLabel.CENTER);
   }
   
   public void addTimerButton(GameTimerEvent ie)
   {
      this.timerButton = new JButton("0:00");
      this.timerButton.setName("timerButton");
      this.timerButton.addActionListener(new ActionListener() 
      {
         public void actionPerformed(ActionEvent e) 
         {
            ie.gameTimerEventToggle();
         }          
      });
      pnlPassArea.add(this.timerButton, JLabel.CENTER);
   }  

   public void updateTimerWithString(String string)
   {
      this.timerButton.setText(string);
   }
   public void updateTimerWithValueInSeconds(int seconds)
   {
      int minutes = (seconds % 3600) / 60;
      int totalSeconds = seconds % 60;
      String timeString = String.format("%02d:%02d", minutes, totalSeconds);
      this.updateTimerWithString(timeString);
   }

   // removes human's hand, and leaves the timer intact
   public void reloadHumansHand()
   {
      Component[] components = this.pnlHumanHand.getComponents();
      for (Component component : components) {
         if (component.getClass().equals(JLabel.class) && component.getName() != "timerButton") 
         {
            this.pnlHumanHand.remove(component);
         }
      }
   }
   //removes button so that the area can refresh
   public void reloadPassArea()
   {
      Component[] components = this.pnlPassArea.getComponents();
      for (Component component : components)
      {
         if (component.getClass().equals(JLabel.class) && component.getName() != "timerButton") 
         {
            this.pnlPassArea.remove(component);
         }
      }
   }
   //removes the current score so that it can be updated
   public void clearScoreboard()
   {
      Component[] components = this.pnlScoreBoard.getComponents();
      for (Component component : components)
      {
        this.pnlScoreBoard.remove(component);
      }
   }
   //updates the scoreboard
   public void updateScoreboard(int humanPass, int compPass)
   {
      String humanString = "Player: " + humanPass;
      String cpuString = "CPU: " + compPass;
      JLabel labelHuman = new JLabel(humanString, JLabel.CENTER);
      JLabel labelCpu = new JLabel(cpuString, JLabel.CENTER);
      this.pnlScoreBoard.add(labelCpu);
      this.pnlScoreBoard.add(labelHuman);
      
  }
   
}

   