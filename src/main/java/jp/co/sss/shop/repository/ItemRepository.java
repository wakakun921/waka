package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Item;

/**
 * itemsテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
	/**
	 * 商品IDを注文個数を参照して計数し、降順で表示
	 * @param deleteFlag 削除フラグ
	 * @return
	 */
	@Query("SELECT i FROM Item i ORDER BY " +
		       "(SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.item = i) DESC")
		List<Item> findAllByOrderByOrderCountDesc();



	/**
	 * 削除フラグの立っていない商品を商品登録日時を基に降順で表示
	 * @param deleteFlag
	 * @return
	 */
	public List<Item> findAllByOrderByInsertDateDesc();
	
	/**
	 * カテゴリ別に検索し、登録日時を基に降順で表示
	 * @param categoryId
	 * @param deleteFlag
	 * @return
	 */
	List<Item> findByCategoryIdOrderByInsertDateDesc(Integer categoryId);

	
	@Query("SELECT i FROM Item i WHERE i.category.id = :categoryId ORDER BY " +
		       "(SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi WHERE oi.item = i) DESC")
		List<Item> findByCategoryIdOrderByOrderCountDesc(@Param("categoryId") Integer categoryId);



	
	


	/**
	 * 商品情報を登録日付順に取得 管理者機能で利用
	 * @param deleteFlag 削除フラグ
	 * @param pageable ページング情報
	 * @return 商品エンティティのページオブジェクト
	 */
	@Query("SELECT i FROM Item i INNER JOIN i.category c WHERE i.deleteFlag =:deleteFlag ORDER BY i.insertDate DESC,i.id DESC")
	Page<Item> findByDeleteFlagOrderByInsertDateDescPage(
			@Param(value = "deleteFlag") int deleteFlag, Pageable pageable);

	/**
	 * 商品IDと削除フラグを条件に検索（管理者機能で利用）
	 * @param id 商品ID
	 * @param deleteFlag 削除フラグ
	 * @return 商品エンティティ
	 */
	public Item findByIdAndDeleteFlag(Integer id, int deleteFlag);

	/**
	 * 商品名と削除フラグを条件に検索 (ItemValidatorで利用)
	 * @param name 商品名
	 * @param notDeleted 削除フラグ
	 * @return 商品エンティティ
	 */
	public Item findByNameAndDeleteFlag(String name, int notDeleted);
}
