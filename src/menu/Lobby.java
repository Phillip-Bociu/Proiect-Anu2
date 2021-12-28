package menu;

import java.util.List;

import main.Game;
import main.InputH;
import main.gfx.Colours;
import main.gfx.Font;
import main.gfx.Screen;

public class Lobby {
	Game game;
	boolean host, enemyFound = false, ready = false;
	InputH input;
	String opponentUsername = "????";
	
	int option = 0;
	List<String> options;
	boolean walker = false;
	public Lobby(Game game, InputH input, boolean host)
	{
		this.input = input;
		this.game = game;
		this.host = host;
	}
	public void tick() {
		if(input.Y.pressed())
		{
			game.initWorld(host);
			ready = ready ? false : true;
		}
		if(input.N.isPressed())
		{
			game.initGui();
		}
	}
	public void render(Screen screen)
	{
		for(int y=0;y<screen.height;y+=16)
			for(int x=0;x<screen.width;x+=16)
				screen.render(x, y, 0, Colours.get(000,000,000,000), 1);
		if(enemyFound)
		{
			Font.render("Y - ready", screen, screen.width/2 - 10*8, 64 + 56, Colours.get(-1,555,555,555), 1);
			Font.render("Game", screen, screen.width/2 - 4*16, 16, Colours.get(-1,555,555,555), 2);
			if(ready)
				Font.render("-ready-", screen, screen.width/2 - 8*8, 16 + 32, Colours.get(-1,555,555,555), 1);
			else
				Font.render("-not ready-", screen, screen.width/2 - 12*8, 16 + 32, Colours.get(-1,555,555,555), 1);
		}
		else
		{
			Font.render("Waiting", screen, screen.width/2 - 7*16, 16, Colours.get(-1,555,555,555), 2);
			Font.render("for opponent", screen, screen.width/2 - 13*8, 16 + 32, Colours.get(-1,555,555,555), 1);
		}
		Font.render("N - return", screen, screen.width/2 - 10*8, 64 + 32, Colours.get(-1,555,555,555), 1);
		Font.render(game.account.username, screen, 1, screen.height-16, Colours.get(-1,555,555,555), 1);
		Font.render(opponentUsername, screen, screen.width - opponentUsername.length()*16, screen.height-16, Colours.get(-1,555,555,555), 1);
			
		int color,color2;
		if(host)
		{
			color = Colours.get(-1,401,502,555);
			color2 = Colours.get(-1,204,305,555);
		}
		else
		{
			color = Colours.get(-1,204,305,555);
			color2 = Colours.get(-1,401,502,555);
		}
		screen.render(0, screen.height- 128 , 28*32 + 10, color, 4);
		screen.render(64, screen.height- 128 , 28*32 + 11, color, 4);
		screen.render(0, screen.height- 128  + 64, 29*32 + 10, color, 4);
		screen.render(64, screen.height- 128  + 64, 29*32 + 11, color, 4);
		
		if(enemyFound)
		{
			screen.render(screen.width - 16, screen.height- 128 , 28*32 + 10, color2, true, false, 4);
			screen.render(screen.width - 80, screen.height- 128 , 28*32 + 11, color2, true, false, 4);
			screen.render(screen.width - 16, screen.height- 128  + 64, 29*32 + 10, color2, true, false, 4);
			screen.render(screen.width - 80, screen.height- 128  + 64, 29*32 + 11, color2, true, false, 4);
		}
		else
			Font.render("?", screen, screen.width - 64, screen.height- 80, Colours.get(-1,555,555,555), 4);
	}		
}

