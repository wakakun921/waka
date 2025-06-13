package jp.co.sss.shop.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.LoginForm;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.util.Constant;

/**
 * ログイン処理を担当するコントローラクラス
 * 一般会員および管理者のログイン認証と画面遷移を行う
 * 
 * @author TeamKode
 */
@Controller
public class LoginController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	HttpSession session;

	/**
	 * ログイン画面の初期表示処理
	 * セッションを無効化し、ログイン画面を表示
	 * 
	 * @param form ログインフォーム
	 * @return ログイン画面のテンプレートパス
	 */
	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public String login(@ModelAttribute LoginForm form) {
		// セッション情報を無効化（ログアウト相当）
		session.invalidate();
		return "login";
	}

	/**
	 * ログイン認証処理
	 * 入力内容に基づきログインし、権限ごとに画面を切り替え
	 * 
	 * @param form ログインフォーム
	 * @param result 入力チェック結果
	 * @return 一般会員: トップ画面へリダイレクト、管理者: 管理者メニューへリダイレクト、エラー時: ログイン画面再表示
	 */
	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public String doLogin(@Valid @ModelAttribute LoginForm form, BindingResult result) {
		String returnStr = "login";

		// バリデーションエラーがある場合
		if (result.hasErrors()) {
			session.invalidate();
			return returnStr;
		}

		// メールアドレスとパスワードでユーザー検索
		User user = userRepository.findByEmailAndPassword(form.getEmail(), form.getPassword());
		if (user == null) {
			// ユーザーが存在しない場合はログイン画面に戻る
			return returnStr;
		}

		// User → UserBean へ情報をコピー
		UserBean userBean = new UserBean();
		userBean.setId(user.getId());
		userBean.setEmail(user.getEmail());
		userBean.setName(user.getName());
		userBean.setPostalCode(user.getPostalCode());
		userBean.setAddress(user.getAddress());
		userBean.setPhoneNumber(user.getPhoneNumber());
		userBean.setAuthority(user.getAuthority());

		// セッションへユーザー情報を保存
		session.setAttribute("user", userBean);
		session.setAttribute("userId", user.getId());

		// 権限に応じて遷移先を決定
		Integer authority = userBean.getAuthority();
		if (authority.intValue() == Constant.AUTH_CLIENT) {
			// 一般会員の場合トップ画面へ
			returnStr = "redirect:/";
		} else {
			// 管理者の場合管理者メニューへ
			returnStr = "redirect:/admin/menu";
		}

		return returnStr;
	}

	/**
	 * 管理者メニュー画面の表示処理
	 * 
	 * @return 管理者メニュー画面のテンプレートパス
	 */
	@RequestMapping(path = "/admin/menu", method = RequestMethod.GET)
	public String showAdminMenu() {
		return "admin/admin_menu";
	}
}
