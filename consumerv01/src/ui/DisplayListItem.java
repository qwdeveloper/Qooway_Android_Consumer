package ui;

public class DisplayListItem {
	
	public int Id;
    public String ThumbNail;
    public String[] Info;

	public DisplayListItem(int id, String picture, String[] info) {
		Id = id;
		ThumbNail = picture;
		Info = info;
	}

}
