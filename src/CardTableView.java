import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

public class CardTableView extends JFrame {
   
   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea, pnlGameStatus, pnlHumanPot;
   


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
      pnlGameStatus.setPreferredSize(new Dimension(160, 350));
      this.add(pnlGameStatus, BorderLayout.WEST);
      
      pnlPlayArea = new JPanel();
      pnlPlayArea.setLayout(new GridLayout(tableModel.getNumPlayers(), tableModel.getNumPlayers()));
      TitledBorder playBorder = new TitledBorder("Last Played Cards");
      playBorder.setTitleJustification(TitledBorder.LEFT);
      playBorder.setTitlePosition(TitledBorder.TOP);
      pnlPlayArea.setBorder(playBorder);
      pnlPlayArea.setPreferredSize(new Dimension(140, 350));
      this.add(pnlPlayArea, BorderLayout.CENTER);
      
      pnlHumanPot = new JPanel();
      pnlHumanPot.setLayout(new GridLayout(2, tableModel.getNumCardsPerHand()/2));
      pnlHumanPot.setPreferredSize(new Dimension(500, 350));
      TitledBorder humanPotBorder = new TitledBorder("Your Winnings");
      humanPotBorder.setTitleJustification(TitledBorder.LEFT);
      humanPotBorder.setTitlePosition(TitledBorder.TOP);
      pnlHumanPot.setBorder(humanPotBorder);
      this.add(pnlHumanPot, BorderLayout.EAST);
      
      pnlHumanHand = new JPanel();
      pnlHumanHand.setLayout(new GridLayout(1, tableModel.getNumCardsPerHand()));
      TitledBorder humanBorder = new TitledBorder("You");
      humanBorder.setTitleJustification(TitledBorder.LEFT);
      humanBorder.setTitlePosition(TitledBorder.TOP);
      pnlHumanHand.setBorder(humanBorder);
      pnlHumanHand.setPreferredSize(new Dimension(750, 125));
      this.add(pnlHumanHand, BorderLayout.SOUTH);
      
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
   
}
