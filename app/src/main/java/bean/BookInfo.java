package bean;


/**
 * 
 * @ClassName BookInfo.java Create on 2015-9-3
 * 
 * @company Copyright (c) 2015 by Vanroid Team
 * 
 * @author CGZ vsjosonadmin@163.com
 * 
 * @Description: 图书模型
 * 
 * @version 1.0
 */
public class BookInfo {

	/**
	 * 自增主键
	 */
	private int bookId;
	/**
	 * 书名
	 */
	private String title;
	/**
	 * 作者
	 */
	private String author; 
	/**
	 * 出版社
	 */
	private String press;
	/**
	 *  出版时间
	 */
	private String pressDate; 
	/**
	 * 索取号
	 */
	private String number; 
	/**
	 * 馆藏号
	 */
	private int dpt; 
	/**
	 *  可借数
	 */
	private int borrowableCount; 
	/**
	 *  详细链接
	 */
	private String href;
	/**
	 *  书的序号
	 */
	private int sortId;
	

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPress() {
		return press;
	}

	public void setPress(String press) {
		this.press = press;
	}

	public String getPressDate() {
		return pressDate;
	}

	public void setPressDate(String pressDate) {
		this.pressDate = pressDate;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getDpt() {
		return dpt;
	}

	public void setDpt(int dpt) {
		this.dpt = dpt;
	}

	public int getBorrowableCount() {
		return borrowableCount;
	}

	public void setBorrowableCount(int borrowableCount) {
		this.borrowableCount = borrowableCount;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public int getSortId() {
		return sortId;
	}

	public void setSortId(int sortId) {
		this.sortId = sortId;
	}


	public BookInfo(int id,String title, String author, String press,
			int borrowableCount, String href) {
		super();
		this.bookId = id;
		this.title = title;
		this.author = author;
		this.press = press;
		this.borrowableCount = borrowableCount;
		this.href = href;
	}


}
