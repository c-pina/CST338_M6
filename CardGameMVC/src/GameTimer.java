public class GameTimer extends Thread
{
   private GameTimerEvent ie;
   public Boolean timerOn = true;
   public int gameTime = 0;

   public GameTimer (GameTimerEvent event)
   {
      ie = event;
   }

   public void run() 
   {     
      this.startTimerLogic();
   }

   private void startTimerLogic()
   {
      try {
         while (this.timerOn) 
         {
            this.gameTime += 1;
            ie.gameTimerEventTick(this.gameTime);
            Thread.sleep(1000);
         }
      } 
      catch (InterruptedException e) 
      {
         e.printStackTrace();
      }
   }

   public void toggleTimer()
   {
      this.timerOn = !this.timerOn;
   }

   public void stopTimer()
   {
      this.timerOn = false;
   }

   public int getGameTime()
   {
      return this.gameTime;
   }

   public static void main(String args[]) 
   {
   }
}