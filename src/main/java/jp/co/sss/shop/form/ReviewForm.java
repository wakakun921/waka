package jp.co.sss.shop.form;

import java.io.Serializable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewForm implements Serializable{
	
	private Integer reviewId;
	
	private Integer itemId;
	
	@NotNull(message = "評価を入力してください。")
	@Min(1)
	@Max(5)
	private Integer star;
	
	@Size(min = 1,max = 50 ,message = "タイトルを入力してください。")
	private String title;
	
	@Size(min = 1,max = 400 ,message = "内容を入力してください。")
	private String content;

	public Integer getReviewId() {
		return reviewId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public Integer getStar() {
		return star;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public void setReviewId(Integer reviewId) {
		this.reviewId = reviewId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	

}
