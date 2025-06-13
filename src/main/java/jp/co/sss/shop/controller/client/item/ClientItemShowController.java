package jp.co.sss.shop.controller.client.item;

import static jp.co.sss.shop.util.Constant.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.bean.ReviewBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Review;
import jp.co.sss.shop.form.ReviewForm;
import jp.co.sss.shop.repository.CategoryRepository;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.ReviewRepository;
import jp.co.sss.shop.service.BeanTools;
import jp.co.sss.shop.util.Constant;

/**
 * 商品管理 一覧表示機能(一般会員用)のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class ClientItemShowController {

	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;

	/**
	 * Entity、Form、Bean間のデータコピーサービス
	 */
	@Autowired
	BeanTools beanTools;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ReviewRepository reviewRepository;

	/**
	 * トップ画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @return "index" トップ画面
	 */
	@RequestMapping(path = "/", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(Model model) {
		List<Item> items = itemRepository.findAllByOrderByOrderCountDesc();
		if (items.isEmpty()) {
			items = itemRepository.findAllByOrderByInsertDateDesc();
			model.addAttribute("sortType", DEFAULT_SORT_TYPE);
		}
		model.addAttribute("items", items);
		model.addAttribute("sortType", 2); // 売れ筋順
		return "index";
	}

	@RequestMapping(path = "/client/item/list/{sortType}")
	public String showItemListFindByCategoryIdAndDeleteFlagOrderByInsertDateDesc(
			@PathVariable("sortType") Integer sortType,
			@RequestParam(value = "categoryId", required = false) Integer categoryId,
			Model model) {

		System.out.println("categoryId = " + categoryId);

		List<Category> categories = categoryRepository.findByDeleteFlag(NOT_DELETED);
		model.addAttribute("categories", categories);

		List<Item> items;

		if (categoryId == null || categoryId == 0) {
			// カテゴリ指定なし（全件表示）
			if (sortType == DEFAULT_SORT_TYPE) {
				items = itemRepository.findAllByOrderByInsertDateDesc();
			} else if (sortType == 2) {
				items = itemRepository.findAllByOrderByOrderCountDesc();
			} else {
				items = itemRepository.findAllByOrderByInsertDateDesc();
			}
		} else {
			// カテゴリ指定あり
			if (sortType == DEFAULT_SORT_TYPE) {
				items = itemRepository.findByCategoryIdOrderByInsertDateDesc(categoryId);
			} else if (sortType == 2) {
				items = itemRepository.findByCategoryIdOrderByOrderCountDesc(categoryId);
			} else {
				items = itemRepository.findByCategoryIdOrderByInsertDateDesc(categoryId);
			}

			// categoryId が有効なときだけ category を取得してモデルに追加
			Category category = categoryRepository.findById(categoryId).orElse(null);
			model.addAttribute("category", category);
		}

		model.addAttribute("items", items);
		model.addAttribute("sortType", sortType);
		model.addAttribute("categoryId", categoryId);

		return "client/item/list";
	}
	
	@RequestMapping(path = "/client/item/detail/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public String showItemDetailAndReview(@PathVariable int id,
	                                       @ModelAttribute("reviewForm") @Valid ReviewForm form,
	                                       BindingResult result,
	                                       Model model,
	                                       RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
	    // 商品取得（論理削除考慮）
	    Item item = itemRepository.findByIdAndDeleteFlag(id, Constant.NOT_DELETED);
	    if (item == null) {
	        return "redirect:/syserror";
	    }

	    // 商品情報をViewに渡す
	    ItemBean itemBean = beanTools.copyEntityToItemBean(item);
	    model.addAttribute("item", itemBean);

	    // レビュー一覧取得
	    Item itemForReview = new Item();
	    itemForReview.setId(id);
	    List<Review> reviewEntities = reviewRepository.findByItemOrderByStarDesc(itemForReview);
	    List<ReviewBean> reviewBeans = beanTools.copyEntityListToReviewBeanList(reviewEntities);
	    model.addAttribute("reviews", reviewBeans);

	    // ★ POST時（submit時）の処理
	    if ("POST".equalsIgnoreCase(request.getMethod())) {
	        if (result.hasErrors()) {
	            // エラー：Flashスコープにエラー情報を退避してリダイレクト
	            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.reviewForm", result);
	            redirectAttributes.addFlashAttribute("reviewForm", form);
	            redirectAttributes.addFlashAttribute("validated", true);
	            return "redirect:/client/item/detail/" + id;
	            
	        }

	        // 成功：レビュー保存
	        Review review = new Review();
	        review.setStar(form.getStar());
	        review.setTitle(form.getTitle());
	        review.setContent(form.getContent());
	        review.setItem(item);
	        reviewRepository.save(review);

	        redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");
	        return "redirect:/client/item/detail/" + id;
	    }

	    return "client/item/detail"; // GET時 or リダイレクト後
	}

}
