package so.zudui.entity;

import java.util.List;

public class Games {
	
private List<Game> games;
	
	public List<Game> getGamesCategory() {
		return games;
	}
	
	public static class Game {
	
		int id;
		String name;
		String icon;
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
	}
	
	
}
