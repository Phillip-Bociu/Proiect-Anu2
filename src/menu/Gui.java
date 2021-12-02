package menu;

import java.util.ArrayList;
import java.util.List;

import main.Game;
import main.InputH;
import main.gfx.Colours;
import main.gfx.Font;
import main.gfx.Screen;

public class Gui {
	private InputH input;
	Game game;
	
	int option = 0;
	List<String> options;
	boolean walker = false;
	public Gui(Game game,InputH input)
	{
		this.game = game;
		this.input = input;
		options = new ArrayList<String>();
		options.add("Connect");
		options.add("Host");
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
				game.initWorld(false);
				game.Connect("26.176.53.192"); // IP of the server
				game.menu = false;
				break;
			case 1:
				game.initWorld(true);
				game.Host();
				game.menu = false;
				break;
			case 2:
				game.close();
				break;
			}
				
		}
			
	}
	public void render(Screen screen)
	{
		for(int y=0;y<screen.height;y++)
			for(int x=0;x<screen.width;x++)
				screen.render(x, y, 0, Colours.get(000,000,000,000), 1);
		
		Font.render("MAIN MENU", screen, screen.width/2 - 4*16*2, 10, Colours.get(-1,555,555,555), 2);
		for(int i=0;i<options.size();i++)
			if(i == option)
				Font.render(">" + options.get(i), screen, 0, 96 + i*16, Colours.get(-1,555,555,555), 1);
			else
				Font.render(options.get(i), screen, 16, 96 + i*16, Colours.get(-1,555,555,555), 1);
	}
}
