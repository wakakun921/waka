package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;

import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer>{
	List<Review> findByItemOrderByStarDesc(Item item);
}

