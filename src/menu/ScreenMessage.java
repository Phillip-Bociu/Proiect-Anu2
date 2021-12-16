package menu;

import java.util.ArrayList;
import java.util.List;

import main.Game;
import main.InputH;
import main.gfx.Colours;
import main.gfx.Font;
import main.gfx.Screen;

public class ScreenMessage {
	private InputH input;
	Game game;
	
	int option = 0;
	List<String> options;
	boolean walker = false;
	public ScreenMessage(Game game,InputH input)
	{
		this.game = game;
		this.input = input;
		options = new ArrayList<String>();
		options.add("Return to Main Menu");
		options.add("Exit");
	}
	public void tick() {
		if(input.up.pressed()){
			if(option == 0)
				option = options.size()-1;
			else
				option--;
		}
		if(input.down.pressed()){
			option =(option+1)%options.size();
		}
		if(input.enter.isPressed())
		{
			switch(option) 
			{
			case 0:
				break;
			case 1:
				game.close();
				break;
			}
				
		}
			
	}
	public void render(Screen screen)
	{
		for(int y=0;y<screen.height;y+=16)
			for(int x=0;x<screen.width;x+=16)
				screen.render(x, y, 0, Colours.get(000,000,000,000), 1);
		
		Font.render("GAME OVER", screen, screen.width/2 - 4*16*2, screen.height/2-16*5, Colours.get(-1,555,555,555), 2);
		for(int i=0;i<options.size();i++)
			if(i == option)
				Font.render(">" + options.get(i), screen, screen.width/2 -16*10, 96 + i*16, Colours.get(-1,555,555,555), 1);
			else
				Font.render(options.get(i), screen, screen.width/2 -16*10 + 16, 96 + i*16, Colours.get(-1,555,555,555), 1);
	}
}

