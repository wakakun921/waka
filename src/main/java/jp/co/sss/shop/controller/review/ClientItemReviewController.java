package jp.co.sss.shop.controller.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.ReviewRepository;
import jp.co.sss.shop.service.BeanTools;

@Controller
@RequestMapping("/client/item")
public class ClientItemReviewController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BeanTools beanTools;

    // 商品情報を itemBean として model に自動追加（講義資料のスコープ制御に準拠）
    @ModelAttribute("item")
    public ItemBean setUpItem(@PathVariable("id") Integer id) {
        Item item = itemRepository.findById(id).get();
        return beanTools.copyEntityToItemBean(item);
    }

    // 外部キー検索：商品に紐づくレビューを item 経由で取得
    /**@RequestMapping("/detail/{id}")
    public String showItemReview(@PathVariable("id") Integer id, Model model) {
        Item item = new Item();
        item.setId(id); // 外部キーエンティティにIDだけセット（講義資料と同じ）

        List<Review> reviewEntities = reviewRepository.findByItem(item); // 外部キーで条件検索
        List<ReviewBean> reviewBeans = beanTools.copyEntityListToReviewBeanList(reviewEntities);

        model.addAttribute("reviews", reviewBeans);	

        return "client/item/detail";*/
    }

