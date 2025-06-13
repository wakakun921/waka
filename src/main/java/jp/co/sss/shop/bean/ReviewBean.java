package jp.co.sss.shop.bean;

public class ReviewBean {

	/**
	 * レビューID
	 */
	private Integer reviewId;

	/**
	 * 評価（1〜5）
	 */
	private Integer star;

	/**
	 * レビュ－タイトル
	 */
	private String title;

	/**
	 * レビュー内容
	 */
	private String content;

	/**
	 * 商品ID（必要なら）
	 */
	private Integer itemId;

	// ===== Getter & Setter =====

	public Integer getReviewId() {
		return reviewId;
	}

	public void setReviewId(Integer reviewId) {
		this.reviewId = reviewId;
	}

	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
}
